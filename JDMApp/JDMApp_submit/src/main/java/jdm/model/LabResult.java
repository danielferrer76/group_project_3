package jdm.model;

public class LabResult {
    private final String id;
    private final String groupId;
    private final String patientId;
    private final String resultName;   // English name
    private final String unit;

    public LabResult(String id, String groupId, String patientId, String resultName, String unit) {
        this.id         = id;
        this.groupId    = groupId;
        this.patientId  = patientId;
        this.resultName = resultName;
        this.unit       = unit;
    }

    public String getId()         { return id; }
    public String getGroupId()    { return groupId; }
    public String getPatientId()  { return patientId; }
    public String getResultName() { return resultName; }
    public String getUnit()       { return unit; }
}
