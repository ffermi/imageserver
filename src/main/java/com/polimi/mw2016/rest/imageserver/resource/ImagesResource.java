package com.polimi.mw2016.rest.imageserver.resource;


import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.polimi.mw2016.rest.imageserver.Secured;
import com.polimi.mw2016.rest.imageserver.service.ImageService;
import com.polimi.mw2016.rest.imageserver.service.JsonManagerService;
import com.polimi.mw2016.rest.imageserver.service.UserService;


@Path("images")
public class ImagesResource {
	
	//Inject controllers
	@Inject UserService us;
	@Inject ImageService is;
	@Inject JsonManagerService jmans;
	
	@Context ResourceContext resourceContext;
	@Context HttpServletRequest req;
	@Context SecurityContext securityContext;
	

	/**
	 * Retrieves the list of all images 
	 * @return a JSONArray containing all the images (ordered by userId)
	 */
	@Secured
	@GET
	public JsonArray getAllImages(){
		System.out.println("scheme:"+securityContext.getAuthenticationScheme());
		String serverURI = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/resources/images";
		return jmans.buildAllImagesList(is.getAllImages(), serverURI);
	}
	
	/**
	 * 
	 * @param servletContext
	 * @param fileInputStream
	 * @param fileMetaData
	 * @return
	 * @throws Exception
	 */
	@Secured
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addImage(
			@Context ServletContext servletContext,
			@Context SecurityContext securityContext,
			@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception {
			
		String loggedUser = securityContext.getUserPrincipal().getName();
		int idUser = us.getUser(loggedUser).getIdUser();  
		Response response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
		String imageName = idUser + "-" + fileMetaData.getFileName();
	    String uploadPath = servletContext.getRealPath("images/")+"\\";	   
	    String filePath = uploadPath + imageName;
	    
	    System.out.println("imgname: "+ imageName+"\n user: "+String.valueOf(idUser));
	    
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
				response = Response.status(Status.CREATED).entity("Image uploaded!").type("text/plain").build();
		    }
	    }
	    return response;
	}
	
	/**
	 * Retrieve a specific image
	 * @param idImage 
	 * @return the image (jpeg)
	 */
	@Secured
	@Path("{idImage}")
	public ImageSubResource getImage(@PathParam("idImage") int idImage){
		return resourceContext.initResource(new ImageSubResource(us, is, idImage));
	}
	
}
