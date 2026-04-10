package jdm;

import jdm.cli.Menu;
import jdm.repository.DataLoader;
import jdm.service.PatientService;

/**
 * Entry point for the JDM Electronic Patient Dossier.
 *
 * Usage: java -cp . jdm.Main <path-to-PatientX-folder>
 * Example: java -cp out jdm.Main data/PatientX
 */
public class Main {

    public static void main(String[] args) {
        String dataDir = args.length > 0 ? args[0] : "PatientX";

        System.out.println("Loading dataset from: " + dataDir);

        DataLoader loader = new DataLoader(dataDir);
        try {
            loader.loadAll();
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            System.err.println("Make sure the PatientX folder is at: " + dataDir);
            System.exit(1);
        }

        PatientService service = new PatientService(loader);
        Menu menu = new Menu(service);
        menu.run();
    }
}
