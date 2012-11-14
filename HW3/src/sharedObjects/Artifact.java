//Artifact.java
/******************************************************
 * This class represents an museum/art/galley artifact 
 ******************************************************/

package sharedObjects;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Artifact implements Serializable {
	private String id;
	private String name;
	private String creator;
	private String origin;
	private String genre;
	private int price;
	
	public Artifact(String id, String name, String creator, String origin, String genre, int price) {
		setId(id);
		setName(name);
		setCreator(creator);
		setOrigin(origin);
		setGenre(genre);
		setPrice(price);
	}
	
	/***********************************
	 * Getters and Setters and toString
	 ***********************************/
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String toString() {
		return "#" + id +" Name: "+ name + " Creator: "+creator + " Made in: " + origin + " Genre: "+ genre + " Value: " + price;
	}
}
