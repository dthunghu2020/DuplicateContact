package com.hungdt.test.model;

import java.util.List;


public class Contact implements Comparable<Contact> {

    private String id;
    private String idContact;
    private String name;
    private String image;
    private String lastCT;
    private String deleted;
    private List<String> phone;
    private List<Account> account;
    private List<String> email;

    public Contact(String id, String idContact, String name, String image, String lastCT,String deleted, List<String> phone, List<Account> account, List<String> email) {
        this.id = id;
        this.idContact = idContact;
        this.image = image;
        this.name = name;
        this.deleted = deleted;
        this.lastCT = lastCT;
        this.phone = phone;
        this.account = account;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdContact() {
        return idContact;
    }

    public void setIdContact(String idContact) {
        this.idContact = idContact;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastCT() {
        return lastCT;
    }

    public void setLastCT(String lastCT) {
        this.lastCT = lastCT;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public List<Account> getAccount() {
        return account;
    }

    public void setAccount(List<Account> account) {
        this.account = account;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    @Override
    public int compareTo(Contact o) {
        return this.name.compareTo(o.name);
    }
}
