package application.clientmanager;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import application.LBProperties;

import com.model.Client;
import com.server.LBServer;

public enum ClientManager {

	INSTANCE;

	LBProperties clientProp;
	LBProperties serverProp;

	private Client currentClient;
	private URL pathToClientDirectoryOnSever;
	private File xmlJournals;

	private ClientManagerListener listener;

	ClientManager(){

		clientProp = new LBProperties("client");
		serverProp = new LBProperties("server");


	}

	public void setClient(Client client){

		if (listener != null){
			listener.processingNewClient(client);
		}

		this.currentClient = client;

		this.pathToClientDirectoryOnSever = getURLToClientDirectoryOnServer();

		loadClientData();

	}

	private URL getURLToClientDirectoryOnServer(){
		if (this.currentClient == null){
			System.err.println("ClientManager: could not get URL to client directory on server because client is null.");
			return null;
		}

		String clientDirectory = "/" + currentClient.getServerDirectory();

		URL urlToClientDirectory = LBServer.INSTANCE.getURLOfClientDirectory(clientDirectory);

		if (urlToClientDirectory != null){
			if (LBServer.INSTANCE.doesClientDirectoryExist(urlToClientDirectory)){
				System.out.println("ClientManager: " + currentClient.getUsername() + "'s server directory URL is = " + urlToClientDirectory.toExternalForm());
				return urlToClientDirectory;
			} else {
				System.err.println("ClientManager: Could not find server directory for " + currentClient.getUsername() + " at " + urlToClientDirectory.toExternalForm());
			}
		} else {
			System.err.println("ClientManager: Could not resolve URL to server directory for " + currentClient.getUsername() + " at " + urlToClientDirectory.toExternalForm());
		}
		return null;

	}

	private void loadClientData(){

		if (listener != null){
			listener.clientLoadingComplete(this.currentClient);
		}

	}

	private void loadAndStoreJournalData() throws IOException, URISyntaxException{


	}

	public Client getCurrentClient() {
		return currentClient;
	}

	public ClientManagerListener getListener() {
		return listener;
	}

	public void setListener(ClientManagerListener listener) {
		this.listener = listener;
	}

}
