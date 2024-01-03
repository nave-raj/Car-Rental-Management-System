/**
This class represents a CarData object which contains details about a car, such as car ID, brand, model, price, status, and date.
@author Naveena Kandasamy Rajkumar
*/
package controllers;

import java.sql.Date;

public class CarData {

	// variables to store car data
	private String carId;
	private String brand;
	private String model;
	private Double price;
	private String status;
	private Date date;

	//Constructor for cardata class
	public CarData(String carId, String brand, String model, Double price, String status, Date date) {
		this.carId = carId;
		this.brand = brand;
		this.model = model;
		this.price = price;
		this.status = status;
		this.date = date;
	}


	//getters for declared variables
	public String getCarId() {
		return carId;
	}

	public String getBrand() {
		return brand;
	}

	public String getModel() {
		return model;
	}

	public Double getPrice() {
		return price;
	}

	public String getStatus() {
		return status;
	}

	public Date getDate() {
		return date;
	}

}
