package jdm.model;

public class Measurement {
    private final String id;
    private final String labResultId;
    private final String dateTime;
    private final String value;

    public Measurement(String id, String labResultId, String dateTime, String value) {
        this.id          = id;
        this.labResultId = labResultId;
        this.dateTime    = dateTime;
        this.value       = value;
    }

    public String getId()          { return id; }
    public String getLabResultId() { return labResultId; }
    public String getDateTime()    { return dateTime; }
    public String getValue()       { return value; }
}
