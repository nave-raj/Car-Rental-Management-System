/**
 * This class provides a method to establish a connection to the database.
 * It requires the MySQL JDBC driver to be installed and in the classpath.
 * @authors  Naveena Kandasamy Rajkumar, Manish Sai Krishna Rudrakoti Chandrani
 * */

package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
	/*
	 * The URL that specifies the location of the database and the protocol to use
	 * for connecting to it.
	 */
	static final String DB_URL = "jdbc:mysql://www.papademas.net:3307/510fp?autoReconnect=true&useSSL=false";
	/* The credentials to use for authenticating to the database. */
	static final String USER_NAME = "fp510", PASSWORD = "510";

	/*
	 * This method establishes a connection to the database using the specified URL
	 * and credentials.
	 */
	public Connection connect() throws SQLException {
		return DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
	}
}