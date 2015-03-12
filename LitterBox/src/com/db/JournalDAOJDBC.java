package com.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.exception.LBException;

import com.model.Journal;

public class JournalDAOJDBC implements JournalDAO {

	private static final String SQL_FIND_BY_ID = "SELECT * FROM journals WHERE id = ?";
	private static final String SQL_FIND_BY_ISSN = "SELECT * FROM journals WHERE issn = ?";
	private static final String SQL_INSERT = "INSERT INTO journals (issn, title, contributor, language, abbreviation, verified) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE =
			"UPDATE journals SET issn = ?, title = ?, contributor = ?, language = ?, abbreviation = ?, verified = ? WHERE id = ?";

	private DAOFactory daoFactory;

	public JournalDAOJDBC(DAOFactory factory) {
		this.daoFactory = factory;
	}

	@Override
	public Journal find(int id) {
		return find(SQL_FIND_BY_ID, id);
	}

	@Override
	public Journal find(String issn) {
		return find(SQL_FIND_BY_ISSN, issn);
	}

	private Journal find(String sql, Object... values){
		Journal journal = null;

		try (
				Connection connection = daoFactory.getConnection();
				PreparedStatement stmt = DAOUtil.prepareStatement(connection, sql, false, values);
				ResultSet resultSet = stmt.executeQuery();
				)	{
			if (resultSet.next()){
				journal = map(resultSet);
			} else {
				System.out.println("JournalDAOJDBC: Could not find journal with SQL stmt = " + stmt.toString());

			}
		} catch (SQLException e) {
			throw new LBException(e);
		}
		return journal;
	}

	@Override
	public void create(Journal journal) {
		if (journal.getId() > 0)
			throw new IllegalArgumentException("Journal is already created, the Journal ID is not null.");

		Object[] values = {
				journal.getIssn(),
				journal.getTitle(),
				journal.getContributor(),
				journal.getLanguage(),
				journal.getAbbreviation(),
				journal.isVerified() ? 1 : 0
		};

		try (
				Connection connection = daoFactory.getConnection();
				PreparedStatement stmt = DAOUtil.prepareStatement(connection, SQL_INSERT, true, values);
				) {
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0){
				throw new LBException("Creating journal failed, no rows affected.");
			}

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()){
				if (generatedKeys.next()){
					journal.setId(generatedKeys.getInt(1));
				} else {
					throw new LBException("Creating literature failed, no generated key obtained");
				}
			}
		} catch (SQLException e) {
			throw new LBException(e);
		}

	}

	@Override
	public void update(Journal journal) {
		if (journal.getId() < 1){
			throw new IllegalArgumentException("Journal is not yet created, the journal ID is null");
		}

		Object[] values = {
				journal.getIssn(),
				journal.getTitle(),
				journal.getContributor(),
				journal.getLanguage(),
				journal.getAbbreviation(),
				journal.isVerified() ? 1 : 0,
						journal.getId()
		};

		try (
				Connection connection = daoFactory.getConnection();
				PreparedStatement stmt = DAOUtil.prepareStatement(connection, SQL_UPDATE, true, values);
				) {
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0){
				throw new LBException("Updating journal failed, no rows affected.");
			}

		} catch (SQLException e) {
			throw new LBException(e);
		}

	}

	// Helpers ------------------------------------------------------------------------------------




	/**
	 * Map the current row of the given ResultSet to a Journal.
	 * @param resultSet The ResultSet of which the current row is to be mapped to a Journal.
	 * @return The mapped Journal from the current row of the given ResultSet.
	 * @throws SQLException If something fails at database level.
	 */
	private static Journal map(ResultSet resultSet) throws SQLException {
		Journal journal = new Journal();
		journal.setId(resultSet.getInt("id"));
		journal.setIssn(resultSet.getString("issn"));
		journal.setTitle(resultSet.getString("title"));
		journal.setAbbreviation(resultSet.getString("abbreviation"));
		journal.setContributor(resultSet.getString("contributor"));
		journal.setLanguage(resultSet.getString("language"));
		journal.setVerified(resultSet.getBoolean("verified"));

		return journal;
	}



}
