package com.hungdt.test.model;

import java.util.List;

public class Duplicate {
    private String idTable;
    private String contactID;
    private String merged;
    private String name;
    private List<Phone> phone;
    private List<Email> email;
    private int typeMer;

    public Duplicate(String idTable, String contactID, String name, String merged, int typeMer, List<Phone> phone, List<Email> email) {
        this.idTable = idTable;
        this.contactID = contactID;
        this.merged = merged;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.typeMer = typeMer;
    }

    public String getIdTable() {
        return idTable;
    }

    public void setIdTable(String idTable) {
        this.idTable = idTable;
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

    public int getTypeMer() {
        return typeMer;
    }

    public void setTypeMer(int typeMer) {
        this.typeMer = typeMer;
    }

    public String getMerged() {
        return merged;
    }

    public void setMerged(String merger) {
        this.merged = merged;
    }

    public List<Phone> getPhone() {
        return phone;
    }

    public void setPhone(List<Phone> phone) {
        this.phone = phone;
    }

    public List<Email> getEmail() {
        return email;
    }

    public void setEmail(List<Email> email) {
        this.email = email;
    }


}
