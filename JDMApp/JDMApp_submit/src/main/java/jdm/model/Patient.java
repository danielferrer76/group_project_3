package jdm.model;

public class Patient {
    private final String id;
    private final String name;

    public Patient(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId()   { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "Patient{id='" + id + "', name='" + name + "'}";
    }
}
