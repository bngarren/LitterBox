package com.db;

import java.util.List;
import java.util.Scanner;

import com.model.Client;
import com.model.Literature;

public class DAOTest {

	static final String TAG = "DAOTest";

	public static void main(String[] args){


		// Obtain DAOFactory
		DAOFactory litKeeperDB = DAOFactory.getInstance("lit_keeper");
		debug("DAOFactory successfully obtained: " + litKeeperDB);

		LiteratureDAO literatureDAO = litKeeperDB.getLiteratureDAO();
		debug("LiteratureDAO successfully obtained: " + literatureDAO);

		ClientDAO clientDAO = litKeeperDB.getClientDAO();
		debug("ClientDAO successfully obtained: " + clientDAO);

		Scanner scanner = new Scanner(System.in);

		while (scanner.hasNext()){

			String input = scanner.nextLine();
			String[] command = input.split(" ");

			switch (command[0]){
			case "list-lit":
				List<Literature> allLiterature = literatureDAO.list();
				allLiterature.forEach(action -> System.out.println(action.toString()));
				break;
			case "get-lit-id":
				Literature literature = literatureDAO.find(Integer.valueOf(command[1]));
				debug("Successfully found literature: ");
				debug(literature.toString());
				break;

			case "get-lit-owner":
				debug("All literature by owner " + command[1]);
				List<Literature> allLiteratureByOwner = literatureDAO.listByOwner(command[1]);
				allLiteratureByOwner.forEach(action -> System.out.println(action.toString()));
				break;

			case "create-lit":

				Literature test = new Literature();
				test.setTitle("Test literature");
				test.setDatePublished(2009);
				test.setSummary("This is a test summary");
				test.setPDFAvailable(true);
				test.setPdfFilename("test.pdf");
				test.setOwner("bngarren");

				literatureDAO.create(test);

				debug("Successfully created literature:");
				debug(test.toString());

				break;

			case "get-client-id":
				Client client = clientDAO.find(Integer.valueOf(command[1]));
				debug("Successfully found client: ");
				debug(client.toString());
				break;

			case "get-client-username":
				Client client2 = clientDAO.find(command[1]);
				debug("Successfully found client: ");
				debug(client2.toString());
				break;

			case "list-clients":
				List<Client> clients = clientDAO.list();
				clients.forEach(action -> System.out.println(action.toString()));
				break;

			case "search-lit":
				List<Literature> literatureByTitleSearch = literatureDAO.searchTitleAndSummaryFor(command[1]);
				literatureByTitleSearch.forEach(action -> System.out.println(action.getTitle()));

				break;

			case "quit":
				System.exit(0);

				break;

			default:
				debug("Enter a command.");
				break;
			}

		}

		scanner.close();



	}

	private static void debug(String text){
		System.out.println(TAG + ": " + text);
	}

}
