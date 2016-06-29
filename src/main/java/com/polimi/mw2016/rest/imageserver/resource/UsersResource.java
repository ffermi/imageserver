package com.polimi.mw2016.rest.imageserver.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.polimi.mw2016.rest.imageserver.Secured;
import com.polimi.mw2016.rest.imageserver.service.ImageService;
import com.polimi.mw2016.rest.imageserver.service.JsonManagerService;
import com.polimi.mw2016.rest.imageserver.service.UserService;

@Path("users")
public class UsersResource {
	
	//Inject controllers
	@Inject UserService us;
	@Inject ImageService is;
	@Inject JsonManagerService jmans;
	
	@Context ResourceContext rc;
	@Context ServletContext sc;
	@Context HttpServletRequest request;
	@Context SecurityContext sec;
	
	/**
	 * Retrieves the list of users
	 * @return a JSONArray containing all registered users
	 */
	@Secured
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public JsonArray getAllUsers(){	
		System.out.println("RESOURCE SERVER - /users - GET request performed by " + sec.getUserPrincipal().getName() );
		String uriPrefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/resources/users/";
		return jmans.buildAllUsersList(us.getAllUsers(), uriPrefix);
	}

	/**
	 * Register a new user
	 * @return redirect to the user's details view
	 */
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public Response register(JsonObject newUserJO){
		System.out.println("RESOURCE SERVER - /users - POST request");
		
		Integer idNewUser = null;
		Response response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
	
		List<String> parsedJSON = jmans.parseNewUser(newUserJO);
		if(parsedJSON == null || parsedJSON.isEmpty() || parsedJSON.get(0) == null ||parsedJSON.get(0).isEmpty()){
			//malformed JSON
			response = Response.status(Status.BAD_REQUEST).build();
		} else {
			
			idNewUser = us.addUser(parsedJSON.get(0), parsedJSON.get(1), parsedJSON.get(2), parsedJSON.get(3));
			
			if(idNewUser == null){
				//cannot create the user, username already taken.
				response = Response.status(Status.NOT_ACCEPTABLE)
						.entity("Username already taken!").type("text/plain")
						.build();
			}else{				
				URI createdUri = null;
				try {
					createdUri = new URI("/resources/users/" + String.valueOf(idNewUser));
					response = Response.created(createdUri).entity(us.getUser(idNewUser)).type("application/json").build();
				} catch (URISyntaxException e) {
					e.printStackTrace();				
				}
			}
		}
		
		return response;
	}
	
	/**
	 * Retrieve user’s basic info
	 * @param idUser the id of the user you want to know more
	 * @return a JSON containing the  user’s basic info
	 */
	@Secured
	@Path("{idUser}")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public JsonObject getUserInfo(@PathParam("idUser") int idUser){
		System.out.println("RESOURCE SERVER - /users/" + idUser + " - request performed by " + sec.getUserPrincipal().getName());
		String serverURI = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/resources/images";
		String uriPrefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/resources/users/";
		return jmans.buildUserInfo(us.getUser(idUser), uriPrefix, serverURI);
	}
	
	/*
	@Path("{userId}/images/{idImage}")
	public ImageSubResource getImage(@PathParam("idImage")int idImage){
		return rc.initResource(new ImageSubResource(us, is, idImage));
	}
	*/
}
