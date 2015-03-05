package com.db;

import java.util.List;

import application.exception.LBException;

import com.model.Literature;

public interface LiteratureDAO {

	public Literature find(int id) throws LBException;

	public List<Literature> list();

	public List<Literature> listByOwner(String owner);

	public void create(Literature literature);

	public void update(Literature literature);

}
