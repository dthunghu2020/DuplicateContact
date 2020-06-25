package com.hungdt.test.model;

import java.io.Serializable;
import java.util.List;


public class Contact  implements Serializable, Comparable<Contact> {

    private String idTable;
    private String idContact;
    private String name;
    private String image;
    private String mContact;
    private String mName;
    private String mPhone;
    private String mEmail;
    private String merger;
    private String father;
    private String deleted;
    private List<Phone> phones;
    private List<Account> accounts;
    private List<Email> emails;
    private boolean isTicked = false;
    private int type = 0;

    public Contact(String idTable, String idContact, String name, String image,String merger,String mContact,String mName,String mPhone,String mEmail,String father,String deleted, List<Phone> phones, List<Account> accounts, List<Email> emails) {
        this.idTable = idTable;
        this.idContact = idContact;
        this.image = image;
        this.name = name;
        this.deleted = deleted;
        this.mName = mName;
        this.mEmail = mEmail;
        this.mPhone = mPhone;
        this.merger = merger;
        this.father = father;
        this.phones = phones;
        this.accounts = accounts;
        this.emails = emails;
        this.mContact = mContact;
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

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
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

    public String getmContact() {
        return mContact;
    }

    public void setmContact(String mContact) {
        this.mContact = mContact;
    }

    @Override
    public int compareTo(Contact o) {
        return this.name.compareTo(o.name);
    }
}
