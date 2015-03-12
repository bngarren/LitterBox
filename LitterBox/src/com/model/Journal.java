package com.model;

public class Journal {

	private int id;
	private String issn;
	private String title;
	private String contributor;
	private String publisher;
	private String language;
	private String abbreviation;
	private boolean verified; // by look up on WorldCat

	public Journal() {


	}

	@Override
	public String toString() {
		return title;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Journal){
			Journal j = (Journal) obj;
			return j.getTitle().equals(title);
		} else {
			return super.equals(obj);
		}
	}



	//-------------------------------------
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
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public String getContributor() {
		return contributor;
	}

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}



}
