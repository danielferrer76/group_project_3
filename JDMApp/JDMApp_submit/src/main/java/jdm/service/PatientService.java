package jdm.service;

import jdm.model.*;
import jdm.repository.DataLoader;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides querying methods over the loaded dataset.
 * All heavy logic lives here, keeping the CLI layer thin.
 */
public class PatientService {

    private final DataLoader loader;

    // Indexed maps built once for fast lookup
    private final Map<String, LabResultGroup> groupById    = new HashMap<>();
    private final Map<String, LabResult>      resultById   = new HashMap<>();
    private final Map<String, List<Measurement>> measurementsByResultId = new HashMap<>();

    public PatientService(DataLoader loader) {
        this.loader = loader;
        buildIndices();
    }

    private void buildIndices() {
        for (LabResultGroup g : loader.getGroups()) {
            groupById.put(g.getId(), g);
        }
        for (LabResult lr : loader.getLabResults()) {
            resultById.put(lr.getId(), lr);
        }
        for (Measurement m : loader.getMeasurements()) {
            measurementsByResultId
                .computeIfAbsent(m.getLabResultId(), k -> new ArrayList<>())
                .add(m);
        }
    }

    // ── Patient ──────────────────────────────────────────────────────────────────

    public Patient getPatient() {
        return loader.getPatients().isEmpty() ? null : loader.getPatients().get(0);
    }

    // ── Summary stats ────────────────────────────────────────────────────────────

    public int totalLabResults()   { return loader.getLabResults().size(); }
    public int totalMeasurements() { return loader.getMeasurements().size(); }
    public int totalCmasEntries()  { return loader.getCmasEntries().size(); }

    public int totalGroups() { return loader.getGroups().size(); }

    // ── Lab result groups ────────────────────────────────────────────────────────

    public List<LabResultGroup> getAllGroups() {
        return loader.getGroups();
    }

    /** Returns all LabResults belonging to the given group ID. */
    public List<LabResult> getResultsByGroup(String groupId) {
        return loader.getLabResults().stream()
            .filter(lr -> lr.getGroupId().equals(groupId))
            .collect(Collectors.toList());
    }

    /** Returns all measurements for a specific LabResult ID. */
    public List<Measurement> getMeasurementsForResult(String labResultId) {
        return measurementsByResultId.getOrDefault(labResultId, Collections.emptyList());
    }

    // ── CMAS ─────────────────────────────────────────────────────────────────────

    public List<CmasEntry> getCmasHistory() {
        return loader.getCmasEntries();
    }

    /** Returns the most recent CMAS entry (first in the list, as data is newest-first). */
    public CmasEntry getLatestCmas() {
        List<CmasEntry> entries = loader.getCmasEntries();
        return entries.isEmpty() ? null : entries.get(0);
    }

    // ── Search ───────────────────────────────────────────────────────────────────

    /**
     *Searches lab results whose English name contains the keyword (case-insensitive).
     *Returns a map of LabResult
     */
    public Map<LabResult, List<Measurement>> searchByName(String keyword) {
        Map<LabResult, List<Measurement>> result = new LinkedHashMap<>();
        String kw = keyword.toLowerCase();
        for (LabResult lr : loader.getLabResults()) {
            if (lr.getResultName().toLowerCase().contains(kw)) {
                result.put(lr, getMeasurementsForResult(lr.getId()));
            }
        }
        return result;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────────

    public LabResultGroup getGroupById(String id) { return groupById.get(id); }
    public LabResult      getResultById(String id) { return resultById.get(id); }
}
