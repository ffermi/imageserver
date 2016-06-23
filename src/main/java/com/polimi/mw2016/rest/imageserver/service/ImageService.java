package com.polimi.mw2016.rest.imageserver.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.polimi.mw2016.rest.imageserver.model.Image;
import com.polimi.mw2016.rest.imageserver.model.User;

@Stateless
public class ImageService {

	@PersistenceContext(unitName="imageserver_pu")
	private EntityManager em;

	public ImageService(){
	}
	
	public List<Image> getAllImages(){
		List<Image> allImages = new ArrayList<>();
		allImages = (List<Image>)em.createNamedQuery("Image.findAllOrderByOwner", Image.class).getResultList();		
		return allImages;
	}
	
	public Image getImage(Integer idImage){
		Image image = null;
		if(idImage != null){
			image = em.find(Image.class, idImage);
		}
		return image;
	}
	public Image getImage(String title, int idUser){
		Image image = null;
		List<Image> queryResult = em.createNamedQuery("Image.findByNameAndOwner", Image.class)
				.setParameter("title", title)
				.setParameter("owner", idUser)
				.getResultList();
		if (!queryResult.isEmpty()){
			image = queryResult.get(0);
		}
		return 	image;
	}
	
	//write image to disk
	public void storeImage(InputStream fileInputStream, String filePath) throws IOException{
        int read = 0;
        byte[] bytes = new byte[1024];
 
        OutputStream out = new FileOutputStream(new File(filePath));
        while ((read = fileInputStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }	    	        
        out.flush();
        out.close();
	}
	
	//save an image into the db
	public Integer saveImage(String path, String title, User owner){
		Integer idImage = null; 	
		try{
			Image image = new Image(path, title, owner);
			em.persist(image);
	        em.flush();
	        idImage = Integer.valueOf(getImage(title, owner.getIdUser()).getIdImage());
		} catch (Exception e){
			e.printStackTrace();
		}
		return idImage;
	}
	

}
