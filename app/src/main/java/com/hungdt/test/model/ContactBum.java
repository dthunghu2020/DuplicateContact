package com.hungdt.test.model;

public class ContactBum {
    private String IDContact;
    private String Name;
    private int bum;

    public ContactBum(String IDContact, String name) {
        this.IDContact = IDContact;
        Name = name;
    }

    public String getIDContact() {
        return IDContact;
    }

    public void setIDContact(String IDContact) {
        this.IDContact = IDContact;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getBum() {
        return bum;
    }

    public void setBum(int bum) {
        this.bum = bum;
    }
}
