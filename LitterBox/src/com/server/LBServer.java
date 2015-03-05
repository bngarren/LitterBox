package com.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import application.LBProperties;
import application.exception.LBException;

public enum LBServer {

	INSTANCE;

	private LBProperties properties;

	LBServer(){
		properties = new LBProperties("server");
	}

	public URL getURLOfClientDirectory(String clientServerDirectory){

		URL result = null;

		try {
			result = new URL("http", properties.getProperty("url", true), Integer.valueOf(properties.getProperty("server_port", true)),
					properties.getProperty("path_to_root", true) + clientServerDirectory);
		} catch (NumberFormatException | MalformedURLException | LBException e) {
			e.printStackTrace();
		}

		return result;
	}

	public boolean doesClientDirectoryExist(URL urlOfClientDirectory){
		HttpURLConnection con = null;
		int response = -1;
		try {
			con = (HttpURLConnection) urlOfClientDirectory.openConnection();
			response = con.getResponseCode();
			System.out.println(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response == HttpURLConnection.HTTP_OK;
	}

}
