package com.hungdt.test.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hungdt.test.model.Account;
import com.hungdt.test.model.Contact;
import com.hungdt.test.model.Email;
import com.hungdt.test.model.Phone;
import com.hungdt.test.utils.KEY;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Contact.db";

    public static final String TABLE_CONTACT = "TB_CONTACT";
    public static final String COLUMN_ID_TABLE_CONTACT = "ID_TABLE_CONTACT";
    public static final String COLUMN_CONTACT_ID = "CONTACT_ID";
    public static final String COLUMN_CONTACT_NAME = "CONTACT_NAME";
    public static final String COLUMN_CONTACT_IMAGE = "CONTACT_IMAGE";
    public static final String COLUMN_TYPE_CONTACT = "TYPE_CONTACT";
    public static final String COLUMN_TYPE_NAME = "TYPE_NAME";
    public static final String COLUMN_TYPE_PHONE = "TYPE_PHONE";
    public static final String COLUMN_TYPE_EMAIL = "TYPE_EMAIL";
    public static final String COLUMN_MERGER_CONTACT = "CONTACT_MERGER_CONTACT";
    public static final String COLUMN_MERGER_NAME = "CONTACT_MERGER_N";
    public static final String COLUMN_MERGER_PHONE = "CONTACT_MERGER_P";
    public static final String COLUMN_MERGER_EMAIL = "CONTACT_MERGER_E";
    public static final String COLUMN_MERGER_FATHER = "CONTACT_MERGER_FATHER";
    public static final String COLUMN_NO_NAME = "CONTACT_NO_NAME";
    public static final String COLUMN_NO_PHONE = "CONTACT_NO_PHONE";
    public static final String COLUMN_NO_EMAIL = "CONTACT_NO_EMAIL";
    public static final String COLUMN_DELETED = "CONTACT_DELETED";

    public static final String TABLE_CONTACT_ACCOUNT = "TB_CONTACT_ACCOUNT";
    public static final String COLUMN_ID_TABLE_ACCOUNT = "ID_TABLE_ACCOUNT";
    public static final String COLUMN_ID_CONTACT_ACCOUNT = "ID_CONTACT";
    public static final String COLUMN_ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String COLUMN_ACCOUNT_TYPE = "ACCOUNT_TYPE";

    public static final String TABLE_CONTACT_PHONE = "TB_CONTACT_PHONE";
    public static final String COLUMN_ID_TABLE_PHONE = "ID_TABLE_PHONE";
    public static final String COLUMN_ID_CONTACT_PHONE = "ID_CONTACT";
    public static final String COLUMN_PHONE_MERGED = "PHONE_MERGED";
    public static final String COLUMN_PHONE_NAME = "PHONE_NAME";

    public static final String TABLE_CONTACT_EMAIL = "TB_CONTACT_EMAIL";
    public static final String COLUMN_ID_TABLE_EMAIL = "ID_TABLE_EMAIL";
    public static final String COLUMN_ID_CONTACT_EMAIL = "ID_CONTACT";
    public static final String COLUMN_EMAIL_MERGED = "EMAIL_MERGED";
    public static final String COLUMN_EMAIL_NAME = "EMAIL_NAME";


    public static final String SQL_CREATE_TABLE_CONTACT = "CREATE TABLE " + TABLE_CONTACT + "("
            + COLUMN_ID_TABLE_CONTACT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CONTACT_ID + " TEXT NOT NULL, "
            + COLUMN_CONTACT_NAME + " TEXT NOT NULL, "
            + COLUMN_CONTACT_IMAGE + " TEXT NOT NULL, "
            + COLUMN_MERGER_CONTACT + " TEXT NOT NULL, "
            + COLUMN_MERGER_NAME + " TEXT NOT NULL, "
            + COLUMN_MERGER_PHONE + " TEXT NOT NULL, "
            + COLUMN_MERGER_EMAIL + " TEXT NOT NULL, "
            + COLUMN_TYPE_CONTACT + " TEXT NOT NULL, "
            + COLUMN_TYPE_NAME + " TEXT NOT NULL, "
            + COLUMN_TYPE_PHONE + " TEXT NOT NULL, "
            + COLUMN_TYPE_EMAIL + " TEXT NOT NULL, "
            + COLUMN_MERGER_FATHER + " TEXT NOT NULL, "
            + COLUMN_NO_NAME + " TEXT NOT NULL, "
            + COLUMN_NO_PHONE + " TEXT NOT NULL, "
            + COLUMN_NO_EMAIL + " TEXT NOT NULL, "
            + COLUMN_DELETED + " TEXT NOT NULL " + ");";

    public static final String SQL_CREATE_TABLE_CONTACT_ACCOUNT = "CREATE TABLE " + TABLE_CONTACT_ACCOUNT + "("
            + COLUMN_ID_TABLE_ACCOUNT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID_CONTACT_ACCOUNT + " TEXT NOT NULL, "
            + COLUMN_ACCOUNT_NAME + " TEXT NOT NULL, "
            + COLUMN_ACCOUNT_TYPE + " TEXT NOT NULL " + ");";

    public static final String SQL_CREATE_TABLE_CONTACT_PHONE = "CREATE TABLE " + TABLE_CONTACT_PHONE + "("
            + COLUMN_ID_TABLE_PHONE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID_CONTACT_PHONE + " TEXT NOT NULL, "
            + COLUMN_PHONE_MERGED + " TEXT NOT NULL, "
            + COLUMN_PHONE_NAME + " TEXT NOT NULL " + ");";

    public static final String SQL_CREATE_TABLE_CONTACT_EMAIL = "CREATE TABLE " + TABLE_CONTACT_EMAIL + "("
            + COLUMN_ID_TABLE_EMAIL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID_CONTACT_EMAIL + " TEXT NOT NULL, "
            + COLUMN_EMAIL_MERGED + " TEXT NOT NULL, "
            + COLUMN_EMAIL_NAME + " TEXT NOT NULL " + ");";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public void addContact(String idContact, String name, String image, String tContact, String tName, String tPhone, String tEmail, String mContact, String mName, String mPhone, String mEmail, String father, String deleted, String noName, String noPhone, String noEmail) {

        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_ID, idContact);
        values.put(COLUMN_CONTACT_NAME, name);
        values.put(COLUMN_CONTACT_IMAGE, image);
        values.put(COLUMN_MERGER_CONTACT, mContact);
        values.put(COLUMN_MERGER_NAME, mName);
        values.put(COLUMN_MERGER_PHONE, mPhone);
        values.put(COLUMN_MERGER_EMAIL, mEmail);
        values.put(COLUMN_TYPE_CONTACT, tContact);
        values.put(COLUMN_TYPE_NAME, tName);
        values.put(COLUMN_TYPE_PHONE, tPhone);
        values.put(COLUMN_TYPE_EMAIL, tEmail);
        values.put(COLUMN_MERGER_FATHER, father);
        values.put(COLUMN_NO_NAME, noName);
        values.put(COLUMN_NO_PHONE, noPhone);
        values.put(COLUMN_NO_EMAIL, noEmail);
        values.put(COLUMN_DELETED, deleted);
        database.insert(TABLE_CONTACT, null, values);
        database.close();
    }

    public String getLastContactID(String id) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        String icContact = null;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT)).equals(id)) {
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

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACT + " ORDER BY " + COLUMN_ID_TABLE_CONTACT + " DESC LIMIT 1", null);
        String lastID = "";
        while (cursor.moveToNext()) {
            lastID = cursor.getString(0);
        }
        cursor.close();
        db.close();

        return lastID;
    }

    public void addAccount(String idContact, String account, String type) {
        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_ACCOUNT, idContact);
        values.put(COLUMN_ACCOUNT_NAME, account);
        values.put(COLUMN_ACCOUNT_TYPE, type);
        database.insert(TABLE_CONTACT_ACCOUNT, null, values);
        database.close();
    }

    public void addPhone(String contactId, String merger, String phone) {
        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_PHONE, contactId);
        values.put(COLUMN_PHONE_MERGED, merger);
        values.put(COLUMN_PHONE_NAME, phone);
        database.insert(TABLE_CONTACT_PHONE, "", values);
        database.close();
    }

    public void addEmail(String contactId, String merger, String email) {
        SQLiteDatabase database = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_EMAIL, contactId);
        values.put(COLUMN_EMAIL_MERGED, merger);
        values.put(COLUMN_EMAIL_NAME, email);
        database.insert(TABLE_CONTACT_EMAIL, null, values);
        database.close();
    }


    public List<Contact> getAllContact() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (!cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)).equals(KEY.TRUE)) {
                    String idContact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID));
                    contacts.add(new Contact(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_FATHER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)),
                            getPhone(idContact),
                            getAccount(idContact),
                            getEmail(idContact)));
                    cursor.moveToNext();
                }
            }
        }
        cursor.close();
        db.close();
        return contacts;
    }

    public Contact getContact(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        Contact contact = null;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)).equals(idContact)) {
                    contact = new Contact(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT)), idContact,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_FATHER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)),
                            getPhone(idContact), getAccount(idContact), getEmail(idContact));
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
                if (cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_FATHER)).equals(KEY.TRUE)) {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT));
                    contacts.add(new Contact(id,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_FATHER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)),
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
        /*SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_MERGER)).equals(type)) {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT));
                    contacts.add(new Contact(id,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_FATHER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)),
                            getPhone(id), getAccount(id), getEmail(id)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return contacts;*/
        return null;
    }

    public String getNumberContactNoName() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        int count = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_NO_NAME)).equals("true")) {
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
                if (cursor.getString(cursor.getColumnIndex(COLUMN_NO_NAME)).equals("true")) {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT));
                    contacts.add(new Contact(id,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_FATHER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)),
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
                if (cursor.getString(cursor.getColumnIndex(COLUMN_NO_PHONE)).equals(KEY.TRUE)) {
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
                if (cursor.getString(cursor.getColumnIndex(COLUMN_NO_PHONE)).equals("true")) {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT));
                    contacts.add(new Contact(id,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_FATHER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)),
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
                if (cursor.getString(cursor.getColumnIndex(COLUMN_NO_EMAIL)).equals("true")) {
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
                if (cursor.getString(cursor.getColumnIndex(COLUMN_NO_EMAIL)).equals("true")) {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT));
                    contacts.add(new Contact(id,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_FATHER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)),
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
                    contacts = (new Contact(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_FATHER)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)),
                            getPhone(idContact), getAccount(idContact), getEmail(idContact)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return contacts;
    }

    public List<Phone> getPhone(String contactID) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_PHONE), null);
        List<Phone> phones = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_PHONE)).equals(contactID)) {
                    phones.add(new Phone(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_MERGED))));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return phones;
    }


