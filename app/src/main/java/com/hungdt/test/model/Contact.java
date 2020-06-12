package com.hungdt.test.model;

import java.util.List;

public class Contact implements Comparable<Contact>{

    private String image;
    private String name;
    private String phone;

    public Contact(String image, String name, String phone) {
        this.image = image;
        this.name = name;
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int compareTo(Contact o) {
        return this.name.compareTo(o.name);
    }
}
