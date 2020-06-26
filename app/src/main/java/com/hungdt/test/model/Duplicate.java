package com.hungdt.test.model;

import java.io.Serializable;
import java.util.List;

public class Duplicate {
    private String id;
    private String contactID;
    private String merger;
    private String name;
    private List<Phone> phone;
    private List<Email> email;
    private int type = 0;

    public Duplicate(String id, String contactID, String name,String merger,List<Phone> phone,List<Email> email) {
            this.id = id;
        this.contactID = contactID;
        this.merger = merger;
        this.name = name;
        this.phone = phone;
        this.email = email;
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

    public String getMerger() {
        return merger;
    }

    public void setMerger(String merger) {
        this.merger = merger;
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
