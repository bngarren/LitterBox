package com.db;

import java.util.List;

import application.exception.LBException;

import com.model.Client;

public interface ClientDAO {

	public Client find(int id) throws LBException;

	public Client find(String username) throws LBException;

	public List<Client> list() throws LBException;

}
