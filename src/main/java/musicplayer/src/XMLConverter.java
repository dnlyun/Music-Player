/**
 * [XMLConverter.java]
 * Output Database object to XML file and convert XML file into Database object
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLConverter {

    /**
     * XMLtoDatabase
     * converts the xml to the database of songs and playlists
     * @return the new database with all of the songs and playlists
     */
    public Database XMLtoDatabase() {
        try {
            File file = new File("Database.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Database.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Database) jaxbUnmarshaller.unmarshal(file);
        } catch (Exception e) {
            return new Database();
        }
    }

    /**
     * dataToXML
     * converts the database to an XML file to be used after the program closes
     * @param database Database object
     */
    public void dataToXML(Database database) {
        try {
            File file = new File("Database.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Database.class);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //jaxbMarshaller.marshal(database, System.out);
            jaxbMarshaller.marshal(database, file);
        } catch (Exception e) {}
    }
}
