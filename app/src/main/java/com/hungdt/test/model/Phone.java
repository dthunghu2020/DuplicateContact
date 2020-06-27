package com.hungdt.test.model;

import java.io.Serializable;

public class Phone implements Serializable {
    private String idTable;
    private String idContact;
    private String phone;
    private String typeMerger;
    private String mPhone;

    public Phone(String idTable, String idContact, String phone, String typeMerger, String mPhone) {
        this.idTable = idTable;
        this.idContact = idContact;
        this.phone = phone;
        this.typeMerger = typeMerger;
        this.mPhone = mPhone;
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

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getTypeMerger() {
        return typeMerger;
    }

    public void setTypeMerger(String typeMerger) {
        this.typeMerger = typeMerger;
    }
}
