package com.hungdt.test.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hungdt.test.model.Account;
import com.hungdt.test.model.Contact;
import com.hungdt.test.model.Duplicate;
import com.hungdt.test.utils.KEY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Contact.db";

    public static final String TABLE_CONTACT = "TB_CONTACT";
    public static final String COLUMN_TABLE_CONTACT_ID = "CONTACT_TABLE_ID";
    public static final String COLUMN_CONTACT_ID = "CONTACT_ID";
    public static final String COLUMN_CONTACT_NAME = "CONTACT_NAME";
    public static final String COLUMN_CONTACT_IMAGE = "CONTACT_IMAGE";
    public static final String COLUMN_MERGER_NAME = "CONTACT_MERGER_N";
    public static final String COLUMN_MERGER_CONTACT = "CONTACT_MERGER_CONTACT";
    public static final String COLUMN_MERGER_PHONE = "CONTACT_MERGER_P";
    public static final String COLUMN_MERGER_EMAIL = "CONTACT_MERGER_E";
    public static final String COLUMN_CONTACT_MERGER = "CONTACT_MERGER";
    public static final String COLUMN_CONTACT_MERGER_F = "CONTACT_MERGER_FATHER";
    public static final String COLUMN_CONTACT_NO_NAME = "CONTACT_NO_NAME";
    public static final String COLUMN_CONTACT_NO_PHONE = "CONTACT_NO_PHONE";
    public static final String COLUMN_CONTACT_NO_EMAIL = "CONTACT_NO_EMAIL";
    public static final String COLUMN_CONTACT_DELETED = "CONTACT_DELETED";

    public static final String TABLE_CONTACT_ACCOUNT = "TB_CONTACT_ACCOUNT";
    public static final String COLUMN_CONTACT_ACCOUNT_ID = "CONTACT_ACCOUNT_ID";
    public static final String COLUMN_ID_CONTACT_ACCOUNT = "CONTACT_ID";
    public static final String COLUMN_CONTACT_ACCOUNT_NAME = "CONTACT_ACCOUNT_NAME";
    public static final String COLUMN_CONTACT_ACCOUNT_TYPE = "CONTACT_ACCOUNT_TYPE";

    public static final String TABLE_CONTACT_PHONE = "TB_CONTACT_PHONE";
    public static final String COLUMN_CONTACT_PHONE_ID = "CONTACT_PHONE_ID";
    public static final String COLUMN_ID_CONTACT_PHONE = "CONTACT_ID";
    public static final String COLUMN_ID_CONTACT_IDP = "CONTACT_IDP";
    public static final String COLUMN_CONTACT_PHONE_M = "CONTACT_PM";
    public static final String COLUMN_PHONE_MERGER_N = "MERGER_NAME";
    public static final String COLUMN_PHONE_MERGER_P = "MERGER_PHONE";
    public static final String COLUMN_PHONE_MERGER_E = "MERGER_EMAIL";
    public static final String COLUMN_CONTACT_PHONE_NAME = "CONTACT_PHONE_NAME";

    public static final String TABLE_CONTACT_EMAIL = "TB_CONTACT_EMAIL";
    public static final String COLUMN_CONTACT_EMAIL_ID = "CONTACT_EMAIL_ID";
    public static final String COLUMN_ID_CONTACT_EMAIL = "CONTACT_ID";
    public static final String COLUMN_ID_CONTACT_IDE = "CONTACT_IDE";
    public static final String COLUMN_CONTACT_EMAIL_M = "CONTACT_EM";
    public static final String COLUMN_EMAIL_MERGER_N = "MERGER_NAME";
    public static final String COLUMN_EMAIL_MERGER_P = "MERGER_PHONE";
    public static final String COLUMN_EMAIL_MERGER_E = "MERGER_EMAIL";
    public static final String COLUMN_CONTACT_EMAIL_NAME = "CONTACT_EMAIL_NAME";

    public static final String SQL_CREATE_TABLE_CONTACT = "CREATE TABLE " + TABLE_CONTACT + "("
            + COLUMN_TABLE_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CONTACT_ID + " TEXT NOT NULL, "
            + COLUMN_CONTACT_NAME + " TEXT NOT NULL, "
            + COLUMN_CONTACT_IMAGE + " TEXT NOT NULL, "
            + COLUMN_MERGER_CONTACT + " TEXT NOT NULL, "
            + COLUMN_MERGER_NAME + " TEXT NOT NULL, "
            + COLUMN_MERGER_PHONE + " TEXT NOT NULL, "
            + COLUMN_MERGER_EMAIL + " TEXT NOT NULL, "
            + COLUMN_CONTACT_MERGER + " TEXT NOT NULL, "
            + COLUMN_CONTACT_MERGER_F + " TEXT NOT NULL, "
            + COLUMN_CONTACT_NO_NAME + " TEXT NOT NULL, "
            + COLUMN_CONTACT_NO_PHONE + " TEXT NOT NULL, "
            + COLUMN_CONTACT_NO_EMAIL + " TEXT NOT NULL, "
            + COLUMN_CONTACT_DELETED + " TEXT NOT NULL " + ");";

    public static final String SQL_CREATE_TABLE_CONTACT_ACCOUNT = "CREATE TABLE " + TABLE_CONTACT_ACCOUNT + "("
            + COLUMN_CONTACT_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID_CONTACT_ACCOUNT + " TEXT NOT NULL, "
            + COLUMN_CONTACT_ACCOUNT_NAME + " TEXT NOT NULL, "
            + COLUMN_CONTACT_ACCOUNT_TYPE + " TEXT NOT NULL " + ");";

    public static final String SQL_CREATE_TABLE_CONTACT_PHONE = "CREATE TABLE " + TABLE_CONTACT_PHONE + "("
            + COLUMN_CONTACT_PHONE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID_CONTACT_PHONE + " TEXT NOT NULL, "
            + COLUMN_ID_CONTACT_IDP + " TEXT NOT NULL, "
            + COLUMN_CONTACT_PHONE_M + " TEXT NOT NULL, "
            + COLUMN_PHONE_MERGER_N + " TEXT NOT NULL, "
            + COLUMN_PHONE_MERGER_P + " TEXT NOT NULL, "
            + COLUMN_PHONE_MERGER_E + " TEXT NOT NULL, "
            + COLUMN_CONTACT_PHONE_NAME + " TEXT NOT NULL " + ");";

    public static final String SQL_CREATE_TABLE_CONTACT_EMAIL = "CREATE TABLE " + TABLE_CONTACT_EMAIL + "("
            + COLUMN_CONTACT_EMAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID_CONTACT_EMAIL + " TEXT NOT NULL, "
            + COLUMN_ID_CONTACT_IDE + " TEXT NOT NULL, "
            + COLUMN_CONTACT_EMAIL_M + " TEXT NOT NULL, "
            + COLUMN_EMAIL_MERGER_N + " TEXT NOT NULL, "
            + COLUMN_EMAIL_MERGER_P + " TEXT NOT NULL, "
            + COLUMN_EMAIL_MERGER_E + " TEXT NOT NULL, "
            + COLUMN_CONTACT_EMAIL_NAME + " TEXT NOT NULL " + ");";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public void addContact(String idContact, String name, String image, String merger,String mContact, String mName, String mPhone, String mEmail, String father, String deleted, String noName, String noPhone, String noEmail) {

        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_ID, idContact);
        values.put(COLUMN_CONTACT_NAME, name);
        values.put(COLUMN_CONTACT_IMAGE, image);
        values.put(COLUMN_MERGER_CONTACT, mContact);
        values.put(COLUMN_MERGER_NAME, mName);
        values.put(COLUMN_MERGER_PHONE, mPhone);
        values.put(COLUMN_MERGER_EMAIL, mEmail);
        values.put(COLUMN_CONTACT_MERGER, merger);
        values.put(COLUMN_CONTACT_MERGER, merger);
        values.put(COLUMN_CONTACT_MERGER_F, father);
        values.put(COLUMN_CONTACT_NO_NAME, noName);
        values.put(COLUMN_CONTACT_NO_PHONE, noPhone);
        values.put(COLUMN_CONTACT_NO_EMAIL, noEmail);
        values.put(COLUMN_CONTACT_DELETED, deleted);
        database.insert(TABLE_CONTACT, null, values);
        database.close();
    }

    public String getLastContactID(String id) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        String icContact = null;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_CONTACT_ID)).equals(id)) {
                    icContact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID));
                }

                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return icContact;
    }

    public String getLastID() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACT + " ORDER BY " + COLUMN_TABLE_CONTACT_ID + " DESC LIMIT 1", null);
        String lastID = "";
        while (cursor.moveToNext()) {
            lastID = cursor.getString(0);
        }
        cursor.close();
        db.close();

        return lastID;
    }

    public void addAccount(String id, String account, String type) {
        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_ACCOUNT, id);
        values.put(COLUMN_CONTACT_ACCOUNT_NAME, account);
        values.put(COLUMN_CONTACT_ACCOUNT_TYPE, type);
        database.insert(TABLE_CONTACT_ACCOUNT, null, values);
        database.close();
    }

    public void addPhone(String id, String contactId, String merger, String mN, String mP, String mE, String phone) {
        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_PHONE, id);
        values.put(COLUMN_ID_CONTACT_IDP, contactId);
        values.put(COLUMN_CONTACT_PHONE_M, merger);
        values.put(COLUMN_PHONE_MERGER_N, mN);
        values.put(COLUMN_PHONE_MERGER_P, mP);
        values.put(COLUMN_PHONE_MERGER_E, mE);
        values.put(COLUMN_CONTACT_PHONE_NAME, phone);
        database.insert(TABLE_CONTACT_PHONE, null, values);
        database.close();
    }

    public void addEmail(String id, String contactId, String merger, String mN, String mP, String mE, String email) {
        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_EMAIL, id);
        values.put(COLUMN_ID_CONTACT_IDE, contactId);
        values.put(COLUMN_CONTACT_EMAIL_M, merger);
        values.put(COLUMN_EMAIL_MERGER_N, mN);
        values.put(COLUMN_EMAIL_MERGER_P, mP);
        values.put(COLUMN_EMAIL_MERGER_E, mE);
        values.put(COLUMN_CONTACT_EMAIL_NAME, email);
        database.insert(TABLE_CONTACT_EMAIL, null, values);
        database.close();
    }


    public List<Contact> getAllContact() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (!cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_DELETED)).equals(KEY.TRUE)) {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_CONTACT_ID));
                    contacts.add(new Contact(id,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER_F)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_DELETED)),
                            getPhone(id),
                            getAccount(id),
                            getEmail(id)));
                    cursor.moveToNext();
                }
            }
        }
        cursor.close();
        db.close();
        return contacts;
    }

    public Contact getContact(String id) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        Contact contact = null;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_CONTACT_ID)).equals(id)) {
                    contact = new Contact(id, cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER_F)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_DELETED)),
                            getPhone(id), getAccount(id), getEmail(id));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return contact;
    }


    public List<Contact> getContactMergedF() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER_F)).equals(KEY.TRUE)) {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_CONTACT_ID));
                    contacts.add(new Contact(id,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER_F)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_DELETED)),
                            getPhone(id), getAccount(id), getEmail(id)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return contacts;
    }

    public List<Contact> getContactMerged(String type) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER)).equals(type)) {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_CONTACT_ID));
                    contacts.add(new Contact(id,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER_F)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_DELETED)),
                            getPhone(id), getAccount(id), getEmail(id)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return contacts;
    }

    public String getNumberContactNoName() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        int count = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NO_NAME)).equals("true")) {
                    count++;
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return String.valueOf(count);
    }

    public List<Contact> getContactNoName() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NO_NAME)).equals("true")) {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_CONTACT_ID));
                    contacts.add(new Contact(id,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER_F)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_DELETED)),
                            getPhone(id), getAccount(id), getEmail(id)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return contacts;
    }

    public String getNumberContactNoPhone() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        int count = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NO_PHONE)).equals("true")) {
                    count++;
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return String.valueOf(count);
    }

    public List<Contact> getContactNoPhone() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NO_PHONE)).equals("true")) {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_CONTACT_ID));
                    contacts.add(new Contact(id,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER_F)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_DELETED)),
                            getPhone(id), getAccount(id), getEmail(id)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return contacts;
    }

    public String getNumberContactNoEmail() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        int count = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NO_EMAIL)).equals("true")) {
                    count++;
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return String.valueOf(count);
    }

    public List<Contact> getContactNoEmail() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NO_EMAIL)).equals("true")) {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_CONTACT_ID));
                    contacts.add(new Contact(id,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER_F)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_DELETED)),
                            getPhone(id), getAccount(id), getEmail(id)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return contacts;
    }

    public Contact getDuplicateContact(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        Contact contacts = null;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)).equals(idContact)) {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_CONTACT_ID));
                    contacts = (new Contact(id,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER_F)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_DELETED)),
                            getPhone(id), getAccount(id), getEmail(id)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return contacts;
    }

    public List<String> getPhone(String contactID) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_PHONE), null);
        List<String> phones = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_PHONE)).equals(contactID)) {
                    phones.add(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_PHONE_NAME)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return phones;
    }


    public List<Duplicate> getDupContact() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Duplicate> duplicates = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if(cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)).equals(KEY.FALSE)){
                    duplicates.add(new Duplicate(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            getPhone(cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_CONTACT_ID))),
                            getEmail(cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_CONTACT_ID)))));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return duplicates;
    }

    public List<Duplicate> getDupName() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Duplicate> duplicates = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                    if(cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)).equals(KEY.FALSE)){
                        duplicates.add(new Duplicate(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_MERGER)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),null,null));
                    }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return duplicates;
    }

    public List<Duplicate> getDupPhone() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_PHONE), null);
        List<Duplicate> duplicates = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
              if(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_MERGER_P)).equals(KEY.FALSE)&&cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_PHONE_M)).equals(KEY.FALSE)){
                  duplicates.add(new Duplicate(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_PHONE_ID)),
                          cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_IDP)),
                          cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_PHONE_M)),
                          cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_MERGER_N)),
                          cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_MERGER_P)),
                          cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_MERGER_E)),
                          cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_PHONE_NAME)),null,null));
              }
              cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return duplicates;
    }

    public List<Duplicate> getDupEmail() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_EMAIL), null);
        List<Duplicate> duplicates = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_MERGER_E)).equals(KEY.FALSE)&&cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_EMAIL_M)).equals(KEY.FALSE)){
                    duplicates.add(new Duplicate(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_EMAIL_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_IDE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_EMAIL_M)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_MERGER_N)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_MERGER_P)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_MERGER_E)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_EMAIL_NAME)),null,null));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return duplicates;
    }

    public List<Account> getAccount(String contactID) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_ACCOUNT), null);
        List<Account> accounts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_ACCOUNT)).equals(contactID)) {
                    accounts.add(new Account(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ACCOUNT_NAME)), cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ACCOUNT_TYPE))));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return accounts;
    }

    public List<String> getEmail(String contactID) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_EMAIL), null);
        List<String> phones = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_ACCOUNT)).equals(contactID)) {
                    phones.add(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_EMAIL_NAME)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return phones;
    }

    public void deleteAllContact() {
        SQLiteDatabase db = instance.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CONTACT);
        db.execSQL("delete from " + TABLE_CONTACT_PHONE);
        db.execSQL("delete from " + TABLE_CONTACT_EMAIL);
        db.execSQL("delete from " + TABLE_CONTACT_ACCOUNT);
        db.close();
    }

    public void deleteContact(String id) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TABLE_CONTACT_ID, id);
        db.delete(TABLE_CONTACT, COLUMN_TABLE_CONTACT_ID + "='" + id + "'", new String[]{});

        db.close();
        deleteAccount(id);
        deletePhone(id);
        deleteEmail(id);
    }

    public void updateDisableContact(String id) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_DELETED, KEY.TRUE);
        values.put(COLUMN_CONTACT_MERGER, KEY.FALSE);
        values.put(COLUMN_CONTACT_MERGER_F, KEY.FALSE);
        db.update(TABLE_CONTACT, values, COLUMN_TABLE_CONTACT_ID + "='" + id + "'", null);
        db.close();
    }

    public void deletePhone(String id) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_PHONE_ID, id);
        db.delete(TABLE_CONTACT_PHONE, COLUMN_CONTACT_PHONE_ID + "='" + id + "'", new String[]{});

        db.close();

    }

    public void deleteEmail(String id) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_EMAIL_ID, id);
        db.delete(TABLE_CONTACT_EMAIL, COLUMN_CONTACT_EMAIL_ID + "='" + id + "'", new String[]{});

        db.close();

    }


    public void deleteAccount(String id) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_ACCOUNT_ID, id);
        db.delete(TABLE_CONTACT_ACCOUNT, COLUMN_CONTACT_ACCOUNT_ID + "='" + id + "'", new String[]{});

        db.close();
    }

    public void updateContactMerger(String id, String idContact,String mC, String mN, String mP, String mE, String typeMerger) {
        Log.e("123123123", "updateContactMerger: "+mN+mE+mN);
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_MERGER, typeMerger);
        values.put(COLUMN_MERGER_CONTACT, mC);
        values.put(COLUMN_MERGER_NAME, mN);
        values.put(COLUMN_MERGER_PHONE, mP);
        values.put(COLUMN_MERGER_EMAIL, mE);
        db.update(TABLE_CONTACT, values, COLUMN_TABLE_CONTACT_ID + "='" + id + "'", null);
        db.close();
        updatePhone(idContact, mN, mP, mE);
        updateEmail(idContact, mN, mP, mE);
    }

    public void updatePhone(String idContact, String mN, String mP, String mE) {
        Log.e("123321", "updatePhone: "+mN+mP+mE );
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_PHONE_M, KEY.FALSE);
        values.put(COLUMN_PHONE_MERGER_N, mN);
        values.put(COLUMN_PHONE_MERGER_P, mP);
        values.put(COLUMN_PHONE_MERGER_E, mE);
        db.update(TABLE_CONTACT_PHONE, values, COLUMN_ID_CONTACT_IDP + "='" + idContact + "'", null);
        db.close();
    }

    public void updateEmail(String idContact, String mN, String mP, String mE) {
        Log.e("123321", "updateEmail: "+mN+mP+mE );
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_EMAIL_M, KEY.FALSE);
        values.put(COLUMN_EMAIL_MERGER_N, mN);
        values.put(COLUMN_EMAIL_MERGER_P, mP);
        values.put(COLUMN_EMAIL_MERGER_E, mE);
        db.update(TABLE_CONTACT_EMAIL, values, COLUMN_ID_CONTACT_IDE + "='" + idContact + "'", null);
        db.close();
    }

    public void updateMergedContact(String id) {
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_MERGER, KEY.FALSE);
        values.put(COLUMN_CONTACT_MERGER_F, KEY.FALSE);
        db.update(TABLE_CONTACT, values, COLUMN_TABLE_CONTACT_ID + "='" + id + "'", null);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_CONTACT);
        db.execSQL(SQL_CREATE_TABLE_CONTACT_ACCOUNT);
        db.execSQL(SQL_CREATE_TABLE_CONTACT_PHONE);
        db.execSQL(SQL_CREATE_TABLE_CONTACT_EMAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT_PHONE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT_EMAIL);
    }


}
