package jdm.model;

public class LabResultGroup {
    private final String id;
    private final String groupName;

    public LabResultGroup(String id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    public String getId()        { return id; }
    public String getGroupName() { return groupName; }
}
