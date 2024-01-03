/**
 * This class acts as a controller for Customer user operations on car rental portal
 * @author  Manish Sai Krishna Rudrakoti Chandrani
 */

package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.DAOModel;

public class CustomerDashboardController implements Initializable {

	DAOModel dao;
	private MainHomePageController mainHomePage;

	// Initializing DAOModel object in constructor
	public CustomerDashboardController() {
		dao = new DAOModel();
	}

	/*
	 * This code defines all the FXML elements of the Rent Cars section of the
	 * customer dashboard.
	 */

	@FXML
	private AnchorPane availableCars_grid;

	@FXML
	private Button rentCarsBtn;

	@FXML
	private TableColumn<CarData, String> rentCars_CarIdCol;

	@FXML
	private TextField rentCars_amount;

	@FXML
	private Label rentCars_balance;

	@FXML
	private ComboBox<String> rentCars_brand;

	@FXML
	private TableColumn<CarData, String> rentCars_brandCol;

	@FXML
	private ComboBox<String> rentCars_carId;

	@FXML
	private TextField rentCars_fName;

	@FXML
	private TextField rentCars_lName;

	@FXML
	private ComboBox<String> rentCars_model;

	@FXML
	private TableColumn<CarData, String> rentCars_modelCol;

	@FXML
	private TableColumn<CarData, Double> rentCars_priceCol;

	@FXML
	private Button rentCars_rentBtn;

	@FXML
	private DatePicker rentCars_rentDate;

	@FXML
	private DatePicker rentCars_returnDate;

	@FXML
	private TableColumn<?, ?> rentCars_statusCol;

	@FXML
	private TableView<CarData> rentCars_tableView;

	@FXML
	private Label rentCars_total;

	@FXML
	private Label rentCars_userLabel;

	@FXML
	private Button rentCars_logOutBtn;

	@FXML
	private TableView<CustomerData> bookingHistoryTable;

	@FXML
	private Button deleteBookingBtn;

	@FXML
	private TableColumn<CustomerData, String> manageBooking_brandCol;

	@FXML
	private TableColumn<CustomerData, String> manageBooking_carIdCol;

	@FXML
	private TableColumn<CustomerData, String> manageBooking_customerIdCol;

	@FXML
	private TableColumn<CustomerData, String> manageBooking_customerNameCol;

	@FXML
	private TableColumn<CustomerData, String> manageBooking_modelCol;

	@FXML
	private TableColumn<CustomerData, Double> manageBooking_priceCol;

	@FXML
	private TableColumn<CustomerData, String> manageBooking_rentDateCol;

	@FXML
	private TableColumn<CustomerData, String> manageBooking_returnDateCol;

	// This method clears all the fields on the rent cars form
	public void rentCarsClearData() {
		rentCars_fName.setText("");
		rentCars_lName.setText("");
		rentCars_total.setText("$0.0");
		rentCars_carId.getSelectionModel().clearSelection();
		rentCars_brand.getSelectionModel().clearSelection();
		rentCars_model.getSelectionModel().clearSelection();
		rentCars_rentDate.setValue(null);
		rentCars_returnDate.setValue(null);
	}

