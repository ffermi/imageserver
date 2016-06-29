package com.polimi.mw2016.rest.imageserver.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.polimi.mw2016.rest.imageserver.model.Image;
import com.polimi.mw2016.rest.imageserver.model.User;

@Stateless
public class JsonManagerService {	
	
	public JsonManagerService(){
	}
	
	/**
	 * 
	 * @param users a list of all registered users
	 * @return a JSONArray containing all registered users (id, username) 
	 */
	public JsonArray buildAllUsersList(List<User> users, String uriPrefix){
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (User u : users){
			//create link
			JsonArray linkJA = userLinks(u, uriPrefix);
			//create user
			jab.add(Json.createObjectBuilder()
		            .add("id", u.getIdUser())
		            .add("username", u.getUsername())
		            .add("links", linkJA)
		            );
		}
		JsonArray ja = jab.build();
		return ja;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public JsonObject buildUserInfo(User user, String uriPrefix, String serverURI){
		
		JsonObject userJO = null;
		if(user == null){
			userJO = Json.createObjectBuilder().build();
		} else {
			JsonArrayBuilder jab = Json.createArrayBuilder();
			for (Image img : user.getImages()){
				//create link img
				JsonArray linkJA = imageLinks(img, serverURI);
				
				jab.add(Json.createObjectBuilder()
			            .add("id", img.getIdImage())
			            .add("title", img.getTitle())
			            .add("links",linkJA) 
			            );
			}
			JsonArray ja = jab.build();
			
			
			//build json object 
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("idUser", user.getIdUser())
				.add("username", user.getUsername())
				.add("firstname", user.getFirstname())
				.add("lastname", user.getLastname())
				.add("images", ja)
				.add("links", userLinks(user, uriPrefix));
			userJO = job.build();
		}
				
		return userJO;
	}
	
	/**
	 * 
	 * @param newUserJO
	 * @return
	 */
	public List<String> parseNewUser(JsonObject newUserJO){
		List<String> attributes = new ArrayList<>();
		//get the object
		newUserJO = newUserJO.getJsonObject("newuser");
		
		//get parameters
		attributes.add(newUserJO.getString("username"));
		attributes.add(newUserJO.getString("password"));
		attributes.add(newUserJO.getString("firstname"));
		attributes.add(newUserJO.getString("lastname"));
		
		//check
		System.out.println("Parsed newUser: "
				+ "(" + attributes.get(0) + ", " + attributes.get(1) 
				+ ", "+ attributes.get(2) + ", " + attributes.get(3) 
				+ ")");
		
		return attributes;
	}
	
	public JsonArray buildUserImageContainer(User user, String serverURI){
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Image i : user.getImages()){
			jab.add(Json.createObjectBuilder()
		            .add("id", i.getIdImage())
		            .add("title", i.getTitle())
		            .add("owner", i.getUser().getUsername())
		            .add("link",serverURI + "/" + i.getIdImage()) 
		            );
		}
		JsonArray ja = jab.build();
		return ja;
	}
	
	public JsonArray buildAllImagesList(List<Image> images, String serverURI){
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Image i : images){
			//create link
			JsonArray linksJA = imageLinks(i, serverURI);
			
			jab.add(Json.createObjectBuilder()
		            .add("id", i.getIdImage())
		            .add("title", i.getTitle())
		            .add("owner", i.getUser().getUsername())
		            .add("links",linksJA) 
		            );
		}
		JsonArray ja = jab.build();
		return ja;
	}
	
	private JsonArray imageLinks(Image image, String serverURI){
		JsonArrayBuilder linkJAB = Json.createArrayBuilder();
		linkJAB.add(
				Json.createObjectBuilder()
				.add("rel","self")
				.add("href", serverURI + "/" + image.getIdImage())
				);
		 return linkJAB.build();
	}
	private JsonArray userLinks(User user, String uriPrefix){
		JsonArrayBuilder linkJAB = Json.createArrayBuilder();
		linkJAB.add(
				Json.createObjectBuilder()
				.add("rel","self")
				.add("href", uriPrefix + user.getIdUser())
				);
		 return linkJAB.build();
	}
}
