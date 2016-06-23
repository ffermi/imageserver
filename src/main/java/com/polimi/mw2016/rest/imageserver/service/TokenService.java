package com.polimi.mw2016.rest.imageserver.service;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.polimi.mw2016.rest.imageserver.model.AccessToken;
import com.polimi.mw2016.rest.imageserver.model.AuthzToken;
import com.polimi.mw2016.rest.imageserver.model.Client;

@Stateless
public class TokenService {

	@PersistenceContext(unitName="imageserver_pu")	
	private EntityManager em;
	
	@Inject UserService us;

	public TokenService() {
	}
	
	public void addAuthzToken(String authToken, int seconds, String idClient, String username) throws Exception {
		Date start = new Date();
		Date expiration = calcExpiration(start, seconds);
		AuthzToken authzToken = new AuthzToken(authToken, expiration, start, em.find(Client.class, idClient), us.getUser(username));
		em.persist(authzToken);
	}
	
	public AuthzToken getAuthzToken(String param) throws Exception {
		return em.find(AuthzToken.class, param);
	}
	
	public void addAccessToken(String accessToken, int seconds, AuthzToken authzToken) {
		Date start = new Date();
		Date expiration = calcExpiration(start, seconds);
		AccessToken accToken = new AccessToken(accessToken, expiration, start, authzToken.getClient(), authzToken.getUser());
		authzToken.setExpiration(start);
		em.merge(authzToken);
		em.persist(accToken);		
	}
	
	private Date calcExpiration(Date dateStart, int seconds) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(dateStart); // sets calendar time/date
        cal.add(Calendar.SECOND, seconds); // adds seconds
        return cal.getTime(); // returns new date object, in the future
	}	
	 
}
