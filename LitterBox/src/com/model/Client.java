package com.model;

public class Client {

	private int id;
	private String username;
	private String password;
	private String serverDirectory;

	public Client (int id, String username, String password){
		this.id = id;
		this.username = username;
		this.password = password;

	}



	@Override
	public String toString() {

		return String.format("Client: \n\tid = %s \n\tusername = %s \n\tpassword = %s \n\tserver_directory = %s", id, username, password, serverDirectory);

	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServerDirectory() {
		return serverDirectory;
	}

	public void setServerDirectory(String serverDirectory) {
		this.serverDirectory = serverDirectory;
	}




}
