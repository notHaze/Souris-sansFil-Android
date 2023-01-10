package backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.remotedesktopcontrol.message.Message;
import com.remotedesktopcontrol.message.MouseClick;
import com.remotedesktopcontrol.message.MouseCoordinate;
import com.remotedesktopcontrol.message.TypeBouton;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
public class Server {
	
	private Robot robot;
	private ServerSocket server;
	private Socket socket;
	private ObjectInputStream reader;
	private int port;
	private boolean continuer;
	private Thread recevoirCommand;
	

	public Server() {
		
		try {
			port = 4665;
			server = new ServerSocket(port);
			System.out.println("Server done and listen on port "+server.getLocalPort());
			
			try {
				robot = new Robot();
			} catch (AWTException e1) {
				
				e1.printStackTrace();
			}
			
		} catch (IOException e) {
			try {
				server.close();
			} catch (IOException e1) {

			}
		}
		
		
	}
	
	public void start() {
		recevoirCommand = new Thread( new Runnable() {
			
			@Override
			public void run() {
				
				try {
					socket = server.accept();
					System.out.println("Connected");
					reader = new ObjectInputStream (socket.getInputStream());

					continuer = true;
					try {
						continuer = doAction(reader.readObject());
			        } catch (SocketException | ClassNotFoundException e) {
			        	continuer=false;
			     	   	close();
			        }
					
			        while(continuer){
			           try {
			        	   continuer = doAction(reader.readObject());
			           } catch (SocketException | ClassNotFoundException e) {
			        	    continuer=false;
			        	    close();
			        	   	
			           }
			        }
				} catch (IOException e) {
					
				}
				
			}
			
		});
		recevoirCommand.start();

		
	}
	
	public void close() {
		try {
			continuer=false;
			recevoirCommand.join();
			socket.close();
			server.close();
			reader.close();
			System.out.println("Déconnecté");
			
		} catch (IOException e) {
			
			
		} catch (InterruptedException e) {

		}
		start();
		
	}
	
	public Boolean doAction(Object o) {
		if (o==null) {
			return false;
		} else {
			if (o instanceof MouseCoordinate) {
				MouseCoordinate inputAdd= (MouseCoordinate)o;
				MouseCoordinate coord = new MouseCoordinate(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
				coord = coord.add(inputAdd);
				robot.mouseMove(coord.getX(), coord.getY());
			} else if (o instanceof MouseClick) {
				MouseClick inputTouche= (MouseClick)o;
				if (inputTouche.getValue()>=1) {
					TypeBouton button = inputTouche.getTypeBouton();
					switch(button) {
						case BOUTON_1:
							robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
							break;
						case BOUTON_2:
							robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
							break;
						case BOUTON_3:
							robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
							break;
						case SCROLL:
							robot.mouseWheel(inputTouche.getValue());
					}
				} else {
					TypeBouton button = inputTouche.getTypeBouton();
					switch(button) {
						case BOUTON_1:
							robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
							break;
						case BOUTON_2:
							robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
							break;
						case BOUTON_3:
							robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
							break;
						case SCROLL:
							robot.mouseWheel(inputTouche.getValue());
					}
				}
			}
			return true;
		}
	}
	
	public int getPort() {
		return this.port;
	}
}
