package com.hungdt.test.utils;

public interface KEY {
    String ID = "id";
    String FALSE = "false";
    String IMAGE = "image";
    String NAME = "name";
    String PHONE = "phone";
    String ACCOUNT = "account";
    String EMAIL = "email";

    String LINK_PATTERN = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
    String PHONE_PATTERN = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$";
    String EMAIL_PATTERN = "^(Email:|email:|EMAIL:)?\\s*[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}\\s*\\n.*\\s*\\n.*\\s*$";
    String ADDRESS_PATTERN = "^\\d+\\s[A-z]+\\s[A-z]+$";
    String WIFI_PATTERN = "^(WIFI:S:)[0-9A-Za-z]*(;T:)(WPA|WEP|nopass)(;P:)[0-9A-Za-z]*(;H:)(true|false);;$";
    String SMS_PATTERN = "^.*[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*(:|\\n)?.*$";
    String CALENDAR_PATTERN = "^\\s*(BEGIN:)[A-Z]*(\\n\\s*.*)*\\n(\\s)*END:[A-Z]*(\\s)*\\n*(\\s)*$";

}
