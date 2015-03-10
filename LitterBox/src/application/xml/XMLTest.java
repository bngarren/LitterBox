package application.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.model.Journal;

public class XMLTest {

	public static void main(String[] args) throws XMLStreamException{

		List<Journal> journals = null;
		Journal currentJournal = null;
		String nodeContent = null;


		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory.createXMLStreamReader(ClassLoader.getSystemResourceAsStream("application/xml/testxml.xml"));

		while (reader.hasNext()){

			int event = reader.next();

			switch (event){

			case XMLStreamConstants.START_ELEMENT:

				if (reader.getLocalName().equals("journals")){
					journals = new ArrayList<Journal>();
				}
				if (reader.getLocalName().equals("journal")){
					currentJournal = new Journal();
					currentJournal.setId(Integer.valueOf(reader.getAttributeValue(0)));
				}
				if (reader.getLocalName().equals("title")){
					currentJournal.setAbbreviation(reader.getAttributeValue(0));
				}

				break;

			case XMLStreamConstants.CHARACTERS:
				nodeContent = reader.getText().trim();
				break;

			case XMLStreamConstants.END_ELEMENT:

				switch (reader.getLocalName()){

				case "journal":
					journals.add(currentJournal);
					break;

				case "title":
					currentJournal.setTitle(nodeContent);
					break;

				}

				break;

			} // end switch(event)

		} // end while(reader.hasNext())

		journals.forEach(j -> {
			System.out.printf("%s - %s (%s)\n"
					+ "", j.getId(), j.getTitle(), j.getAbbreviation());
		});

	} // end main

}