	// This method retrieves the available car data and displays it in a table view
	public void rentCarsShowTableData() {
		if (rentCars_CarIdCol != null) {
			try {
				ObservableList<CarData> carList = FXCollections.observableArrayList();
				ResultSet result = dao.getAvailableCarDetails("status", null, null);
				while (result.next()) {
					CarData car_data = new CarData(result.getString("car_id"), result.getString("brand"),
							result.getString("model"), result.getDouble("price"), result.getString("status"),
							result.getDate("date"));
					carList.add(car_data);
				}
				rentCars_CarIdCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
				rentCars_brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
				rentCars_modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
				rentCars_priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
				rentCars_tableView.setItems(carList);
				rentCars_rentBtn.setDisable(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// Updates the table view with the user's booking history data.
	public void rentCarsShowUserBookingData() {
		if (manageBooking_customerIdCol != null && bookingHistoryTable != null) {
			try {
				/*
				 * Create an observable list of CustomerData objects to hold the data retrieved
				 * from the database
				 */
				ObservableList<CustomerData> customerList = FXCollections.observableArrayList();
				// Retrieve the user's booking details from the database
				ResultSet result = dao.getUserBookingDetails(false);
				while (result.next()) {
					// Create a new CustomerData object for each row in the ResultSet
					CustomerData customer_data = new CustomerData(result.getInt("customer_id"),
							result.getString("car_id"), result.getString("brand"), result.getString("model"),
							result.getDouble("total"), result.getDate("date_returned"), result.getDate("date_rented"),
							result.getString("booking_status"));

					// Add the CustomerData object to the observable list
					customerList.add(customer_data);
				}

				// Update the table view with the data from the observable list
				manageBooking_customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
				manageBooking_carIdCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
				manageBooking_brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
				manageBooking_modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
				manageBooking_priceCol.setCellValueFactory(new PropertyValueFactory<>("total"));
				manageBooking_rentDateCol.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
				manageBooking_returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
				bookingHistoryTable.setItems(customerList);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * This method is called when a user selects a booked car from the booking
	 * history table
	 */
	public void bookedCarSelect() {
		// Get the selected customer data from the booking history table
		CustomerData customer_data = bookingHistoryTable.getSelectionModel().getSelectedItem();
		// Get the index of the selected row in the table
		int num = bookingHistoryTable.getSelectionModel().getSelectedIndex();
		// If the selected row is not valid, return
		if ((num - 1) < -1) {
			return;
		}
		/*
		 * Update the booking status of the selected customer to cancelled in the
		 * database
		 */
		boolean isCancelled = dao.updateUserBookingDetails(customer_data.getCustomerId());
		/*
		 * If the booking status is successfully updated, update the car status to
		 * available and refresh the booking history table
		 */
		if (isCancelled) {
			dao.updateCarStatus(String.valueOf(customer_data.getCarId()), "Available");
			rentCarsShowUserBookingData();
		}

	}

	public void displayCustomerUsername() {
		// Get the logged in user name and capitalize the first letter of the user name
		String user = LoginSignUpController.userName;
		if (user != null) {
			rentCars_userLabel.setText(user.substring(0, 1).toUpperCase() + user.substring(1));
		}
	}

	public void setAvailableCarIds() {
		if (rentCars_carId != null) {
			try {
				// Create an observable list of available car IDs
				ObservableList listData = FXCollections.observableArrayList();
				ResultSet availableCars = dao.getAvailableCarDetails("status", null, null);
				while (availableCars.next()) {
					// Add each available car ID to the list
					listData.add(availableCars.getString("car_id"));
				}

				// Set the items of car ID drop down to the observable list created above
				rentCars_carId.setItems(listData);
				// Call the method to set available car brands
				setAvailableCarBrands();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// Sets the available car brands based on the selected car ID.
	public void setAvailableCarBrands() {
		try {
			String selectedCarId = rentCars_carId.getSelectionModel().getSelectedItem();
			ResultSet availableCars = dao.getAvailableCarDetails("carId", selectedCarId, null);
			ObservableList listData = FXCollections.observableArrayList();

			while (availableCars.next()) {
				listData.add(availableCars.getString("brand"));
			}

			rentCars_brand.setItems(listData);

			setAvailableCarModels();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Sets the available car models based on the selected car ID and brand.

	public void setAvailableCarModels() {
		try {
			String selectedCarId = rentCars_carId.getSelectionModel().getSelectedItem();
			String selectedCarBrand = rentCars_brand.getSelectionModel().getSelectedItem();
			ResultSet availableCars = dao.getAvailableCarDetails("brand", selectedCarId, selectedCarBrand);
			ObservableList listData = FXCollections.observableArrayList();

			while (availableCars.next()) {
				listData.add(availableCars.getString("model"));
			}

			rentCars_model.setItems(listData);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * This is a method to validate the rent and return date It checks if all
	 * required fields are filled out, and shows an error message if not If the
	 * dates are valid, it sets the dateDiff variable and enables the rent button
	 */

	private int dateDiff;

	public void validateRentAndReturnDate() {
		Alert alert;
		if (rentCars_fName.getText().isEmpty() || rentCars_lName.getText().isEmpty()
				|| rentCars_carId.getSelectionModel().getSelectedItem() == null
				|| rentCars_brand.getSelectionModel().getSelectedItem() == null
				|| rentCars_model.getSelectionModel().getSelectedItem() == null) {
			// Show an error message if any of the required fields are not filled out
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Please fill the incomplete fields !!!");
			alert.showAndWait();

			// Reset the date fields and disable the rent button
			rentCars_rentDate.setValue(null);
			rentCars_returnDate.setValue(null);
			rentCars_rentBtn.setDisable(true);
		} else {
			if (rentCars_returnDate.getValue().isAfter(rentCars_rentDate.getValue())) {
				// calculate the date difference between Rent Date and Return Date
				dateDiff = rentCars_returnDate.getValue().compareTo(rentCars_rentDate.getValue());
				// enable the rent button
				rentCars_rentBtn.setDisable(false);
			} else {
				// Show an error message if return date is before rent date
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please select a date after rented date !!!");
				alert.showAndWait();
				// If same day is selected by user, add a day to return date
				rentCars_returnDate.setValue(rentCars_rentDate.getValue().plusDays(1));
			}
		}
	}

	/**
	 * This method calculates the final price of the car rental based on the
	 * selected model and date range. It calls the validateRentAndReturnDate()
	 * method to ensure the dates are valid.
	 */
	private double finalPrice;

	public void calculateFinalRent() {
		double amountPerDay = 0;
		validateRentAndReturnDate();
		String selectedCarModel = rentCars_model.getSelectionModel().getSelectedItem();
		try {
			ResultSet availableCarPrice = dao.getAvailableCarDetails("price", null, selectedCarModel);
			if (availableCarPrice.next()) {
				amountPerDay = availableCarPrice.getDouble("price");
			}

			// Split the rental and return dates into separate year-month-day components

			LocalDate rentalDate = rentCars_rentDate.getValue();
			LocalDate returnDate = rentCars_returnDate.getValue();
			if (rentalDate != null && returnDate != null) {
				int rentalYear = rentalDate.getYear();
				int rentalMonth = rentalDate.getMonthValue();
				int rentalDay = rentalDate.getDayOfMonth();
				int returnYear = returnDate.getYear();
				int returnMonth = returnDate.getMonthValue();
				int returnDay = returnDate.getDayOfMonth();

				// Calculate the total number of days rented
				int daysRented = 0;
				while (rentalYear < returnYear || rentalMonth < returnMonth || rentalDay < returnDay) {
					daysRented++;
					rentalDay++;
					if (rentalDay > rentalDate.lengthOfMonth()) {
						rentalDay = 1;
						rentalMonth++;
						if (rentalMonth > 12) {
							rentalMonth = 1;
							rentalYear++;
						}
					}
				}

				// Calculate the final price by multiplying the amount per day with the number
				// of days rented
				finalPrice = amountPerDay * daysRented;

				// Display the final price to the customer
				rentCars_total.setText("$" + String.valueOf(finalPrice));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method for renting a car
	public void rentACar() {

		try {
			Alert alert;
			if (rentCars_fName.getText().isEmpty() || rentCars_lName.getText().isEmpty()
					|| rentCars_carId.getSelectionModel().getSelectedItem() == null
					|| rentCars_brand.getSelectionModel().getSelectedItem() == null
					|| rentCars_model.getSelectionModel().getSelectedItem() == null || finalPrice == 0
					|| rentCars_total.getText().isEmpty()) {
				alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Please fill all the blank fields!!!");
				alert.showAndWait();
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText(null);
				alert.setContentText("Are you sure?");
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get().equals(ButtonType.OK)) {
					int customerId = dao.getPreviousCustomerId();
					boolean isInserted = dao.insertNewCustomer(customerId, rentCars_carId, rentCars_fName,
							rentCars_lName, rentCars_brand, rentCars_model, finalPrice, rentCars_rentDate,
							rentCars_returnDate);
					if (isInserted) {
						// updating the car status to not-available
						dao.updateCarStatus(rentCars_carId.getSelectionModel().getSelectedItem(), "Not Available");
						alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText(null);
						alert.setContentText("Successful!");
						alert.showAndWait();
						rentCarsShowTableData();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void goToRentCarsPage() {
		try {
			mainHomePage.showPage("/views/RentCarsPage.fxml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void goToBookingHistoryPage() {
		try {
			rentCarsShowUserBookingData();
			mainHomePage.showPage("/views/RentCarsHistory.fxml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void logout() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Message");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to logout?");
		Optional<ButtonType> option = alert.showAndWait();
		try {
			if (option.get().equals(ButtonType.OK)) {
				mainHomePage.showMainHomePageView();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		displayCustomerUsername();
		rentCarsShowTableData();
		setAvailableCarIds();
		rentCarsShowUserBookingData();
		rentCarsClearData();
	}

}
