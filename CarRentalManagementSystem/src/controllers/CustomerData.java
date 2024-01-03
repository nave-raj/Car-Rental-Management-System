/**
This class represents a CustomerData object which contains details about a customer
@author Manish Sai Krishna Rudrakoti Chandrani
*/
package controllers;

import java.sql.Date;

public class CustomerData {

	// variables to store customer data
	private Integer customerId;
	private String carId;
	private String brand;
	private String model;
	private Double total;
	private Date rentDate;
	private Date returnDate;
	private String status;

	// constructor to initialize customer data variables
	public CustomerData(Integer customerId, String carId, String brand, String model, Double total, Date rentDate,
			Date returnDate, String status) {
		this.customerId = customerId;
		this.carId = carId;
		this.brand = brand;
		this.model = model;
		this.total = total;
		this.rentDate = rentDate;
		this.returnDate = returnDate;
		if (status != null) {
			this.status = status;
		} else {
			this.status = "Active";
		}
	}

	// methods to retrieve customer data variables
	public Integer getCustomerId() {
		return customerId;
	}

	public String getCarId() {
		return carId;
	}

	public String getBrand() {
		return brand;
	}

	public String getModel() {
		return model;
	}

	public Double getTotal() {
		return total;
	}

	public Date getRentDate() {
		return rentDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public String getStatus() {
		return status;
	}
}
