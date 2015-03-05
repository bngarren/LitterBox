package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;

public class PDFDownloader {

	public static final String USER_HOME_DIR = System.getProperty("user.home");
	public static final String TEMP_DIR = USER_HOME_DIR + "\\lit_temp";

	private URLConnection connection;
	private URL url;
	private File tempDir;

	private boolean isComplete;

	public PDFDownloader(URL url) throws IOException{
		this.url = url;
		connection = url.openConnection();

		System.out.println("PDFDownloader: connected to -> " + connection.getURL());

		tempDir = new File(TEMP_DIR);

		if (!tempDir.exists()){
			Files.createDirectory(new File(TEMP_DIR).toPath());
		}


	}

	public File getFileFromServer() throws IOException{

		this.isComplete = false;

		String filename = url.getFile().substring(url.getFile().lastIndexOf("/") + 1);
		File result = new File(tempDir, "\\" + filename);

		String ext = url.getFile().substring(url.getFile().lastIndexOf(".") + 1);
		System.out.println(connection.getContentType());
		if (!ext.equals("pdf")){
			System.out.println("FAILED.\n[Sorry. This is not a PDF.]");
		} else {

			while (!result.createNewFile()){
				result.delete();
			}

			FileUtils.copyURLToFile(url, result, 5000, 5000);

			result.deleteOnExit();


			this.isComplete = true;

		}

		return result;

	}



	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public static void main(String[] args) throws IOException{

		URL url = new URL("http://localhost/LiteratureKeeper/pdfs"
				+ "/bngarren/test.pdf");
		PDFDownloader loader = new PDFDownloader(url);
		loader.getFileFromServer();

	}

}
