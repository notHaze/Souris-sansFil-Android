package com.remotedesktopcontrol.backend;
import android.util.ArrayMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

import com.remotedesktopcontrol.message.Message;

public class Client {

	private Socket connexion;
	private ObjectOutputStream outputObject;
	private Collection<Message> queue;
	private Boolean exec=true;
	private Thread sendData;

	public Client(String ip, int Port) throws IOException {
		connexion = new Socket(ip, Port);
		outputObject = new ObjectOutputStream(connexion.getOutputStream());
		queue = Collections.synchronizedList(new ArrayList<Message>());

		sendData = new Thread( new Runnable() {
			@Override
			public void run() {
				while(exec) {
					if (queue.size()>0) {
						try {
							Message m = getQueue(0);
							if (m!=null) {
								outputObject.writeObject(m);
								outputObject.flush();
								removeQueue(m);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}
			}
		});
		sendData.start();

	}

	public String toString() {
		return connexion.getInetAddress() + ":" + connexion.getPort();
	}
	
	public void close() {
		try {
			exec=false;
			sendData.join();
			outputObject.flush();
			outputObject.close();
			connexion.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public  void addQueue(Message m) {
		queue.add(m);
	}

	public void removeQueue(Message m) {
		queue.remove(m);
	}

	public Message getQueue(int index) {
		int cmp=0;
		for (Message m : queue) {
			if (cmp==index) {
				return m;
			}
			cmp++;
		}
		return null;
	}
}