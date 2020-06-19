package com.hungdt.test.model;

import java.io.Serializable;
import java.util.List;


public class Contact  implements Serializable, Comparable<Contact> {

    private String id;
    private String idContact;
    private String name;
    private String image;
    private String merger;
    private String father;
    private String deleted;
    private List<String> phone;
    private List<Account> account;
    private List<String> email;
    private boolean isTicked = false;
    private int type = 0;

    public Contact(String id, String idContact, String name, String image,String merger,String father,String deleted, List<String> phone, List<Account> account, List<String> email) {
        this.id = id;
        this.idContact = idContact;
        this.image = image;
        this.name = name;
        this.deleted = deleted;
        this.merger = merger;
        this.father = father;
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

    public boolean isTicked() {
        return isTicked;
    }

    public void setTicked(boolean ticked) {
        isTicked = ticked;
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

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    @Override
    public int compareTo(Contact o) {
        return this.name.compareTo(o.name);
    }
}
