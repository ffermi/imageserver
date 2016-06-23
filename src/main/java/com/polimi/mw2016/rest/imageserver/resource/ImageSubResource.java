package com.polimi.mw2016.rest.imageserver.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.polimi.mw2016.rest.imageserver.service.ImageService;
import com.polimi.mw2016.rest.imageserver.service.UserService;

@RequestScoped
public class ImageSubResource {
	
	//NB: this is a sub-resource, and these EJBs cannot be injected directly
	private ImageService is;
	private int idImage;
	
	public ImageSubResource(UserService us, ImageService is, int idImage) {
		this.is = is;
		this.idImage = idImage;
	}
		
	/**
	 * Retrieve a specific image
	 * @return the image (jpeg)
	 */
	@GET
	@Produces("image/jpeg")
	public Response getImage(){
		Response response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
		if (is.getImage(idImage) == null){
			response = Response.status(Status.NO_CONTENT).build();
		}else{
			String filePath = is.getImage(idImage).getPath();
			byte[] bImage;
	        File file = new File(filePath);
	        bImage = new byte[(int)file.length()];
	        FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(file);
		        fileInputStream.read(bImage);
		        fileInputStream.close();
				response = Response.ok(bImage).build();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}

	
}
