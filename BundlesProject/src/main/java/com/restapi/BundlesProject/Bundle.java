package com.restapi.BundlesProject;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "bundles")
@DynamicUpdate
public class Bundle {
	
	@Id
	@Column(name="code") @JsonProperty("code")
	private long code;
	
	@Column(name="name", nullable=false) @JsonProperty("name")
	private String name;
	
	@Column(name="price", nullable=false) @JsonProperty("price")
	private float price;
	
	@Column(name="activated", nullable=false) @JsonProperty("activated")
	private boolean activated;
	
	@Column(name="expiration") @JsonProperty("expiration")
	private Timestamp expiration;
	
	@Column(name="availability", nullable=false) @JsonProperty("availability")
	private Timestamp  availability;
	
	//Constructor
	public Bundle() {}
	public Bundle(long code, String name, float price, boolean activated, Timestamp expiration, Timestamp  availability) {
		this.name = name;
		this.price = price;
		this.code = code;
		this.activated = activated;
		this.expiration=expiration;
		this.availability=availability;
	}
	
	
	//Setters & Getters	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	//Price
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	//Code
	public long getCode() {
		return code;
	}
	public void setCode(long code) {
		this.code = code;
	}
	//Activated
	public boolean getActivated() {
		return activated;
	}
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	//Expiration
	public Timestamp  getExpiration() {
		return expiration;
	}
	public void setExpiration(Timestamp expiration) {
		this.expiration = expiration;
	}
	//Availability
	public Timestamp getAvailability() {
		return availability;
	}
	public void setAvailability(Timestamp availability) {
		this.availability = availability;
	}
	
	@Override
	public String toString() {
	  return "Bundle{" + "code=" + this.code + ", name='" + this.name + '\'' + ", activated='" + this.activated + '\'' + '}';
	}
	
}