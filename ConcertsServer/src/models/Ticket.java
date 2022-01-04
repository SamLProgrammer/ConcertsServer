package models;

public class Ticket {
	
	private boolean available;
	private String ticketId;
	private String clientId;

	public Ticket(String id) {
		this.ticketId = id;
		available = true;
	}
	
	public boolean isAvailable() {
		return available;
	}

	public String getClientId() {
		return clientId;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public String getId() {
		return ticketId;
	}
	
}
