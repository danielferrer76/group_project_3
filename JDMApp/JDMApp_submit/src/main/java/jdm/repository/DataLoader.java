package jdm.repository;

import jdm.model.*;
import jdm.util.CsvReader;

import java.io.IOException;
import java.util.*;

/**
Loads all CSV files from the PatientX dataset into memory.
 */
public class DataLoader {

    private final String dataDir;

    private List<Patient>        patients        = new ArrayList<>();
    private List<LabResultGroup> groups          = new ArrayList<>();
    private List<LabResult>      labResults      = new ArrayList<>();
    private List<Measurement>    measurements    = new ArrayList<>();
    private List<CmasEntry>      cmasEntries     = new ArrayList<>();

    public DataLoader(String dataDir) {
        this.dataDir = dataDir;
    }

    public void loadAll() throws IOException {
        loadPatients();
        loadGroups();
        loadLabResults();
        loadMeasurements();
        loadCmas();
    }

    // ── Patients ────────────────────────────────────────────────────────────────

    private void loadPatients() throws IOException {
        List<String[]> rows = CsvReader.readAll(dataDir + "/Patient.csv");
        for (int i = 1; i < rows.size(); i++) {       // skip header
            String[] r = rows.get(i);
            if (r.length < 2) continue;
            patients.add(new Patient(r[0], r[1]));
        }
    }

    // ── Lab Result Groups ────────────────────────────────────────────────────────

    private void loadGroups() throws IOException {
        List<String[]> rows = CsvReader.readAll(dataDir + "/LabResultGroup.csv");
        for (int i = 1; i < rows.size(); i++) {
            String[] r = rows.get(i);
            if (r.length < 2) continue;
            groups.add(new LabResultGroup(r[0], r[1]));
        }
    }

    // ── Lab Results (English) ────────────────────────────────────────────────────

    private void loadLabResults() throws IOException {
        // Use the English version: ID, GroupID, PatientID, ResultName, Unit, ResultName_English
        List<String[]> rows = CsvReader.readAll(dataDir + "/LabResults(EN).csv");
        for (int i = 1; i < rows.size(); i++) {
            String[] r = rows.get(i);
            if (r.length < 5) continue;
            String englishName = (r.length >= 6 && !r[5].isBlank()) ? r[5] : r[3];
            String unit        = r[4];
            labResults.add(new LabResult(r[0], r[1], r[2], englishName, unit));
        }
    }

    // ── Measurements ─────────────────────────────────────────────────────────────

    private void loadMeasurements() throws IOException {
        List<String[]> rows = CsvReader.readAll(dataDir + "/Measurement.csv");
        for (int i = 1; i < rows.size(); i++) {
            String[] r = rows.get(i);
            if (r.length < 4) continue;
            measurements.add(new Measurement(r[0], r[1], r[2], r[3]));
        }
    }

    // ── CMAS ─────────────────────────────────────────────────────────────────────

    /**
     * CMAS.csv layout (transposed):
     *   Row 0 : header row — first cell empty, then dates
     *   Row 1 : "CMAS Score > 10", then values per date
     *   Row 2 : "CMAS Score 4-9",  then values per date
     */
    private void loadCmas() throws IOException {
        List<String[]> rows = CsvReader.readAll(dataDir + "/CMAS.csv");
        if (rows.size() < 3) return;

        String[] header    = rows.get(0);   // dates start at index 1
        String[] above10   = rows.get(1);
        String[] score4to9 = rows.get(2);

        // Last column is "points" label — skip it
        int cols = header.length - 1;

        for (int col = 1; col < cols; col++) {
            String date = col < header.length    ? header[col]    : "";
            String s1   = col < above10.length   ? above10[col]   : "";
            String s2   = col < score4to9.length ? score4to9[col] : "";

            if (date.isBlank()) continue;
            if (s1.isBlank() && s2.isBlank()) continue;   // no data for this date

            cmasEntries.add(new CmasEntry(date, s1, s2));
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────────────

    public List<Patient>        getPatients()     { return patients; }
    public List<LabResultGroup> getGroups()       { return groups; }
    public List<LabResult>      getLabResults()   { return labResults; }
    public List<Measurement>    getMeasurements() { return measurements; }
    public List<CmasEntry>      getCmasEntries()  { return cmasEntries; }
}
