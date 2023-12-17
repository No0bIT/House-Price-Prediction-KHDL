package com.example.HousePriceBE.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HouseTraining {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String location;
    private float size;
    private String houseDirection;
    private String balconyDirection;
    private int bedrooms;
    private int bathrooms;
    private String investor;
    private float price;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public String getHouseDirection() {
		return houseDirection;
	}
	public void setHouseDirection(String houseDirection) {
		this.houseDirection = houseDirection;
	}
	public String getBalconyDirection() {
		return balconyDirection;
	}
	public void setBalconyDirection(String balconyDirection) {
		this.balconyDirection = balconyDirection;
	}
	public int getBedrooms() {
		return bedrooms;
	}
	public void setBedrooms(int bedrooms) {
		this.bedrooms = bedrooms;
	}
	public int getBathrooms() {
		return bathrooms;
	}
	public void setBathrooms(int bathrooms) {
		this.bathrooms = bathrooms;
	}
	public String getInvestor() {
		return investor;
	}
	public void setInvestor(String investor) {
		this.investor = investor;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}

    
   

}
