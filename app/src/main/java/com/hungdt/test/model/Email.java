package com.hungdt.test.model;

import java.io.Serializable;

public class Email implements Serializable {
    private String idTable;
    private String idContact;
    private String email;

    public Email(String idTable, String idContact, String email) {
        this.idTable = idTable;
        this.idContact = idContact;
        this.email = email;
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
}