/*    public List<Duplicate> getDupContact() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Duplicate> duplicates = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_CONTACT)).equals(KEY.FALSE)) {
                    duplicates.add(new Duplicate(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGED)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            getPhone(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT))),
                            getEmail(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT)))));
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
                if (cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)).equals(KEY.FALSE)) {
                    duplicates.add(new Duplicate(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGED)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MERGER_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)), null, null));
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
                if (cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_MERGER_P)).equals(KEY.FALSE) && cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_PHONE_M)).equals(KEY.FALSE)) {
                    duplicates.add(new Duplicate(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_PHONE_M)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_MERGER_N)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_MERGER_P)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_MERGER_E)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_PHONE_NAME)), null, null));
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
                if (cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_MERGER_E)).equals(KEY.FALSE) && cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_EMAIL_M)).equals(KEY.FALSE)) {
                    duplicates.add(new Duplicate(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_EMAIL_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_IDE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_EMAIL_M)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_MERGER_N)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_MERGER_P)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_MERGER_E)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_EMAIL_NAME)), null, null));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return duplicates;
    }*/

    public List<Account> getAccount(String contactID) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_ACCOUNT), null);
        List<Account> accounts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_ACCOUNT)).equals(contactID)) {
                    accounts.add(new Account(cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NAME)), cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_TYPE))));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return accounts;
    }

    public List<Email> getEmail(String contactID) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_EMAIL), null);
        List<Email> emails = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_EMAIL)).equals(contactID)) {
                    emails.add(new Email(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_MERGED))));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return emails;
    }

    public void deleteAllContact() {
        SQLiteDatabase db = instance.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CONTACT);
        db.execSQL("delete from " + TABLE_CONTACT_PHONE);
        db.execSQL("delete from " + TABLE_CONTACT_EMAIL);
        db.execSQL("delete from " + TABLE_CONTACT_ACCOUNT);
        db.close();
    }

    public void deleteContact(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_ID, idContact);
        db.delete(TABLE_CONTACT, COLUMN_CONTACT_ID + "='" + idContact + "'", new String[]{});

        db.close();
        deleteAccount(idContact);
        deletePhone(idContact);
        deleteEmail(idContact);
    }

    public void updateDisableContact(String id) {
        /*SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DELETED, KEY.TRUE);
        values.put(COLUMN_TYPE_MERGER, KEY.FALSE);
        values.put(COLUMN_MERGER_FATHER, KEY.FALSE);
        db.update(TABLE_CONTACT, values, COLUMN_ID_TABLE_CONTACT + "='" + id + "'", null);
        db.close();*/
    }

    public void deletePhone(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_PHONE, idContact);
        db.delete(TABLE_CONTACT_PHONE, COLUMN_ID_CONTACT_PHONE + "='" + idContact + "'", new String[]{});

        db.close();

    }

    public void deleteEmail(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_EMAIL, idContact);
        db.delete(TABLE_CONTACT_EMAIL, COLUMN_ID_CONTACT_EMAIL + "='" + idContact + "'", new String[]{});

        db.close();
    }


    public void deleteAccount(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_ACCOUNT, idContact);
        db.delete(TABLE_CONTACT_ACCOUNT, COLUMN_ID_CONTACT_ACCOUNT + "='" + idContact + "'", new String[]{});

        db.close();
    }

    public void updateContactMerger(String idContact, String tC, String tN, String tP, String tE, String mC, String mN, String mP, String mE) {
        Log.e("123123123", "updateContactMerger: " + mN + mE + mN);
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE_CONTACT, tC);
        values.put(COLUMN_TYPE_NAME, tN);
        values.put(COLUMN_TYPE_PHONE, tP);
        values.put(COLUMN_TYPE_EMAIL, tE);
        values.put(COLUMN_MERGER_CONTACT, mC);
        values.put(COLUMN_MERGER_NAME, mN);
        values.put(COLUMN_MERGER_PHONE, mP);
        values.put(COLUMN_MERGER_EMAIL, mE);
        db.update(TABLE_CONTACT, values, COLUMN_CONTACT_ID + "='" + idContact + "'", null);
        db.close();
        if (mP.equals(KEY.TRUE)) {
            updatePhone(idContact);
        }
        if (mE.equals(KEY.TRUE)) {
            updateEmail(idContact);
        }
    }

    public void updatePhone(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE_MERGED, KEY.TRUE);
        db.update(TABLE_CONTACT_PHONE, values, COLUMN_ID_CONTACT_PHONE + "='" + idContact + "'", null);
        db.close();
    }

    public void updateEmail(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL_MERGED, KEY.TRUE);
        db.update(TABLE_CONTACT_EMAIL, values, COLUMN_ID_CONTACT_EMAIL + "='" + idContact + "'", null);
        db.close();
    }

    /*public void updateMergedContact(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE_MERGER, KEY.FALSE);
        values.put(COLUMN_MERGER_FATHER, KEY.FALSE);
        db.update(TABLE_CONTACT, values, COLUMN_CONTACT_ID + "='" + idContact + "'", null);
        db.close();
    }*/

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
