package com.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.exception.LBException;

import com.model.Client;

public class ClientDAOJDBC implements ClientDAO {

	// Constants ----------------------------------------------------------------

	private static final String SQL_FIND_BY_ID =
			"SELECT * FROM clients WHERE id = ?";
	private static final String SQL_FIND_BY_USERNAME =
			"SELECT * FROM clients WHERE username = ?";
	private static final String SQL_LIST_ORDER_BY_ID =
			"SELECT * FROM clients ORDER BY id";

	private DAOFactory daoFactory;

	public ClientDAOJDBC(DAOFactory factory) {
		this.daoFactory = factory;
	}

	@Override
	public Client find(int id) throws LBException {
		return find(SQL_FIND_BY_ID, id);
	}

	@Override
	public Client find(String username) throws LBException {
		return find(SQL_FIND_BY_USERNAME, username);
	}

	private Client find(String sql, Object... values){
		Client client = null;

		try (
				Connection connection = daoFactory.getConnection();
				PreparedStatement stmt = DAOUtil.prepareStatement(connection, sql, false, values);
				ResultSet resultSet = stmt.executeQuery();
				)	{
			if (resultSet.next()){
				client = map(resultSet);
			} else {
				//throw new LBException("Could not find client with SQL stmt = " + stmt.toString());
				System.out.print("ClientDAO: could not find client by parameters = ");
				for (Object o : values)
					System.out.println(o.toString());

			}
		} catch (SQLException e) {
			throw new LBException(e);
		}
		return client;
	}

	@Override
	public List<Client> list() throws LBException {
		List<Client> result = new ArrayList<Client>();

		try (
				Connection connection = daoFactory.getConnection();
				PreparedStatement stmt = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
				ResultSet resultSet = stmt.executeQuery();
				)	{
			while (resultSet.next()){
				result.add(map(resultSet));
			}
		}  catch (SQLException e) {
			throw new LBException(e);
		}
		return result;
	}


	// Helpers ------------------------------------------------------------------------------------


	/**
	 * Map the current row of the given ResultSet to an User.
	 * @param resultSet The ResultSet of which the current row is to be mapped to an User.
	 * @return The mapped User from the current row of the given ResultSet.
	 * @throws SQLException If something fails at database level.
	 */
	private static Client map(ResultSet resultSet) throws SQLException {
		Client client = new Client(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"));
		client.setServerDirectory(resultSet.getString("server_directory"));
		return client;
	}


}
