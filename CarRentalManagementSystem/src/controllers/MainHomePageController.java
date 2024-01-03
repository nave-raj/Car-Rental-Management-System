/**
This class represents the Main class that extends Application
@authors Naveena Kandasamy Rajkumar, Manish Sai Krishna Rudrakoti Chandrani
*/
package controllers;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class MainHomePageController extends Application {
	// Declare static variables for the stage and layouts
	private static Stage primaryStage;
	private static BorderPane mainBPaneLayout;
	public static StackPane mainSPaneLayout;
	private static Parent mainLayout;

	@Override
	public void start(Stage primaryStage) {
		try {
			// Set the primary stage and title, and call the showMainHomePageView method
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Car Rental Mangement System");
			showMainHomePageView();

		} catch (Exception e) {
			// Catch any exceptions and print the stack trace
			e.printStackTrace();
		}
	}

	// Method to show the main home page view
	public static void showMainHomePageView() throws IOException {
		// Load the FXML file for the main view
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainHomePageController.class.getResource("/views/mainView.fxml"));
		mainBPaneLayout = loader.load();
		// Create a new scene and set it on the primary stage
		Scene scene = new Scene(mainBPaneLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Method to show a specific FXML file
	public static void showPage(String FXMLFileName) {
		try {
			// Load the FXML file for the specified view
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainHomePageController.class.getResource(FXMLFileName));
			mainLayout = loader.load();
			// Create a new scene and set it on the primary stage
			Scene scene = new Scene(mainLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			// Catch any exceptions and print the stack trace
			e.printStackTrace();
		}
	}

	// Main method to launch the application
	public static void main(String[] args) {
		launch(args);
	}

}
