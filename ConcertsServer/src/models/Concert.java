package models;

import java.util.Calendar;

public class Concert {
	
	private String id;
	private String name;
	private Ticket[] ticketsList;	
	private double price;
	private Calendar date;
	
	public Concert(String id, String name, double price, Calendar date, int slot) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.date = date;
		ticketsList = new Ticket[slot];
		initTickets();
	}
	
	public Concert(String name) {
		this.name = name;
	}
	
	private void initTickets() {
		for (int i = 0; i < ticketsList.length; i++) {
			ticketsList[i] = new Ticket(id + "-" + i);
		}
	}

	private int freeTicketsCount() {
		int counter = 0;
		for (int i = 0; i < ticketsList.length; i++) {
			if(ticketsList[i] == null) {
				counter++;
			}
		}
		return counter;
	}
	
	public String[] dataToVector() {
		String[] dataVector = {id,name,date.toString(), "" + freeTicketsCount(), "" + price};
		return dataVector;
	}
	
	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public Calendar getDate() {
		return date;
	}
	
	public String getId() {
		return id;
	}

	public Ticket[] getTicketsList() {
		return ticketsList;
	}
}
