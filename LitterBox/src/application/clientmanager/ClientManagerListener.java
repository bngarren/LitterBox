package application.clientmanager;

import com.model.Client;

public interface ClientManagerListener {

	void processingNewClient(Client client);
	void clientLoadingComplete(Client client);

}
