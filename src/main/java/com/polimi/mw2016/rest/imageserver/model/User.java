package com.polimi.mw2016.rest.imageserver.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="User.findAll", query="SELECT u FROM User u"),
	@NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUser;

	private String firstname;

	private String lastname;

	@XmlTransient
	private String password;

	@XmlTransient
	private String usergroup;

	private String username;

	//bi-directional many-to-one association to Accesstoken
	@OneToMany(mappedBy="user")
	@XmlTransient
	private List<AccessToken> accesstokens;

	//bi-directional many-to-one association to Authztoken
	@OneToMany(mappedBy="user")
	@XmlTransient
	private List<AuthzToken> authztokens;

	//bi-directional many-to-one association to Image
	@OneToMany(mappedBy="user")
	private List<Image> images;

	public User() {
	}
	public User(String username, String password, String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
		this.username = username;
		this.usergroup = "REGISTRATO";
	}
	
	public int getIdUser() {
		return this.idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsergroup() {
		return this.usergroup;
	}

	public void setUsergroup(String usergroup) {
		this.usergroup = usergroup;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<AccessToken> getAccesstokens() {
		return this.accesstokens;
	}

	public void setAccesstokens(List<AccessToken> accesstokens) {
		this.accesstokens = accesstokens;
	}

	public AccessToken addAccesstoken(AccessToken accesstoken) {
		getAccesstokens().add(accesstoken);
		accesstoken.setUser(this);

		return accesstoken;
	}

	public AccessToken removeAccesstoken(AccessToken accesstoken) {
		getAccesstokens().remove(accesstoken);
		accesstoken.setUser(null);

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
		authztoken.setUser(this);

		return authztoken;
	}

	public AuthzToken removeAuthztoken(AuthzToken authztoken) {
		getAuthztokens().remove(authztoken);
		authztoken.setUser(null);

		return authztoken;
	}

	public List<Image> getImages() {
		return this.images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Image addImage(Image image) {
		getImages().add(image);
		image.setUser(this);

		return image;
	}

	public Image removeImage(Image image) {
		getImages().remove(image);
		image.setUser(null);

		return image;
	}

}