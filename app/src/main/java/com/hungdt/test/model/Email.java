package com.hungdt.test.model;

public class Email {
    String idTable;
    String idContact;
    String email;
    String merger;

    public Email(String idTable, String idContact, String email, String merger) {
        this.idTable = idTable;
        this.idContact = idContact;
        this.email = email;
        this.merger = merger;
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

    public String getMerger() {
        return merger;
    }

    public void setMerger(String merger) {
        this.merger = merger;
    }
}

