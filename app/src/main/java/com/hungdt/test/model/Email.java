package com.hungdt.test.model;

import java.io.Serializable;

public class Email implements Serializable {
    private String idTable;
    private String idContact;
    private String email;
    private String typeMerger;
    private String mEmail;

    public Email(String idTable, String idContact, String email, String typeMerger, String mEmail) {
        this.idTable = idTable;
        this.idContact = idContact;
        this.email = email;
        this.typeMerger = typeMerger;
        this.mEmail = mEmail;
    }

    public String getIdTable() {
        return idTable;
    }

    public void setIdTable(String idTable) {
        this.idTable = idTable;
    }

    public String getIdContact() {
        return idContact;
    }

    public void setIdContact(String idContact) {
        this.idContact = idContact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getTypeMerger() {
        return typeMerger;
    }

    public void setTypeMerger(String typeMerger) {
        this.typeMerger = typeMerger;
    }
}

