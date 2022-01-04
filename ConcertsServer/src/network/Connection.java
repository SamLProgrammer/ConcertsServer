package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import persistence.JSONManager;

public class Connection {

	private Socket socket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	private boolean connectionStatus;
	private ServerListener serverListener;
	private JSONManager json;
	
	public Connection(Socket socket, ServerListener serverListener, JSONManager json) throws IOException {
		connectionStatus = true;
		this.json = json;
		this.socket = socket;
		this.serverListener = serverListener;
		initStreams();
		Logger.getGlobal().log(Level.INFO, 
				"Nuevo cliente." + socket.getInetAddress().getHostAddress());
		initComunication();
	}
	
	private void initStreams() throws IOException {
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(socket.getOutputStream());
	}
	
	private void initComunication() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(connectionStatus) {
					try {
						String trace = inputStream.readUTF();
						switch (Requests.valueOf(trace)) {
						case UPDATE_SERVER_DATA:
							catchNewData();
							break;
						case ASK_FOR_FULL_DATA:
							sendFullDataToAdmin();
							break;
						case ASK_FOR_CLIENT_CONCERTS:
							sendConcertsToClient();
							break;
						case CLIENT_ASK_FOR_TICKETS:
							sendTicketsDataToClient();
							break;
						case ASK_FOR_TICKETS_PURCHASE:
							sendTicketsPurchaseResponse();
							break;
						default: 
							break;
						}
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
			}
		}).start();
	}
	
	public void catchNewData()  {
		try {
			String string = inputStream.readUTF();
			serverListener.updateConcertsData(string);
			outputStream.writeUTF(Requests.FULL_DATA_WRITEN.toString());
		} catch (IOException e) {
			e.printStackTrace();
			}
	}
	
	public void sendFullDataToAdmin() {
		try {
			outputStream.writeUTF(Requests.FULL_DATA_TO_ADMIN.toString());
			outputStream.writeUTF(json.loadFullDataAsString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateEvery30() {
		try {
			outputStream.writeUTF(Requests.UPDATE_30.toString());
			String string = json.loadClientsConcertsList();
			outputStream.writeUTF(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendConcertsToClient() {
		try {
			outputStream.writeUTF(Requests.SERVER_TO_CLIENT_CONCERTS.toString());
			outputStream.writeUTF(json.loadClientsConcertsList());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendTicketsDataToClient() {
			try {
				outputStream.writeUTF(Requests.SERVER_TICKETS_TO_CLIENT.toString());
				outputStream.writeUTF(serverListener.giveTicketsData(inputStream.readUTF()));
			} catch (IOException e) {
				e.printStackTrace();
			}
	} 
	
	public void sendTicketsPurchaseResponse() {
		try {
			if(serverListener.processPurchaseRequest(inputStream.readUTF(),inputStream.readUTF())) {
				outputStream.writeUTF(Requests.PURCHASE_COMPLETED.toString());
			}
			else {
				outputStream.writeUTF(Requests.PURCHASE_ERROR.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public DataOutputStream getOutputStream() {
		return outputStream;
	}
	
	public DataInputStream getInputStream() {
		return inputStream;
	}
}