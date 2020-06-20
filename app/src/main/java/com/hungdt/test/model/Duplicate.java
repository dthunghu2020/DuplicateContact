package com.hungdt.test.model;

import java.util.List;

public class Duplicate {
    private String id;
    private String contactID;
    private String merger;
    private String mName;
    private String mPhone;
    private String mEmail;
    private String name;
    private List<String> phone;
    private List<String> email;
    private int type = 0;

    public Duplicate(String id, String contactID,String merger,String mName,String mPhone,String mEmail, String name,List<String> phone,List<String> email) {
            this.id = id;
        this.contactID = contactID;
        this.merger = merger;
        this.mName = mName;
        this.mPhone = mPhone;
        this.mEmail = mEmail;
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

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }
    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }
}
