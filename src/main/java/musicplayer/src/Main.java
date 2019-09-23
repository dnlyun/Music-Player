/**
 * [Main.java]
 * Has the main method where it will create the Database and Display objects
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

public class Main {

    private static Database database;

    public static void main(String[] args) throws Exception {
        XMLConverter xmlConverter = new XMLConverter();
        database = xmlConverter.XMLtoDatabase();

        Display display = new Display(database);

        //Store database in xml when app closes
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                xmlConverter.dataToXML(database);
            }
        }, "Shutdown-thread"));
    }
}