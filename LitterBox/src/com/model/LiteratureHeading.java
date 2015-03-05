package com.model;

public class LiteratureHeading {

	private int id;
	private String title;
	private int datePublished;
	private boolean isPDFAvailable;

	public LiteratureHeading(int id, String title, int datePublished){
		this.id = id;
		this.title = title;
		this.datePublished = datePublished;
		this.isPDFAvailable = true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(int datePublished) {
		this.datePublished = datePublished;
	}

	public boolean isPDFAvailable() {
		return isPDFAvailable;
	}

	public void setPDFAvailable(boolean isPDFAvailable) {
		this.isPDFAvailable = isPDFAvailable;
	}



}
