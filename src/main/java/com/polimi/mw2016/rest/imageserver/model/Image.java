package com.polimi.mw2016.rest.imageserver.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the image database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Image.findAll", query="SELECT i FROM Image i"),
	@NamedQuery(name="Image.findAllOrderByOwner", query="SELECT i FROM Image i ORDER BY i.user.idUser"),
	@NamedQuery(name="Image.findByNameAndOwner", query="SELECT i FROM Image i WHERE i.title = :title AND i.user.idUser = :owner")
})
@XmlRootElement
public class Image implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idImage;

	private String path;

	private String title;

	//bi-directional many-to-one association to User
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="owner")
	private User user;

	public Image() {
	}
	public Image(String path, String title, User user) {
		super();
		this.path = path;
		this.title = title;
		this.user = user;
	}

	public int getIdImage() {
		return this.idImage;
	}

	public void setIdImage(int idImage) {
		this.idImage = idImage;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}