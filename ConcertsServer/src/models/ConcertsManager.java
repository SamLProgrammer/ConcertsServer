package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import persistence.JSONManager;

public class ConcertsManager {

	private ArrayList<Concert> concertsList;
	private JSONManager json;
	
	public ConcertsManager() {
		json = new JSONManager();
		concertsList = new ArrayList<>();
		if(json.persistenceExists()) {
		initModel();
		}
	}
	
	private void initModel() {
		concertsList = json.loadFullData();
	}
	
	public Concert createConcert(String id, String name, double price, Calendar date, int slots) {
		return new Concert(id, name, price, date, slots);
	}
	
	public void addConcert(Concert concert) {
		concertsList.add(concert);
	}
	
	public void removeConcert(String id) {
		for (Concert concert : concertsList) {
			if(concert.getId() == id) {
				concertsList.remove(concert);
			}
		}
	}

	public ArrayList<Concert> getConcertsList() {
		return concertsList;
	}
	
	public void addConcertToData(String newConcertAsJson) {
		concertsList.add(json.decodeConcertJson(newConcertAsJson));
		try {
			json.writeFullData(concertsList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String giveTicketsData(String jsonConcertName) {
		String concertName = json.decodeStringJson(jsonConcertName); 
		String ticketsData = "";
		for (Concert concert : concertsList) {
			if(concert.getName().equalsIgnoreCase(concertName)) {
				ticketsData = json.encodeConcert(concert);
			}
		}
		return ticketsData;
	}
	
	public void overWriteFullData() {
		try {
			json.writeFullData(concertsList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean processPurchaseRequest(String ticketsCodeToPurchase, String clientCode) {
		boolean flag = false;
		boolean resultFlag = false;
		String concertCode = "";
		ArrayList<String> codesList = json.decodePurchase(ticketsCodeToPurchase);
		for (String string : codesList) {
			concertCode = extractConcertCodeFromPurchasCode(string);
			for (Concert concert : concertsList) {
				if(concert.getId().equalsIgnoreCase(concertCode)) {
					for (int i = 0; i < concert.getTicketsList().length; i++) {
						if(concert.getTicketsList()[i].getId().equalsIgnoreCase(string) &&
						   !concert.getTicketsList()[i].isAvailable()) {
							flag = true;
						}
					}
				}
			}
		}
		if(!flag) {
			resultFlag = true;
			carryOutPurchase(codesList, concertCode, clientCode);
			overWriteFullData();
		}
		return resultFlag;
	}
	
	public void carryOutPurchase(ArrayList<String> codesList, String codeList, String clientCode) {
		for (String string : codesList) {
			String concertCode = extractConcertCodeFromPurchasCode(string);
			for (Concert concert : concertsList) {
				if(concert.getId().equalsIgnoreCase(concertCode)) {
					for (int i = 0; i < concert.getTicketsList().length; i++) {
						if(concert.getTicketsList()[i].getId().equalsIgnoreCase(string)) {
							concert.getTicketsList()[i].setAvailable(false);
							concert.getTicketsList()[i].setClientId(clientCode);;							
						}
					}
				}
			}
		}
	}
	
	private String extractConcertCodeFromPurchasCode(String string) {
		String concertCode = "";
		int i = 0;
		while(string.charAt(i) != '-') {
			concertCode += string.charAt(i);
			i++;
		}
		return concertCode;
	}
}
