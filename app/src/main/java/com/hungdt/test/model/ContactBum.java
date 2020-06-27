package com.hungdt.test.model;

import java.util.List;

public class ContactBum {
    private String idContact;
    private String nameContact;
    private String name;
    private List<Phone> phones;
    private List<Email> emails;
    private String  bum ;

    public ContactBum(String idContact, String nameContact, String name, List<Phone> phones, List<Email> emails) {
        this.idContact = idContact;
        this.nameContact = nameContact;
        this.name = name;
        this.phones = phones;
        this.emails = emails;
    }

    public String getNameContact() {
        return nameContact;
    }

    public void setNameContact(String nameContact) {
        this.nameContact = nameContact;
    }

    public String getIdContact() {
        return idContact;
    }

    public void setIdContact(String idContact) {
        this.idContact = idContact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public String getBum() {
        return bum;
    }

    public void setBum(String bum) {
        this.bum = bum;
    }
}
