package com.polimi.mw2016.rest.imageserver.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the client database table.
 * 
 */
@Entity
@NamedQuery(name="Client.findAll", query="SELECT c FROM Client c")
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idClient;

	private String secret;

	//bi-directional many-to-one association to Accesstoken
	@OneToMany(mappedBy="client")
	private List<AccessToken> accesstokens;

	//bi-directional many-to-one association to Authztoken
	@OneToMany(mappedBy="client")
	private List<AuthzToken> authztokens;

	public Client() {
	}
	public Client(int idClient, String secret) {
		this.idClient = idClient;
		this.secret = secret;
	}


	public int getIdClient() {
		return this.idClient;
	}

	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}

	public String getSecret() {
		return this.secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public List<AccessToken> getAccesstokens() {
		return this.accesstokens;
	}

	public void setAccesstokens(List<AccessToken> accesstokens) {
		this.accesstokens = accesstokens;
	}

	public AccessToken addAccesstoken(AccessToken accesstoken) {
		getAccesstokens().add(accesstoken);
		accesstoken.setClient(this);

		return accesstoken;
	}

	public AccessToken removeAccesstoken(AccessToken accesstoken) {
		getAccesstokens().remove(accesstoken);
		accesstoken.setClient(null);

		return accesstoken;
	}

	public List<AuthzToken> getAuthztokens() {
		return this.authztokens;
	}

	public void setAuthztokens(List<AuthzToken> authztokens) {
		this.authztokens = authztokens;
	}

	public AuthzToken addAuthztoken(AuthzToken authztoken) {
		getAuthztokens().add(authztoken);
		authztoken.setClient(this);

		return authztoken;
	}

	public AuthzToken removeAuthztoken(AuthzToken authztoken) {
		getAuthztokens().remove(authztoken);
		authztoken.setClient(null);

		return authztoken;
	}

}