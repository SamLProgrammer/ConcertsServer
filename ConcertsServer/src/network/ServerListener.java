package network;

public class ServerListener {
 
	private ConcertsServer server;
	
	public ServerListener(ConcertsServer server) {
		this.server = server;
	} 
	
	public void updateConcertsData(String newConcertAsJson) {
		server.updateConcertsData(newConcertAsJson);
	}
	
	public String giveTicketsData(String jsonData) {
		System.out.println("boton ok pidió a server: " + jsonData);
		return server.giveTicketsData(jsonData);
	}

	public boolean processPurchaseRequest(String ticketsCodeToPurchase, String clientCode) {
		return server.processPurchaseRequest(ticketsCodeToPurchase, clientCode);
	}
}
