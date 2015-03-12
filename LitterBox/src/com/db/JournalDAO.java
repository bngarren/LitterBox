package com.db;

import com.model.Journal;

public interface JournalDAO {

	public Journal find(int id);
	public Journal find(String issn);
	public void create(Journal journal);
	public void update(Journal journal);

}
