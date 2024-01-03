/**
 * This class acts as Data Access layer for the car rental portal
 * @authors  Naveena Kandasamy Rajkumar, Manish Sai Krishna Rudrakoti Chandrani
 */

package models;

import java.util.Date;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import controllers.CarData;
import controllers.LoginSignUpController;
import controllers.MainHomePageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class DAOModel {

	// Declaring and initializing DB Objects
	DBConnect conn = null;
	PreparedStatement pst = null;
	ResultSet result = null;
	LoginSignUpController loginCtrl;

	// constructor for DAOModel class which creates db Object instance
	public DAOModel() {
		conn = new DBConnect();
	}

	// This method is used to sign up a new user into the system.
	public boolean signUpUser(TextField email, TextField phonenumber, TextField username, PasswordField password,
			PasswordField confirmPassword) {
		// Prepare the SQL statement for inserting a new user into the database.
		String sql = "INSERT INTO car_rental_users (username, password, category, email, phone_number) "
				+ "VALUES(?,?,?,?,?)";
		try {
			Alert alert;
			// Prepare the PreparedStatement object for executing the SQL statement.
			pst = conn.connect().prepareStatement(sql);
			// Check if any of the fields are empty.
			if (email.getText().isEmpty() || phonenumber.getText().isEmpty() || username.getText().isEmpty()
					|| password.getText().isEmpty() || confirmPassword.getText().isEmpty()) {
				alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Please fill out the blank fields");
				alert.showAndWait();
				return false;
			} else if (!password.getText().equals(confirmPassword.getText())) {
				// Check if the passwords match.
				alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Passwords doesn't match! Please re-enter passwords!");
				alert.showAndWait();
				return false;
			} else if (!email.getText().contains(".com")) {
				// Check for valid email domain
				alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Please enter a valid domain!");
				alert.showAndWait();
				return false;
			} else {
				/*
				 * If all the fields are filled and the passwords match, create a new user with
				 * the given details.
				 */
				alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("Signed Up Successfully!!!");
				alert.showAndWait();
				// Set the values of the PreparedStatement object to the corresponding fields.
				pst.setString(1, username.getText());
				String hashedPassword = hashPassword(password.getText());
				pst.setString(2, hashedPassword);
				pst.setString(3, "customer");
				pst.setString(4, email.getText());
				pst.setString(5, phonenumber.getText());
				// Execute the SQL statement to insert the new user into the database.
				pst.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	// This method is used to log in a user into the system.

	public boolean loginUser(TextField username, PasswordField password, String category) {
		// Prepare the SQL statement for selecting a user from the database.
		String sql = "SELECT * FROM car_rental_users WHERE username = ? AND password = ? AND category = ?";
		try {
			pst = conn.connect().prepareStatement(sql);
			// Set the values of the PreparedStatement object to the corresponding fields.
			pst.setString(1, username.getText());
			String hashedPassword = hashPassword(password.getText());
			pst.setString(2, hashedPassword);
			pst.setString(3, category);
			result = pst.executeQuery();
			Alert alert;
			if (username.getText().isEmpty() || password.getText().isEmpty()) {
				// Check if any of the fields are empty.
				alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Please fill out the blank fields");
				alert.showAndWait();
			} else if (result.next()) {
				/*
				 * If the credentials are valid, set the static userName variable to the user's
				 * username, display a success message, and return true.
				 */
				LoginSignUpController.userName = username.getText();
				alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("Login Success!!!");
				alert.showAndWait();
				return true;
			} else {
				// If the credentials are invalid, display an error message and return false.
				alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Invalid Credentials!!!");
				alert.showAndWait();
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* EXTRA - CREDIT
	 * This function takes a string password as input and returns a hashed password
	 * in the form of a string.
	 */
	public String hashPassword(String password) {
		try {
			// Creates a MessageDigest object using the SHA-256 algorithm for hashing.
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			// Gets the byte array representation of the input password in UTF-8 encoding.
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

			// Encodes the hashed byte array to a Base64 string.
			String hashedPassword = Base64.getEncoder().encodeToString(hash);

			// Returns the hashed password string.
			return hashedPassword;
		} catch (NoSuchAlgorithmException ex) {
			// Prints the stack trace if the specified algorithm is not found.
			ex.printStackTrace();

			// Returns null in case of any error.
			return null;
		}
	}

	/*
	 * This function retrieves all the car details from a database table named
	 * "car_rental_cardetails" and returns the details as an observable list of
	 * CarData objects.
	 */
	public ObservableList<CarData> getAllCarDetails() {

		// Creates an empty observable list to hold the retrieved car data.
		ObservableList<CarData> listData = FXCollections.observableArrayList();

		// SQL query to retrieve all car data from the database.
		String sql = "SELECT * FROM car_rental_cardetails";

		try {
			// Prepares the SQL statement for execution and executes the query.
			pst = conn.connect().prepareStatement(sql);
			result = pst.executeQuery();

			// CarData object to hold the data for each car.
			CarData car_data;

			// Iterates through the query result set and retrieves data for each car.
			while (result.next()) {
				car_data = new CarData(result.getString("car_id"), result.getString("brand"), result.getString("model"),
						result.getDouble("price"), result.getString("status"), result.getDate("date"));

				// Adds the retrieved car data to the observable list.
				listData.add(car_data);
			}

		} catch (Exception e) {
			// Prints the stack trace in case of any error.
			e.printStackTrace();
		}

		// Returns the observable list of car data.
		return listData;
	}

	/*
	 * This function retrieves car details based on a query parameter and returns
	 * the results as a ResultSet object.
	 */
	public ResultSet getAvailableCarDetails(String query, String field1, String field2) {
		String sql = null;
		// Constructs the SQL query based on the value of the query parameter.
		if (query == "status") {
			// Retrieves all cars with status 'Available'.
			sql = "SELECT * FROM car_rental_cardetails WHERE status = 'Available'";
		} else if (query == "carId") {
			// Retrieves car details for a specific car ID.
			sql = "SELECT * FROM car_rental_cardetails WHERE car_id = '" + field1 + "'";
		} else if (query == "brand") {
			// Retrieves car details for a specific car brand and ID.
			sql = "SELECT * FROM car_rental_cardetails WHERE car_id = '" + field1 + "'AND brand = '" + field2 + "'";
		} else if (query == "price") {
			// Retrieves the price for a specific car model.
			sql = "SELECT price FROM car_rental_cardetails WHERE model ='" + field2 + "'";
		}

		try {
			// Prepares the SQL statement for execution and executes the query.
			pst = conn.connect().prepareStatement(sql);
			result = pst.executeQuery();
		} catch (Exception e) {
			// Prints the stack trace in case of any error.
			e.printStackTrace();
		}

		// Returns the result set containing the car details.
		return result;
	}

	/*
	 * This function retrieves the ID of the last customer added to the database and
	 * returns the next available ID for a new customer.
	 */
	public int getPreviousCustomerId() {
		String sql = "SELECT customer_id FROM car_rental_customers ORDER BY customer_id DESC LIMIT 1";
		int lastCustomerId = 0;
		try {
			// Prepares the SQL statement for execution and executes the query.
			pst = conn.connect().prepareStatement(sql);
			result = pst.executeQuery();

			/*
			 * Retrieves the ID of the last customer added to the database and adds 1 to
			 * obtain the next available ID.
			 */
			while (result.next()) {
				lastCustomerId = result.getInt("customer_id") + 1;
			}
		} catch (Exception e) {
			// Prints the stack trace in case of any error.
			e.printStackTrace();
		}

		// Returns the next available customer ID.
		return lastCustomerId;
	}

	/*
	 * This function adds a new car to the database, provided that all the required
	 * fields are filled.
	 */
	public boolean addCarDetails(TextField carId, TextField brand, TextField model, ComboBox<String> status,
			TextField price) {
		// SQL statement to insert car details into the database.
		String sql = "INSERT INTO car_rental_cardetails (car_id, brand, model, price, status, date) VALUES(?,?,?,?,?,?)";

		try {
			Alert alert;

			// Checks if all required fields are filled before executing the SQL statement.
			if (carId.getText().isEmpty() || brand.getText().isEmpty() || model.getText().isEmpty()
					|| status.getSelectionModel().getSelectedItem() == null || price.getText().isEmpty()) {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all blank fields");
				alert.showAndWait();
				return false;
			} else {
				/*
				 * Prepares the SQL statement for execution and sets the values of the
				 * parameters to be inserted.
				 */
				pst = conn.connect().prepareStatement(sql);
				pst.setString(1, carId.getText());
				pst.setString(2, brand.getText());
				pst.setString(3, model.getText());
				pst.setString(4, price.getText());
				pst.setString(5, (String) status.getSelectionModel().getSelectedItem());
				Date date = new Date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				pst.setString(6, String.valueOf(sqlDate));
				pst.executeUpdate();

				/*
				 * Displays an information message to confirm that the car details have been
				 * successfully added.
				 */
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Message");
				alert.setHeaderText(null);
				alert.setContentText("Successfully Added!");
				alert.showAndWait();
			}
			return true;
		} catch (Exception e) {
			// Prints the stack trace in case of any error.
			e.printStackTrace();
		}

		/*
		 * Returns true in case the car details were successfully added, false
		 * otherwise.
		 */
		return true;
	}

	/*
	 * The function returns a boolean value indicating whether the update operation
	 * was successful or not
	 */
	public boolean updateCarDetails(TextField carId, TextField brand, TextField model, ComboBox<String> status,
			TextField price) {

		// the SQL query to update the car details
		String sql = "UPDATE car_rental_cardetails SET brand = ? , model = ?, status = ?,price = ? WHERE car_id = ?";

		try {
			Alert alert;
			if (carId.getText().isEmpty() || brand.getText().isEmpty() || model.getText().isEmpty()
					|| status.getSelectionModel().getSelectedItem() == null || price.getText().isEmpty()) {
				// if any of the fields are empty, display an error message
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all blank fields");
				alert.showAndWait();
				return false;
			} else {
				// otherwise, prepare the SQL statement and set its parameters
				pst = conn.connect().prepareStatement(sql);
				pst.setString(1, brand.getText());
				pst.setString(2, model.getText());
				pst.setString(3, (String) status.getSelectionModel().getSelectedItem());
				pst.setString(4, price.getText());
				pst.setString(5, carId.getText());

				// ask for confirmation before executing the update
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to UPDATE Car ID: " + carId.getText() + "?");
				Optional<ButtonType> option = alert.showAndWait();

				if (option.get().equals(ButtonType.OK)) {
					// if the user confirms, execute the update and display a success message
					pst.executeUpdate();
					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Message");
					alert.setHeaderText(null);
					alert.setContentText("Successfully Updated!");
					alert.showAndWait();
					return true;
				}
				return false;
			}
		} catch (Exception e) {
			// if an exception occurs, print the stack trace and return false
			e.printStackTrace();
		}
		return true;
	}

	// This method is used to delete a car's details from the car_rental_cardetails
	// table in the database
	public boolean deleteCarDetails(TextField carId, TextField brand, TextField model, ComboBox<String> status,
			TextField price) {

		// SQL query to delete a car's details from the car_rental_cardetails table
		String sql = "DELETE FROM car_rental_cardetails WHERE car_id = ?";

		try {
			Alert alert;
			// Check if all the fields are filled before proceeding with the deletion
			if (carId.getText().isEmpty() || brand.getText().isEmpty() || model.getText().isEmpty()
					|| status.getSelectionModel().getSelectedItem() == null || price.getText().isEmpty()) {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all blank fields");
				alert.showAndWait();
				return false;
			} else {
				// Prepare the SQL statement and set the car ID parameter
				pst = conn.connect().prepareStatement(sql);
				pst.setString(1, carId.getText());
				// Show a confirmation message before deleting the car's details
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to DELETE Car ID: " + carId.getText() + "?");
				Optional<ButtonType> option = alert.showAndWait();

				if (option.get().equals(ButtonType.OK)) {
					// Execute the SQL statement to delete the car's details
					pst.executeUpdate();
					// Show a success message after deleting the car's details
					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Message");
					alert.setHeaderText(null);
					alert.setContentText("Successfully Deleted!");
					alert.showAndWait();
					return true;
				}
				return false;
			}
		} catch (Exception e) {
			// Print the stack trace if an exception occurs while deleting the car's details
			e.printStackTrace();
		}
		// Return true by default
		return true;
	}

	/*
	 * This method is used to insert new customer data into the car_rental_customers
	 * table
	 */
	public boolean insertNewCustomer(int customerId, ComboBox<String> carId, TextField fName, TextField lName,
			ComboBox<String> brand, ComboBox<String> model, double finalPrice, DatePicker rentDate,
			DatePicker returnDate) {
		try {
			// SQL statement to insert new data into the table
			String sql = "INSERT INTO car_rental_customers "
					+ "(customer_id, first_name, last_name, username,car_id, brand"
					+ ", model, total, date_rented, date_returned, booking_status) " + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			// Set the prepared statement with the data to be inserted
			pst = conn.connect().prepareStatement(sql);
			pst.setString(1, String.valueOf(customerId));
			pst.setString(2, fName.getText());
			pst.setString(3, lName.getText());
			pst.setString(4, loginCtrl.userName);
			pst.setString(5, (String) carId.getSelectionModel().getSelectedItem());
			pst.setString(6, (String) brand.getSelectionModel().getSelectedItem());
			pst.setString(7, (String) model.getSelectionModel().getSelectedItem());
			pst.setString(8, String.valueOf(finalPrice));
			pst.setString(9, String.valueOf(rentDate.getValue()));
			pst.setString(10, String.valueOf(returnDate.getValue()));
			pst.setString(11, "active");

			// Execute the prepared statement to insert the data into the table
			pst.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// This method updates the status of a car in the car rental database.
	public void updateCarStatus(String carId, String status) {
		// Build the SQL query to update the car's status based on its ID.
		String sql = "UPDATE car_rental_cardetails SET status = '" + status + "' WHERE car_id = '" + carId + "'";

		try {
			// Prepare the SQL statement for execution.
			pst = conn.connect().prepareStatement(sql);
			// Execute the update statement to update the car's status.
			pst.executeUpdate();
		} catch (SQLException e) {
			// Handle any errors that occur during execution.
			e.printStackTrace();
		}
	}

	// This method retrieves booking details from the car rental database.
	public ResultSet getBookingDetails() {
		// Build the SQL query to retrieve the booking details.
		String sql = "SELECT date_rented, COUNT(customer_id) FROM car_rental_customers GROUP BY date_rented ORDER BY TIMESTAMP(date_rented) ASC LIMIT 5";
		try {
			// Prepare the SQL statement for execution.
			pst = conn.connect().prepareStatement(sql);
			// Execute the query to retrieve the booking details.
			result = pst.executeQuery();
		} catch (SQLException e) {
			// Handle any errors that occur during execution.
			e.printStackTrace();
		}
		// Return the ResultSet containing the booking details.
		return result;
	}

	/*
	 * This method retrieves booking details for all users or for the current user,
	 * depending on the isAllUsers parameter.
	 */
	public ResultSet getUserBookingDetails(boolean isAllUsers) {
		String sql;
		if (isAllUsers) {
			// Build the SQL query to retrieve booking details for all users.
			sql = "SELECT * FROM car_rental_customers";
		} else {
			// Build the SQL query to retrieve booking details only for the current user.
			sql = "SELECT * FROM car_rental_customers WHERE username = '" + loginCtrl.userName
					+ "' AND booking_status != 'cancelled'";
		}

		try {
			// Prepare the SQL statement for execution.
			pst = conn.connect().prepareStatement(sql);
			// Execute the query to retrieve the booking details.
			result = pst.executeQuery();
		} catch (SQLException e) {
			// Handle any errors that occur during execution.
			e.printStackTrace();
		}

		// Return the ResultSet containing the booking details.
		return result;
	}

	// This method cancels the booking for the specified customer ID.
	public boolean updateUserBookingDetails(int customer_id) {
		// Build the SQL query to cancel the booking for the specified customer ID.
		String sql = "UPDATE car_rental_customers SET booking_status = 'cancelled' WHERE customer_id = '" + customer_id
				+ "'";
		Alert alert;
		try {
			// Prepare the SQL statement for execution.
			pst = conn.connect().prepareStatement(sql);
			/*
			 * Display a confirmation message asking the user if they are sure they want to
			 * cancel the booking.
			 */
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Message");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to cancel the booking?");
			Optional<ButtonType> option = alert.showAndWait();

			if (option.get().equals(ButtonType.OK)) {
				/*
				 * If the user confirms the cancellation, execute the SQL query to cancel the
				 * booking.
				 */
				pst.executeUpdate();
				/*
				 * Display an information message to let the user know the booking was cancelled
				 * successfully.
				 */
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Message");
				alert.setHeaderText(null);
				alert.setContentText("Your Booking has been cancelled!");
				alert.showAndWait();
				return true;
			} else {
				// If the user cancels the cancellation, return false.
				return false;
			}
		} catch (SQLException e) {
			// Handle any errors that occur during execution and return false.
			e.printStackTrace();
			return false;
		}

	}

	// This method returns the number of cars with the specified status.
	public int getCarCount(String status) {
		// Build the SQL query to count the number of cars with the specified status.
		String sql = "SELECT COUNT(id) FROM car_rental_cardetails WHERE status = '" + status + "'";
		try {
			// Prepare the SQL statement for execution.
			pst = conn.connect().prepareStatement(sql);
			result = pst.executeQuery();

			while (result.next()) {
				// Return the count of cars with the specified status.
				return result.getInt("COUNT(id)");
			}

		} catch (Exception e) {
			// Handle any errors that occur during execution and return 0.
			e.printStackTrace();
		}
		return 0;
	}

	// This method returns the total income from all customer bookings.
	public double getTotalIncome() {
		// Build the SQL query to calculate the total income from all customer bookings.
		String sql = "SELECT SUM(total) FROM car_rental_customers";
		try {
			// Prepare the SQL statement for execution.
			pst = conn.connect().prepareStatement(sql);
			result = pst.executeQuery();

			while (result.next()) {
				// Return the total income from all customer bookings.
				return result.getDouble("SUM(total)");
			}

		} catch (Exception e) {
			// Handle any errors that occur during execution and return 0.
			e.printStackTrace();
		}
		return 0;
	}

	/*
	 * This method retrieves the total number of customers in the car rental
	 * database.It executes a SQL query that counts the number of customer_id
	 * entries in the car_rental_customers table.
	 */
	public int getTotalCustomers() {
		String sql = "SELECT COUNT(customer_id) FROM car_rental_customers";
		try {
			pst = conn.connect().prepareStatement(sql);
			result = pst.executeQuery();
			/*
			 * The result set should contain only one row with a single column that
			 * represents the total count of customers.
			 */
			while (result.next()) {
				return result.getInt("COUNT(customer_id)");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// If no customers were found, this method returns 0.
		return 0;
	}

	/*
	 * This method retrieves the total revenue information for the last six rental
	 * dates. It executes a SQL query that sums the total revenue for each rental
	 * date and orders the results by date in ascending order.
	 */
	public ResultSet getTotalRevenueDetails() {
		String sql = "SELECT date_rented, SUM(total) FROM car_rental_customers GROUP BY date_rented ORDER BY TIMESTAMP(date_rented) ASC LIMIT 6";
		try {
			pst = conn.connect().prepareStatement(sql);
			result = pst.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		/*
		 * This method returns the result set that contains the total revenue
		 * information.
		 */
		return result;
	}
}
