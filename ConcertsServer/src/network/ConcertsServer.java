package network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Timer;

import models.ConcertsManager;
import persistence.JSONManager;

public class ConcertsServer {
	
	private ServerSocket serverSocket;
	private boolean serverOn;
	private ArrayList<Connection> connections;
	private ServerListener serverListener;
	private JSONManager json;
	private ConcertsManager mainConcertsManager;
	private Timer updateClientsTimer;
	
	
	public ConcertsServer() throws IOException {
		mainConcertsManager = new ConcertsManager();
		json = new JSONManager();
		serverListener = new ServerListener(this);
		connections = new ArrayList<>();
		serverSocket = new ServerSocket(3000);
		Logger.getGlobal().log(Level.INFO, "Opened server at port: " + serverSocket.getLocalPort());
		initConnectionsThread();
		initUpdateClientsTimer();
	}
	
	private void initConnectionsThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				serverOn = true;
				while(serverOn) {
					try {
						Socket socket = serverSocket.accept();
						System.out.println("new Client at: " + socket.getRemoteSocketAddress().toString());
						connections.add(new Connection(socket, serverListener,json));
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void updateConcertsData(String jsonData) {
		mainConcertsManager.addConcertToData(jsonData);
	}
	 
	public String giveTicketsData(String concertName) {
		return mainConcertsManager.giveTicketsData(concertName);
	}
	
	public static void main(String[] args) {
		try {
			new ConcertsServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean processPurchaseRequest(String ticketsCodeToPurchase, String clientCode) {
		return mainConcertsManager.processPurchaseRequest(ticketsCodeToPurchase, clientCode);
	}
	
	private void updateClientsConcertsList() {
		for (Connection connection : connections) {
			connection.sendConcertsToClient();
		}
	}
	
	
	private void initUpdateClientsTimer() {
		updateClientsTimer = new Timer(60000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(connections.size() > 0) {
					updateClientsConcertsList();
				}
			}
		});
		updateClientsTimer.start();
	}
}