/**
 * This class acts as a controller for Admin user operations(CRUD) on car rental portal
 * @author  Naveena Kandasamy Rajkumar
 */

package controllers;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import models.DAOModel;

public class AdminDashboardController implements Initializable {

	DAOModel dao;

	// Constructor for the class
	public AdminDashboardController() {
		// Initialize the dao instance variable by creating a new instance of DAOModel
		dao = new DAOModel();
	}

	/*
	 * These are the JavaFX UI elements used in the Manage Cars tab of the admin
	 * dashboard
	 */
	@FXML
	private TableColumn<CarData, Integer> manageCars_CarIdCol;
	@FXML
	private TextField manageCars_brand;
	@FXML
	private TableColumn<CarData, String> manageCars_brandCol;
	@FXML
	private TextField manageCars_carId;
	@FXML
	private Button manageCars_clearBtn;
	@FXML
	private Button manageCars_deleteBtn;
	@FXML
	private AnchorPane manageCars_grid;
	@FXML
	private ImageView manageCars_imageView;
	@FXML
	private Button manageCars_addBtn;
	@FXML
	private TextField manageCars_model;
	@FXML
	private TableColumn<CarData, String> manageCars_modelCol;
	@FXML
	private TextField manageCars_price;
	@FXML
	private TableColumn<CarData, Double> manageCars_priceCol;
	@FXML
	private TextField manageCars_search;
	@FXML
	private ComboBox<String> manageCars_status;
	@FXML
	private TableColumn<CarData, String> manageCars_statusCol;
	@FXML
	private TableView<CarData> manageCars_tableView;
	@FXML
	private Button manageCars_editBtn;
	@FXML
	private Label adminPage_userName;
	@FXML
	private AnchorPane admin_form;

	/*
	 * These are the JavaFX UI elements used in the Admin Home tab of the admin
	 * dashboard
	 */
	@FXML
	private Label adminHome_carsAvailable;
	@FXML
	private Label adminHome_carsRented;
	@FXML
	private Label adminHome_totalBookings;
	@FXML
	private Label adminHome_totalRevenue;
	@FXML
	private LineChart<?, ?> bookings_chart;
	@FXML
	private BarChart<?, ?> total_revenue_chart;

	/*
	 * These are the JavaFX UI elements used in the Booking History tab of the admin
	 * dashboard
	 */
	@FXML
	private TableColumn<CustomerData, String> bookingHistory_brandCol;
	@FXML
	private TableColumn<CustomerData, String> bookingHistory_carIdCol;
	@FXML
	private TableColumn<CustomerData, String> bookingHistory_customerIdCol;
	@FXML
	private TableColumn<CustomerData, String> bookingHistory_customerNameCol;
	@FXML
	private TableColumn<CustomerData, String> bookingHistory_modelCol;
	@FXML
	private TableColumn<CustomerData, ?> bookingHistory_priceCol;
	@FXML
	private TableColumn<CustomerData, String> bookingHistory_rentDateCol;
	@FXML
	private TableColumn<CustomerData, String> bookingHistory_returnDateCol;
	@FXML
	private TableColumn<CustomerData, String> bookingHistory_statusCol;
	@FXML
	private TableView<CustomerData> empBookingHistoryTable;

	// Other class variables
	private MainHomePageController mainHomePage;
	private ObservableList<CarData> availableCarList;

	/*
	 * Returns the list of all available cars by calling the getAllCarDetails method
	 * of the DAOModel class.
	 */
	public ObservableList<CarData> availableCarListData() {
		return dao.getAllCarDetails();
	}

	// Displays the number of available and rented cars on the home page.
	public void homeAvailableCars() {
		int available_count = 0;
		int notavailable_count = 0;

		if (adminHome_carsAvailable != null && adminHome_carsRented != null) {
			available_count = dao.getCarCount("Available");
			notavailable_count = dao.getCarCount("Not Available");
			adminHome_carsAvailable.setText(String.valueOf(available_count));
			adminHome_carsRented.setText(String.valueOf(notavailable_count));
		}

	}

	// Displays the total revenue earned from car rentals on the home page.
	public void homeTotalIncome() {
		double sumIncome = 0;
		if (adminHome_totalRevenue != null) {
			sumIncome = dao.getTotalIncome();
			adminHome_totalRevenue.setText("$" + String.valueOf(sumIncome));
		}
	}

