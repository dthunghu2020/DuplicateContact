package com.hungdt.test;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class ContactConfig {
    private static ContactConfig _instance;
    private FirebaseRemoteConfig config;

    private ContactConfig() {

    }

    public FirebaseRemoteConfig getConfig() {
        return this.config;
    }

    void setConfig(FirebaseRemoteConfig config) {
        this.config = config;
    }

    public static ContactConfig getInstance() {
        if (_instance == null) {
            _instance = new ContactConfig();
        }
        return _instance;
    }
}