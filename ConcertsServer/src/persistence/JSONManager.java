package persistence;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.Concert;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JSONManager {

	private static final String FILE_PATH = "./files/concertsFullData.json";
	private static final String FILE_DIRECTORY = "./files";
	
	public boolean persistenceExists() {
		boolean flag = true;
		File file = new File(FILE_PATH);
		if(!file.exists() || file.length() == 0) {
				file = new File(FILE_DIRECTORY);
				flag = false;
				try {
					file.mkdir();
					file = new File(FILE_PATH);
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return flag;
	}
	
	public void writeFullData(ArrayList<Concert> concerts) throws IOException {
		if(concerts != null) {
			for (Concert concert : concerts) {
				for (int i = 0; i < concert.getTicketsList().length; i++) {
					concert.getTicketsList()[i].getId();
				}
			}
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String toWrite = gson.toJson(concerts);
			FileWriter fileWriter = new FileWriter(FILE_PATH);
			fileWriter.write(toWrite);
			fileWriter.close();
		}
	}
	
	public ArrayList<Concert> loadFullData(){
		ArrayList<Concert> concertsList = new ArrayList<>();
		Gson gson = new Gson();
		JsonParser jsonParser = new JsonParser();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(FILE_PATH));
			JsonElement jsonElement = jsonParser.parse(br);
			Type type = new TypeToken<ArrayList<Concert>>(){}.getType();
			concertsList =  gson.fromJson(jsonElement, type);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return concertsList;        
	}
	
	public String loadFullDataAsString() {
		String fullDataString = "";
		JsonParser jsonParser = new JsonParser();
		BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(FILE_PATH));
				JsonElement jsonElement = jsonParser.parse(br);
				fullDataString = jsonElement.toString();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return fullDataString;        
	}
	
	public ArrayList<Concert> decodeConcertsList(String string) {
		ArrayList<Concert> concertsList = new ArrayList<>();
		Gson gson = new Gson();
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(string);
		Type type = new TypeToken<ArrayList<Concert>>(){}.getType();
		concertsList =  gson.fromJson(jsonElement, type);
		return concertsList;   
	}
	
	public String loadClientsConcertsList() {
		String string = "";
		ArrayList<Concert> concertsList = new ArrayList<>();
		Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
			Gson gson = new GsonBuilder()
		            .addDeserializationExclusionStrategy(new ExclusionStrategy() {
		                @Override
		                public boolean shouldSkipField(FieldAttributes f) {
		                    return f.getName().toLowerCase().contains("id") ||
		                    	   f.getName().toLowerCase().contains("price") ||
		                    	   f.getName().toLowerCase().contains("date");
		                }

		                @Override
		                public boolean shouldSkipClass(Class<?> aClass) {
		                    return false;
		                }
		            }).setPrettyPrinting().create();
		    JsonParser jsonParser = new JsonParser();
		        BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(FILE_PATH));
					JsonElement jsonElement = jsonParser.parse(br);
					Type type = new TypeToken<ArrayList<Concert>>(){}.getType();
					concertsList = gson.fromJson(jsonElement, type);
					string = gson2.toJson(concertsList);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
		    return string;   
	}
	
	public Concert decodeConcertJson(String concert) {
		Gson gson = new Gson();
		Concert newConcert = gson.fromJson(concert, Concert.class);
		return newConcert;
	}
	
	public String decodeStringJson(String jsonString) {
		Gson gson = new Gson();
		String string = gson.fromJson(jsonString, String.class);
		return string;
	} 
	
	public String encodeConcert(Concert concert) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(concert);
	}

	public ArrayList<String> decodePurchase(String jsonTicketsToPurchaseList) {
		ArrayList<String> ticketsToPurchaseList = new ArrayList<>();
		Gson gson = new Gson();
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(jsonTicketsToPurchaseList);
		Type type = new TypeToken<ArrayList<String>>(){}.getType();
		ticketsToPurchaseList =  gson.fromJson(jsonElement, type);
		return ticketsToPurchaseList;   
	}
}