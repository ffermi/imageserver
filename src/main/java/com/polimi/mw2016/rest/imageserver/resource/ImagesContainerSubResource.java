package com.polimi.mw2016.rest.imageserver.resource;

import javax.json.JsonArray;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.polimi.mw2016.rest.imageserver.service.JsonManagerService;
import com.polimi.mw2016.rest.imageserver.service.UserService;

//DA CANCELLARE
public class ImagesContainerSubResource {
	
	//NB: this is a sub-resource, and these EJBs cannot be injected directly
	private UserService us;
	private JsonManagerService jmans;
	private HttpServletRequest req;
	private int idUser;
		
	public ImagesContainerSubResource(UserService us, JsonManagerService jmans, HttpServletRequest req, int idUser) {
		this.us = us;
		this.jmans = jmans;
		this.req = req;
		this.idUser = idUser;
	}

	/**
	 * Retrieve all images owned by a specific user.
	 * @return a JSONArray containing info about the images owned by a specific user 
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonArray getAllImages(){
		String serverURI = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/resources/users/" + idUser +"/images";
		return jmans.buildUserImageContainer(us.getUser(idUser),serverURI);
	}
	
	/*
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addImage(
			@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception {
			
		Response response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
		//URI IMAGE  resourcePath +idUser+ "/" + imageName
		String imageName = idUser + fileMetaData.getFileName();
	    String uploadPath = sc.getRealPath("images/")+"\\";	   
	    String filePath = uploadPath + imageName;
	    
	    if(is.getImage(imageName, idUser)!= null){
	    	response = Response.status(Status.NOT_ACCEPTABLE)
					.entity("You have already uploaded an image with that name!").type("text/plain")
					.build();
	    }else{
		    try{
		    	is.storeImage(fileInputStream, filePath);
		    } catch (IOException e){
		    	System.out.println("errore nel salvataggio su disco");
		    	return response;
		    }
		    System.out.println("Image stored into disk.");
		    
		    Integer idNewImage = is.saveImage(filePath, imageName, us.getUser(idUser));
		    if (idNewImage != null) {
		    	URI createdUri = null;
				try {
					createdUri = new URI("/resources/users/" + idUser + "/images/" + idNewImage);
					response = Response.created(createdUri).entity(is.getImage(idNewImage)).build();
				} catch (URISyntaxException e) {
					System.out.println("errore nella creazione dell'URI");
					e.printStackTrace();				
				}
		    }
	    }
	    return response;
	}
	*/
}
