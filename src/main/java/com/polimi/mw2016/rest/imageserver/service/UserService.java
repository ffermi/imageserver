package com.polimi.mw2016.rest.imageserver.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.polimi.mw2016.rest.imageserver.model.User;

@Stateless
public class UserService {
	
	@PersistenceContext(unitName="imageserver_pu")	
	private EntityManager em;

	public UserService(){
	}
	
	public List<User> getAllUsers(){
		List<User> allUsers = new ArrayList<>();
		allUsers = (List<User>)em.createNamedQuery("User.findAll", User.class).getResultList();		
		return allUsers;
	}
	
	public User getUser(Integer idUser){
		User user = null;
		if (idUser != null){
			user = em.find(User.class, idUser); 			
		}
		return user;
	}
	public User getUser(String username){
		User user = null;
		List<User> queryResult = em.createNamedQuery("User.findByUsername", User.class)
				.setParameter("username", username)
				.getResultList();
		if (!queryResult.isEmpty()){
			user = queryResult.get(0);
		}
		return user;
	}
	
	public Integer addUser(String username, String password, String firstname, String lastname){
		Integer idUser = null;
		User newUser = null;
		if (getUser(username) == null){
			newUser = new User(username,encryptPassword(password),firstname,lastname);
			em.persist(newUser);
	        em.flush();
			idUser = Integer.valueOf(newUser.getIdUser());
		}
		return idUser;
	}
	
	/*	
	public void deleteUser(int idUser){
		User user = getUser(idUser);
		if(user != null){
			em.remove(user);
			em.flush();
		}
	}
	*/

	public static String encryptPassword(String password) {
		String encPass = null;        
	    try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(password.getBytes("UTF-8"));
	        BigInteger bigInt = new BigInteger(1, hash);
	        encPass = bigInt.toString(16);
	    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
	        ex.printStackTrace();
	    }
	    return encPass;       
	}

}