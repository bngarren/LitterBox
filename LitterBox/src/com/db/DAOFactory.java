package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import application.LBProperties;
import application.exception.LBException;

public abstract class DAOFactory {

	// Constants -------------------------------------------------------

	private static final String PROPERTY_URL = "url";
	private static final String PROPERTY_USERNAME = "username";
	private static final String PROPERTY_PASSWORD = "password";

	public static DAOFactory getInstance(String databaseName) throws LBException {

		if (databaseName == null){
			throw new LBException("Database name is null");
		}

		LBProperties properties = new LBProperties(databaseName);
		String url = properties.getProperty(PROPERTY_URL, true) + databaseName;
		String username = properties.getProperty(PROPERTY_USERNAME, true);
		String password = properties.getProperty(PROPERTY_PASSWORD, true);

		DAOFactory instance;

		instance = new DefaultDOAFactory(url, username, password);

		return instance;

	}

	abstract Connection getConnection() throws SQLException;

	// DAO implementation getters --------------------------------------

	public LiteratureDAO getLiteratureDAO(){
		return new LiteratureDAOJDBC(this);
	}

	public ClientDAO getClientDAO(){
		return new ClientDAOJDBC(this);
	}



}

/* Default DAOFactory implementations
 *
 */

class DefaultDOAFactory extends DAOFactory {

	private String url;
	private String username;
	private String password;

	public DefaultDOAFactory(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	@Override
	Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

}
