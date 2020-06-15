package com.hungdt.test.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hungdt.test.model.Contact;

import java.util.ArrayList;
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

    public static final String TABLE_CONTACT_ACCOUNT = "TB_CONTACT_ACCOUNT";
    public static final String COLUMN_CONTACT_ACCOUNT_ID = "CONTACT_ACCOUNT_ID";
    public static final String COLUMN_ID_CONTACT_ACCOUNT = "CONTACT_ID";
    public static final String COLUMN_CONTACT_ACCOUNT_NAME = "CONTACT_ACCOUNT_NAME";

    public static final String TABLE_CONTACT_PHONE = "TB_CONTACT_PHONE";
    public static final String COLUMN_CONTACT_PHONE_ID = "CONTACT_PHONE_ID";
    public static final String COLUMN_ID_CONTACT_PHONE = "CONTACT_ID";
    public static final String COLUMN_CONTACT_PHONE_NAME = "CONTACT_PHONE_NAME";

    public static final String TABLE_CONTACT_EMAIL = "TB_CONTACT_EMAIL";
    public static final String COLUMN_CONTACT_EMAIL_ID = "CONTACT_EMAIL_ID";
    public static final String COLUMN_ID_CONTACT_EMAIL = "CONTACT_ID";
    public static final String COLUMN_CONTACT_EMAIL_NAME = "CONTACT_EMAIL_NAME";

    public static final String SQL_CREATE_TABLE_CONTACT = "CREATE TABLE " + TABLE_CONTACT + "("
            + COLUMN_TABLE_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CONTACT_ID + " TEXT NOT NULL, "
            + COLUMN_CONTACT_NAME + " TEXT NOT NULL, "
            + COLUMN_CONTACT_IMAGE + " TEXT NOT NULL " + ");";

    public static final String SQL_CREATE_TABLE_CONTACT_ACCOUNT = "CREATE TABLE " + TABLE_CONTACT_ACCOUNT + "("
            + COLUMN_CONTACT_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID_CONTACT_ACCOUNT + " TEXT NOT NULL, "
            + COLUMN_CONTACT_ACCOUNT_NAME + " TEXT NOT NULL " + ");";

    public static final String SQL_CREATE_TABLE_CONTACT_PHONE = "CREATE TABLE " + TABLE_CONTACT_PHONE + "("
            + COLUMN_CONTACT_PHONE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID_CONTACT_PHONE + " TEXT NOT NULL, "
            + COLUMN_CONTACT_PHONE_NAME + " TEXT NOT NULL " + ");";

    public static final String SQL_CREATE_TABLE_CONTACT_EMAIL = "CREATE TABLE " + TABLE_CONTACT_EMAIL + "("
            + COLUMN_CONTACT_EMAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID_CONTACT_EMAIL + " TEXT NOT NULL, "
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

    public void addContact(String idContact, String name, String image) {
        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_ID, idContact);
        values.put(COLUMN_CONTACT_NAME, name);
        values.put(COLUMN_CONTACT_IMAGE, image);
        database.insert(TABLE_CONTACT, null, values);
        database.close();
    }

    public String getLastContactID() {
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

    public void addAccount(String id, String account) {
        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_ACCOUNT, id);
        values.put(COLUMN_CONTACT_ACCOUNT_NAME, account);
        database.insert(TABLE_CONTACT_ACCOUNT, null, values);
        database.close();
    }

    public void addPhone(String id, String phone) {
        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_PHONE, id);
        values.put(COLUMN_CONTACT_PHONE_NAME, phone);
        database.insert(TABLE_CONTACT_PHONE, null, values);
        database.close();
    }

    public void addEmail(String id, String email) {
        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_EMAIL, id);
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
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_TABLE_CONTACT_ID));
                contacts.add(new Contact(id,
                        cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                        getPhone(id),
                        getAccount(id),
                        getEmail(id)));
                cursor.moveToNext();
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
                    contact = new Contact(id,cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            getPhone(id),getAccount(id),getEmail(id));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return contact;
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
    public List<String> getAccount(String contactID) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_ACCOUNT), null);
        List<String> phones = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_ACCOUNT)).equals(contactID)) {
                    phones.add(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ACCOUNT_NAME)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return phones;
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

    public void deleteContact(String id) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TABLE_CONTACT_ID, id);
        db.delete(TABLE_CONTACT, COLUMN_TABLE_CONTACT_ID + "='" + id + "'", new String[]{});

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
        db.delete(TABLE_CONTACT_EMAIL, COLUMN_CONTACT_EMAIL_ID+ "='" + id + "'", new String[]{});

        db.close();

    }
    public void deleteAccount(String id) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_ACCOUNT_ID, id);
        db.delete(TABLE_CONTACT_ACCOUNT, COLUMN_CONTACT_ACCOUNT_ID + "='" + id + "'", new String[]{});

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
