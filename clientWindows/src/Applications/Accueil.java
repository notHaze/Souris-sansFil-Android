package Applications;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import backend.Server;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JTextPane;
import javax.swing.JTextArea;

public class Accueil {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Accueil window = new Accueil();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Accueil() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		
		InetAddress ipLocal = null;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("google.com", 80));
			ipLocal = socket.getLocalAddress();
			socket.close();
		} catch (IOException e) {

		}
	
		String ip = "";
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
			                whatismyip.openStream()));
	
			ip = in.readLine();

		} catch (Exception e) {
			try {
				URL whatismyip = new URL("https://ipv4.icanhazip.com/");
				BufferedReader in = new BufferedReader(new InputStreamReader(
				                whatismyip.openStream()));
		
				ip = in.readLine();
				
			} catch (Exception e1) {
				try {
					URL whatismyip = new URL("https://myexternalip.com/raw");
					BufferedReader in = new BufferedReader(new InputStreamReader(
					                whatismyip.openStream()));
			
					ip = in.readLine();
					;
				} catch (Exception e2) {
					
				}
			}
		}
			
		Server server = new Server();
		
		String texteConnexion = String.format("%20s", "Local connexion : \n")
									+String.format("%30s", "ip : "+ipLocal.toString()+"\n")
									+String.format("%30s", "port : "+server.getPort()+"\n")
								+String.format("%20s", "Wi-Fi connexion : \n")
									+String.format("%30s", "ip : "+ip+"\n")
									+String.format("%30s", "port : "+server.getPort()+"\n");
		
		JTextArea textArea = new JTextArea("Infos connexion : \n\n\n"+texteConnexion);
		textArea.setEditable(false);
		frame.getContentPane().add(textArea, BorderLayout.CENTER);
		
		JLabel Labeltitre = new JLabel("Remote Desktop Control");
		Labeltitre.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(Labeltitre, BorderLayout.NORTH);
		
		server.start();
		

	}

}
