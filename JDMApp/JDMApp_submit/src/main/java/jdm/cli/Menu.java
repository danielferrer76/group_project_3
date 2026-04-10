package jdm.cli;

import jdm.model.*;
import jdm.service.PatientService;

import java.util.*;

/**
 *CLI-based interface
 *
 *Menu options:
 * 1. Patient summary
 * 2. Browse lab results by group
 * 3. View CMAS history
 * 4. Search lab results by name
 * 0. Exit
 */
public class Menu {

    private static final String SEP  = "-".repeat(60);
    private static final String SEP2 = "=".repeat(60);

    private final PatientService service;
    private final Scanner        scanner;

    public Menu(PatientService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = readLine("Select: ").trim();
            System.out.println();
            switch (choice) {
                case "1" -> showPatientSummary();
                case "2" -> browseLabGroups();
                case "3" -> showCmasHistory();
                case "4" -> searchLabResults();
                case "0" -> running = false;
                default  -> System.out.println("Invalid option. Try again.\n");
            }
        }
        System.out.println("\nGoodbye.");
    }

    // ── Banner ───────────────────────────────────────────────────────────────────

    private void printBanner() {
        System.out.println(SEP2);
        System.out.println("  JDM Electronic Patient Dossier");
        System.out.println("  Juvenile Dermatomyositis PatientX Dataset");
        System.out.println(SEP2);
        System.out.println();
    }

    private void printMainMenu() {
        System.out.println(SEP);
        System.out.println("  MAIN MENU");
        System.out.println(SEP);
        System.out.println("  1. Patient summary");
        System.out.println("  2. Browse lab results by group");
        System.out.println("  3. View CMAS history");
        System.out.println("  4. Search lab results by name");
        System.out.println("  0. Exit");
        System.out.println(SEP);
    }

    // ── Option 1: Patient summary ────────────────────────────────────────────────

    private void showPatientSummary() {
        Patient p = service.getPatient();
        System.out.println("  PATIENT SUMMARY");
        System.out.println(SEP);
        if (p == null) {
            System.out.println("  No patient data found.");
        } else {
            System.out.printf("  Name      : %s%n", p.getName());
            System.out.printf("  Patient ID: %s%n", p.getId());
            System.out.println();
            System.out.printf("  Lab result types   : %d%n", service.totalLabResults());
            System.out.printf("  Total measurements : %d%n", service.totalMeasurements());
            System.out.printf("  Lab result groups  : %d%n", service.totalGroups());
            System.out.printf("  CMAS assessments   : %d%n", service.totalCmasEntries());

            CmasEntry latest = service.getLatestCmas();
            if (latest != null) {
                System.out.println();
                System.out.printf("  Latest CMAS (%s): %s%n",
                    latest.getDate(), latest.getDisplayScore());
            }
        }
        System.out.println();
    }

    // ── Option 2: Browse lab groups ──────────────────────────────────────────────

    private void browseLabGroups() {
        List<LabResultGroup> groups = service.getAllGroups();
        System.out.println("  LAB RESULT GROUPS");
        System.out.println(SEP);

        for (int i = 0; i < groups.size(); i++) {
            System.out.printf("  %2d. %s%n", i + 1, groups.get(i).getGroupName());
        }
        System.out.println();

        String input = readLine("  Select group number (or 0 to go back): ").trim();
        int idx;
        try {
            idx = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            System.out.println("  Invalid input.\n");
            return;
        }
        if (idx < 0 || idx >= groups.size()) {
            System.out.println();
            return;
        }

        LabResultGroup selected = groups.get(idx);
        List<LabResult> results = service.getResultsByGroup(selected.getId());

        System.out.println();
        System.out.printf("  GROUP: %s  (%d result types)%n", selected.getGroupName(), results.size());
        System.out.println(SEP);

        for (int i = 0; i < results.size(); i++) {
            LabResult lr = results.get(i);
            int count = service.getMeasurementsForResult(lr.getId()).size();
            String unit = lr.getUnit().isBlank() ? "" : " [" + lr.getUnit() + "]";
            System.out.printf("  %3d. %-35s %s  (%d measurements)%n",
                i + 1, lr.getResultName(), unit, count);
        }
        System.out.println();

        String input2 = readLine("  Select result to view measurements (or 0 to go back): ").trim();
        int idx2;
        try {
            idx2 = Integer.parseInt(input2) - 1;
        } catch (NumberFormatException e) {
            System.out.println();
            return;
        }
        if (idx2 < 0 || idx2 >= results.size()) {
            System.out.println();
            return;
        }

        showMeasurements(results.get(idx2));
    }

    private void showMeasurements(LabResult lr) {
        List<Measurement> measurements = service.getMeasurementsForResult(lr.getId());
        String unit = lr.getUnit().isBlank() ? "" : " (" + lr.getUnit() + ")";
        System.out.println();
        System.out.printf("  MEASUREMENTS: %s%s%n", lr.getResultName(), unit);
        System.out.println(SEP);

        if (measurements.isEmpty()) {
            System.out.println("  No measurements recorded.");
        } else {
            System.out.printf("  %-25s  %s%n", "Date/Time", "Value");
            System.out.println("  " + "-".repeat(40));
            for (Measurement m : measurements) {
                System.out.printf("  %-25s  %s%n", m.getDateTime(), m.getValue());
            }
        }
        System.out.println();
    }

    // ── Option 3: CMAS history ───────────────────────────────────────────────────

    private void showCmasHistory() {
        List<CmasEntry> entries = service.getCmasHistory();
        System.out.println("  CMAS ASSESSMENT HISTORY  (newest first)");
        System.out.println(SEP);
        System.out.printf("  %-15s  %s%n", "Date", "Score");
        System.out.println("  " + "-".repeat(40));

        if (entries.isEmpty()) {
            System.out.println("  No CMAS data found.");
        } else {
            for (CmasEntry e : entries) {
                System.out.printf("  %-15s  %s%n", e.getDate(), e.getDisplayScore());
            }
        }
        System.out.println();
    }

    // ── Option 4: Search ─────────────────────────────────────────────────────────

    private void searchLabResults() {
        String keyword = readLine("  Enter search keyword: ").trim();
        if (keyword.isBlank()) {
            System.out.println("  No keyword entered.\n");
            return;
        }

        Map<LabResult, List<Measurement>> results = service.searchByName(keyword);
        System.out.println();
        System.out.printf("  SEARCH RESULTS for \"%s\"  (%d found)%n", keyword, results.size());
        System.out.println(SEP);

        if (results.isEmpty()) {
            System.out.println("  No lab results matched your search.");
        } else {
            for (Map.Entry<LabResult, List<Measurement>> entry : results.entrySet()) {
                LabResult lr = entry.getKey();
                List<Measurement> measurements = entry.getValue();
                LabResultGroup group = service.getGroupById(lr.getGroupId());
                String groupName = group != null ? group.getGroupName() : "Unknown";
                String unit = lr.getUnit().isBlank() ? "" : " [" + lr.getUnit() + "]";

                System.out.printf("  • %-35s %s  |  Group: %-20s  |  %d measurements%n",
                    lr.getResultName(), unit, groupName, measurements.size());

                // Show last 3 measurements as a preview
                int preview = Math.min(3, measurements.size());
                for (int i = 0; i < preview; i++) {
                    Measurement m = measurements.get(i);
                    System.out.printf("      %-25s → %s%n", m.getDateTime(), m.getValue());
                }
                if (measurements.size() > preview) {
                    System.out.printf("      ... and %d more%n", measurements.size() - preview);
                }
                System.out.println();
            }
        }
        System.out.println();
    }

    // ── Helper ───────────────────────────────────────────────────────────────────

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.hasNextLine() ? scanner.nextLine() : "";
    }
}