	// Displays the total number of customers who have rented cars on the home page.
	public void homeTotalCustomers() {
		int bookings_count = 0;
		if (adminHome_totalBookings != null) {
			bookings_count = dao.getTotalCustomers();
			adminHome_totalBookings.setText(String.valueOf(bookings_count));
		}
	}

	/*
	 * Populates the Manage Cars table by setting the cell value factory of each
	 * column and assigning the availableCarList data to the table.
	 */
	public void manageCarsTableData() {
		availableCarList = (ObservableList<CarData>) availableCarListData();
		if (manageCars_CarIdCol != null && manageCars_brandCol != null && manageCars_modelCol != null) {
			manageCars_CarIdCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
			manageCars_brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
			manageCars_modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
			manageCars_priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
			manageCars_statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

			manageCars_tableView.setItems(availableCarList);
		}
	}

	// Method to display booking history data for a user.
	public void showUserBookingData() {
		if (bookingHistory_customerIdCol != null && empBookingHistoryTable != null) {
			try {
				ObservableList<CustomerData> customerList = FXCollections.observableArrayList();
				ResultSet result = dao.getUserBookingDetails(true);
				while (result.next()) {
					CustomerData customer_data = new CustomerData(result.getInt("customer_id"),
							result.getString("car_id"), result.getString("brand"), result.getString("model"),
							result.getDouble("total"), result.getDate("date_returned"), result.getDate("date_rented"),
							result.getString("booking_status"));
					// Add retrieved customer data to customerList
					customerList.add(customer_data);
				}

				// Set cell value factory for all columns using PropertyValueFactory
				bookingHistory_customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
				bookingHistory_carIdCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
				bookingHistory_brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
				bookingHistory_modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
				bookingHistory_priceCol.setCellValueFactory(new PropertyValueFactory<>("total"));
				bookingHistory_rentDateCol.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
				bookingHistory_returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
				bookingHistory_statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

				// Populate employee booking history table with customerList
				empBookingHistoryTable.setItems(customerList);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	// Method to generate and display a bookings chart.
	public void bookingsChart() {
		if (bookings_chart != null) {
			// Clear existing data from bookings chart
			bookings_chart.getData().clear();

			try {
				// Create a new XYChart.Series object
				XYChart.Series chart = new XYChart.Series();

				/*
				 * Retrieve booking details from the database and add them to the chart data
				 * series
				 */
				ResultSet result = dao.getBookingDetails();
				while (result.next()) {
					chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
				}

				// Add the chart data series to the bookings chart
				bookings_chart.getData().add(chart);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	// Method to generate and display a revenue chart.
	public void revenueChart() {
		if (total_revenue_chart != null) {
			// Clear existing data from total revenue chart
			total_revenue_chart.getData().clear();
			try {
				// Create a new XYChart.Series object
				XYChart.Series chart = new XYChart.Series();

				/*
				 * Retrieve total revenue details from the database and add them to the chart
				 * data series
				 */
				ResultSet result = dao.getTotalRevenueDetails();
				while (result.next()) {
					chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
				}

				// Add the chart data series to the total revenue chart
				total_revenue_chart.getData().add(chart);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Method to select an available car and populate the relevant fields with the
	 * car's details.
	 */
	public void availableCarSelect() {
		/*
		 * Retrieve the selected car data from the manageCars_tableView and its index
		 * number
		 */
		CarData car_data = manageCars_tableView.getSelectionModel().getSelectedItem();
		int num = manageCars_tableView.getSelectionModel().getSelectedIndex();

		// If no car is selected, exit the method
		if ((num - 1) < -1) {
			return;
		}

		// Populate the car data fields with the selected car's details
		manageCars_carId.setText(String.valueOf(car_data.getCarId()));
		manageCars_brand.setText(car_data.getBrand());
		manageCars_model.setText(car_data.getModel());
		manageCars_price.setText(String.valueOf(car_data.getPrice()));
		manageCars_status.setValue(car_data.getStatus());

		/*
		 * Disable the manageCars_addBtn button to prevent adding a duplicate car to the
		 * database
		 */
		manageCars_addBtn.setDisable(true);
	}

	/*
	 * Method to add new car details to the database using the data entered by the
	 * user in the manageCars fields.
	 */
	public void addCarDetails() {
		boolean isInserted = dao.addCarDetails(manageCars_carId, manageCars_brand, manageCars_model, manageCars_status,
				manageCars_price);

		/*
		 * If the car details are successfully added to the database, update the
		 * tableview and clear the input fields
		 */
		if (isInserted) {
			manageCarsTableData();
			manageCarClearData();
		}
	}

	/*
	 * Method to update existing car details in the database using the data entered
	 * by the user in the manageCars fields.
	 */
	public void updateCarDetails() {
		boolean isUpdated = dao.updateCarDetails(manageCars_carId, manageCars_brand, manageCars_model,
				manageCars_status, manageCars_price);

		/*
		 * If the car details are successfully updated in the database, update the
		 * tableview and clear the input fields
		 */
		if (isUpdated) {
			manageCarsTableData();
			manageCarClearData();
		}
	}

	/*
	 * Method to delete existing car details from the database using the data
	 * entered by the user in the manageCars fields.
	 */
	public void deleteCarDetails() {
		boolean isDeleted = dao.deleteCarDetails(manageCars_carId, manageCars_brand, manageCars_model,
				manageCars_status, manageCars_price);

		/*
		 * If the car details are successfully deleted from the database, update the
		 * tableview and clear the input fields
		 */
		if (isDeleted) {
			manageCarsTableData();
			manageCarClearData();
		}
	}

	/*
	 * This method is used to populate the "status" ComboBox in the manage cars page
	 * with values "Available" and "Not Available"
	 */
	public void manageCarsStatusList() {
		List<String> carstatus = new ArrayList<String>(Arrays.asList("Available", "Not Available"));
		ObservableList listData = FXCollections.observableArrayList(carstatus);
		if (manageCars_status != null) {
			manageCars_status.setItems(listData);
		}
	}

	// This method is used to clear the input fields in the manage cars page
	public void manageCarClearData() {
		manageCars_carId.setText("");
		manageCars_brand.setText("");
		manageCars_model.setText("");
		manageCars_status.getSelectionModel().clearSelection();
		manageCars_price.setText("");
		manageCars_addBtn.setDisable(false);
	}

	// This method is used to display the logged-in user's name on the admin page
	public void displayUserName() {
		String user = LoginSignUpController.userName;
		if (user != null && adminPage_userName != null) {
			adminPage_userName.setText(user.substring(0, 1).toUpperCase() + user.substring(1));
		}
	}


	/*
	 * This method is used to search for car details based on a search term entered
	 * by the user in the manage cars page
	 */
	@FXML
	private void searchCarDetails(ActionEvent event) {
		String searchTerm = manageCars_search.getText().toLowerCase();

		ObservableList<CarData> filteredCarList = FXCollections.observableArrayList();
		for (CarData car : availableCarListData()) {
			if (car.getBrand().toLowerCase().contains(searchTerm) || car.getModel().toLowerCase().contains(searchTerm)
					|| String.valueOf(car.getCarId()).contains(searchTerm)) {
				filteredCarList.add(car);
			}
		}

		manageCars_tableView.setItems(filteredCarList);
	}

	/*
	 * This method is used to log out the user and return to the main login/signup
	 * page
	 */
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

	// These methods are used to switch between different pages in the application
	@FXML
	public void switchToHomePage() {
		try {
			mainHomePage.showPage("/views/adminDashboardCharts.fxml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void switchToManageCarsPage() {
		try {
			mainHomePage.showPage("/views/adminHomePage.fxml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void switchToEmployeeManageCars() {
		try {
			mainHomePage.showPage("/views/employeeHomePage.fxml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void switchToEmployeeBookingHistory() {
		try {
			showUserBookingData();
			mainHomePage.showPage("/views/employeeManageBookings.fxml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * The method calls a number of other methods in the class, each of which
	 * initializes a specific aspect of the UI.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		displayUserName();
		homeAvailableCars();
		homeTotalIncome();
		homeTotalCustomers();
		manageCarsStatusList();
		manageCarsTableData();
		bookingsChart();
		revenueChart();
		showUserBookingData();
	}

}
