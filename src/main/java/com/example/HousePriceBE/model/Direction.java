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
public class Direction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String direction;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
}
