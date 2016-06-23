package com.polimi.mw2016.rest.imageserver.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the accesstoken database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Accesstoken.findAll", query="SELECT a FROM AccessToken a"),
	@NamedQuery(name = "Accesstoken.findByAccessToken", query = "SELECT a FROM AccessToken a WHERE a.accessToken = :accessToken")
})
public class AccessToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String accessToken;

	@Temporal(TemporalType.TIMESTAMP)
	private Date expiration;

	@Temporal(TemporalType.TIMESTAMP)
	private Date start;

	//bi-directional many-to-one association to Client
	@ManyToOne
	@JoinColumn(name="idClient")
	private Client client;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="idUser")
	private User user;

	public AccessToken() {
	}
	public AccessToken(String accessToken, Date expiration, Date start, Client client, User user) {
		this.accessToken = accessToken;
		this.expiration = expiration;
		this.start = start;
		this.client = client;
		this.user = user;
	}


	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getExpiration() {
		return this.expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public Date getStart() {
		return this.start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public boolean isExpired() {
		if(this.expiration.before(new Date())){
			return true;
		}
		return false;
	}

}