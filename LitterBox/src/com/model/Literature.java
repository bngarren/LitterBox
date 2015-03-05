package com.model;


public class Literature{

	private int id;
	private String title;
	private int datePublished;
	private String summary;
	private boolean isPDFAvailable;
	private String owner;
	private String pdfFilename;

	public Literature () {

	}

	public Literature(int id, String title, int datePublished){

		this.id = id;
		this.title = title;
		this.datePublished = datePublished;

		this.isPDFAvailable = false;

	}



	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("------------------------------------------");
		sb.append("\n\tID: " + id);
		sb.append("\n\tTitle: " + title);
		sb.append("\n\tDate published: " + datePublished);

		String summaryShort = summary;
		if (summary.length() > 100)
			summaryShort = summary.substring(0, 100) + "...";

		sb.append("\n\tSummary: " + summaryShort);
		sb.append("\n\tPDF?: " + isPDFAvailable);
		sb.append("\n------------------------------------------");

		return sb.toString();
	}

	public LiteratureHeading getLiteratureHeading(){
		return new LiteratureHeading(id, title, datePublished);
	}



	/* Getters and setters *//////////////////////////////////////////////////////////////




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




	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}




	public boolean isPDFAvailable() {
		return isPDFAvailable;
	}


	public void setPDFAvailable(boolean isPDFAvailable) {
		this.isPDFAvailable = isPDFAvailable;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public String getPdfFilename() {
		return pdfFilename;
	}


	public void setPdfFilename(String pdfFilename) {
		this.pdfFilename = pdfFilename;
	}








}
