/**
 * This class acts as a controller for user login and sign up operations.
 * @authors  Naveena Kandasamy Rajkumar, Manish Sai Krishna Rudrakoti Chandrani
 */
package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.DAOModel;

public class LoginSignUpController {

	// Instance variables
	private MainHomePageController mainHomePage;
	public static String userName;
	public static String path;
	DAOModel dao;

	public LoginSignUpController() {
		dao = new DAOModel();
	}

	// FXML components
	@FXML
	private Button adminLoginBtn;

	@FXML
	private PasswordField adminPassword;

	@FXML
	private TextField adminUserName;

	@FXML
	private PasswordField empPassword;

	@FXML
	private TextField empUserName;

	@FXML
	private PasswordField customerPassword;

	@FXML
	private TextField customerUserName;

	@FXML
	private PasswordField customerConfirmPassword;

	@FXML
	private TextField customerEmail;

	@FXML
	private TextField customerPhoneNumber;

	@FXML
	private Button cancelBtn;

	// Navigate to login pages
	@FXML
	private void goToAdminLoginPage() throws IOException {
		mainHomePage.showPage("/views/adminLogin.fxml");
	}

	@FXML
	private void goToCustomerLoginPage() throws IOException {
		mainHomePage.showPage("/views/customerLogin.fxml");
	}

	@FXML
	private void goToEmployeeLoginPage() throws IOException {
		mainHomePage.showPage("/views/employeeLogin.fxml");
	}

	// Navigate to sign up page
	@FXML
	private void goToCustomerSignUp() throws IOException {
		mainHomePage.showPage("/views/signUpPage.fxml");
	}

	// Login actions
	@FXML
	private void loginAdmin() throws IOException {
		boolean isLoggedIn = dao.loginUser(adminUserName, adminPassword, "admin");
		if (isLoggedIn) {
			mainHomePage.showPage("/views/adminDashboardCharts.fxml");
		}
	}

	//method to login employee
	@FXML
	private void loginEmployee() {
		boolean isLoggedIn = dao.loginUser(empUserName, empPassword, "employee");
		if (isLoggedIn) {
			mainHomePage.showPage("/views/employeeHomePage.fxml");
		}
	}

	// method to login the customer
	@FXML
	private void loginCustomer() {
		boolean isLoggedIn = dao.loginUser(customerUserName, customerPassword, "customer");
		if (isLoggedIn) {
			mainHomePage.showPage("/views/RentCarsPage.fxml");
		}
	}

	// method to Sign up the customer
	@FXML
	private void SignUpCustomer() {
		boolean isSignedUp = dao.signUpUser(customerEmail, customerPhoneNumber, customerUserName, customerPassword,
				customerConfirmPassword);
		if (isSignedUp) {
			mainHomePage.showPage("/views/customerLogin.fxml");
		}
	}

	// Navigate to home page
	@FXML
	private void goToHomePage() throws IOException {
		mainHomePage.showMainHomePageView();
	}

}
