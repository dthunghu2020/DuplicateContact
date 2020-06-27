package com.hungdt.test.model;

import java.util.List;

public class Duplicate {
    private String idTable;
    private String contactID;
    private String merged;
    private String name;
    private List<Phone> phone;
    private List<Email> email;
    private String typeMer;

    public Duplicate(String idTable, String contactID, String name, String merged, String typeMer, List<Phone> phone, List<Email> email) {
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

    public String getTypeMer() {
        return typeMer;
    }

    public void setTypeMer(String typeMer) {
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
