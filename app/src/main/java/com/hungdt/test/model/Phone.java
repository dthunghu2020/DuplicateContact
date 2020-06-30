package com.hungdt.test.model;

import java.io.Serializable;

public class Phone implements Serializable {
    private String idTable;
    private String idContact;
    private String phone;

    public Phone(String idTable, String idContact, String phone) {
        this.idTable = idTable;
        this.idContact = idContact;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
