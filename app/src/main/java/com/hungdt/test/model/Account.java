package com.hungdt.test.model;

import java.io.Serializable;

public class Account implements Serializable {
    private String accountName;
    private String accountType;

    public Account( String accountName, String accountType) {
        this.accountName = accountName;
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
