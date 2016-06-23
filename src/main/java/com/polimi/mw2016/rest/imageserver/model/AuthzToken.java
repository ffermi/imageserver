package com.polimi.mw2016.rest.imageserver.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the authztoken database table.
 * 
 */
@Entity

@NamedQueries({
	@NamedQuery(name="Authztoken.findAll", query="SELECT a FROM AuthzToken a"),
	@NamedQuery(name = "Authztoken.findByAuthzToken", query = "SELECT a FROM AuthzToken a WHERE a.authToken = :authzToken")
})
public class AuthzToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String authToken;

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

	public AuthzToken() {
	}
	public AuthzToken(String authToken, Date expiration, Date start, Client client, User user) {
		this.authToken = authToken;
		this.expiration = expiration;
		this.start = start;
		this.client = client;
		this.user = user;
	}

	public String getAuthToken() {
		return this.authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
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