package com.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.exception.LBException;

import com.model.Literature;

public class LiteratureDAOJDBC implements LiteratureDAO {

	// Constants ---------------------------------------------------------------

	private static final String SQL_FIND_BY_ID = "SELECT * FROM literature WHERE id = ?";
	private static final String SQL_LIST_ORDER_BY_ID = "SELECT * FROM literature ORDER BY id";
	private static final String SQL_LIST_BY_OWNER = "SELECT * FROM literature WHERE owner = ?";
	private static final String SQL_INSERT = "INSERT INTO literature (title, date_published, summary, isPDFAvailable, pdfFilename, owner) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE =
			"UPDATE literature SET title = ?, date_published = ?, summary = ?, isPDFAvailable = ?, pdfFilename = ?, owner = ? WHERE id = ?";
	private static final String SQL_SEARCH_TITLE_AND_SUMMARY_FOR_STRING = "SELECT * FROM literature WHERE MATCH (title, summary) AGAINST (? IN BOOLEAN MODE)";
	private static final String SQL_SEARCH_TITLE_AND_SUMMARY_FOR_STRING_BY_OWNER = "SELECT * FROM literature WHERE MATCH (title, summary) AGAINST (? IN BOOLEAN MODE) AND owner = ?";

	private DAOFactory daoFactory;

	public LiteratureDAOJDBC(DAOFactory factory) {
		this.daoFactory = factory;
	}

	@Override
	public Literature find(int id) throws LBException {
		return find(SQL_FIND_BY_ID, id);
	}

	private Literature find(String sql, Object... values){
		Literature literature = null;

		try (
				Connection connection = daoFactory.getConnection();
				PreparedStatement stmt = DAOUtil.prepareStatement(connection, sql, false, values);
				ResultSet resultSet = stmt.executeQuery();
				)	{
			if (resultSet.next()){
				literature = map(resultSet);
			} else {
				throw new LBException("Could not find literature with SQL stmt = " + stmt.toString());

			}
		} catch (SQLException e) {
			throw new LBException(e);
		}
		return literature;
	}



	@Override
	public List<Literature> list() {

		List<Literature> result = new ArrayList<Literature>();

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


	@Override
	public List<Literature> listByOwner(String owner) {
		List<Literature> result = new ArrayList<Literature>();

		try (
				Connection connection = daoFactory.getConnection();
				PreparedStatement stmt = DAOUtil.prepareStatement(connection, SQL_LIST_BY_OWNER, false, owner);
				ResultSet resultSet = stmt.executeQuery();
				)	{
			while (resultSet.next()){
				result.add(map(resultSet));
			}
		} catch (SQLException e) {
			throw new LBException(e);
		}
		return result;
	}


	@Override
	public void create(Literature literature){
		if (literature.getId() > 0)
			throw new IllegalArgumentException("Literature is already created, the literature ID is not null.");

		Object[] values = {
				literature.getTitle(),
				literature.getDatePublished(),
				literature.getSummary(),
				literature.isPDFAvailable(),
				literature.getPdfFilename(),
				literature.getOwner()
		};

		try (
				Connection connection = daoFactory.getConnection();
				PreparedStatement stmt = DAOUtil.prepareStatement(connection, SQL_INSERT, true, values);
				) {
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0){
				throw new LBException("Creating literature failed, no rows affected.");
			}

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()){
				if (generatedKeys.next()){
					literature.setId(generatedKeys.getInt(1));
				} else {
					throw new LBException("Creating literature failed, no generated key obtained");
				}
			}
		} catch (SQLException e) {
			throw new LBException(e);
		}

	}

	@Override
	public void update(Literature literature){
		if (literature.getId() < 1){
			throw new IllegalArgumentException("Literature is not yet created, the literature ID is null");
		}

		Object[] values = {
				literature.getTitle(),
				literature.getDatePublished(),
				literature.getSummary(),
				literature.isPDFAvailable(),
				literature.getPdfFilename(),
				literature.getOwner(),
				literature.getId()
		};

		try (
				Connection connection = daoFactory.getConnection();
				PreparedStatement stmt = DAOUtil.prepareStatement(connection, SQL_UPDATE, true, values);
				) {
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0){
				throw new LBException("Updating literature failed, no rows affected.");
			}

		} catch (SQLException e) {
			throw new LBException(e);
		}


	}

	@Override
	public List<Literature> searchTitleAndSummaryFor(String s, String owner) {
		return searchTitleAndSummary(SQL_SEARCH_TITLE_AND_SUMMARY_FOR_STRING_BY_OWNER, s, owner);
	}

	@Override
	public List<Literature> searchTitleAndSummaryFor(String s) {
		return searchTitleAndSummary(SQL_SEARCH_TITLE_AND_SUMMARY_FOR_STRING, s);
	}

	private List<Literature> searchTitleAndSummary(String sql, Object... values){
		List<Literature> result = new ArrayList<Literature>();

		String inputText = (String) values[0];

		if (inputText.trim().length() == 0 || inputText.trim().isEmpty()){
			System.err.println("Cannot search literature titles with an empty string");
			return result;
		} else {
			values[0] = inputText + "*";
		}

		try (
				Connection connection = daoFactory.getConnection();
				PreparedStatement stmt = DAOUtil.prepareStatement(connection, sql, false, values);
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
	 * Map the current row of the given ResultSet to a Literature.
	 * @param resultSet The ResultSet of which the current row is to be mapped to a Literature.
	 * @return The mapped Literature from the current row of the given ResultSet.
	 * @throws SQLException If something fails at database level.
	 */
	private static Literature map(ResultSet resultSet) throws SQLException {
		Literature literature = new Literature(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getInt("date_published"));
		literature.setSummary(resultSet.getString("summary"));
		literature.setPDFAvailable(resultSet.getBoolean("isPDFAvailable"));
		literature.setPdfFilename(resultSet.getString("pdfFilename"));
		literature.setOwner(resultSet.getString("owner"));

		return literature;
	}





}
