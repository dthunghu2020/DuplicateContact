package com.hungdt.test.model;

public class Duplicate {
    private String id;
    private String contactID;
    private String name;
    private int type = 0;

    public Duplicate(String id, String contactID, String name) {
        this.id = id;
        this.contactID = contactID;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
