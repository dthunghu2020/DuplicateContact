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
import java.util.Collections;
import java.util.List;

import static com.hungdt.test.view.MainActivity.contactList;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Contact.db";

    public static final String TABLE_CONTACT = "TB_CONTACT";
    public static final String COLUMN_ID_TABLE_CONTACT = "ID_TABLE_CONTACT";
    public static final String COLUMN_CONTACT_ID = "CONTACT_ID";
    public static final String COLUMN_CONTACT_NAME = "CONTACT_NAME";
    public static final String COLUMN_CONTACT_IMAGE = "CONTACT_IMAGE";
    public static final String COLUMN_NO_NAME = "CONTACT_NO_NAME";
    public static final String COLUMN_NO_PHONE = "CONTACT_NO_PHONE";
    public static final String COLUMN_NO_EMAIL = "CONTACT_NO_EMAIL";
    public static final String COLUMN_DELETED = "CONTACT_DELETED";

    public static final String TABLE_CONTACT_ACCOUNT = "TB_CONTACT_ACCOUNT";
    public static final String COLUMN_ID_TABLE_ACCOUNT = "ID_TABLE_ACCOUNT";
    public static final String COLUMN_ID_CONTACT_ACCOUNT = "ID_CONTACT";
    public static final String COLUMN_ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String COLUMN_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String COLUMN_DELETED_ACCOUNT = "ACCOUNT_DELETED";

    public static final String TABLE_CONTACT_PHONE = "TB_CONTACT_PHONE";
    public static final String COLUMN_ID_TABLE_PHONE = "ID_TABLE_PHONE";
    public static final String COLUMN_ID_CONTACT_PHONE = "ID_CONTACT";
    public static final String COLUMN_PHONE_NAME = "PHONE_NAME";
    public static final String COLUMN_DELETED_PHONE = "PHONE_DELETED";

    public static final String TABLE_CONTACT_EMAIL = "TB_CONTACT_EMAIL";
    public static final String COLUMN_ID_TABLE_EMAIL = "ID_TABLE_EMAIL";
    public static final String COLUMN_ID_CONTACT_EMAIL = "ID_CONTACT";
    public static final String COLUMN_EMAIL_NAME = "EMAIL_NAME";
    public static final String COLUMN_DELETED_EMAIL = "EMAIL_DELETED";


    public static final String SQL_CREATE_TABLE_CONTACT = "CREATE TABLE " + TABLE_CONTACT + "("
            + COLUMN_ID_TABLE_CONTACT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CONTACT_ID + " TEXT NOT NULL, "
            + COLUMN_CONTACT_NAME + " TEXT NOT NULL, "
            + COLUMN_CONTACT_IMAGE + " TEXT NOT NULL, "
            + COLUMN_NO_NAME + " TEXT NOT NULL, "
            + COLUMN_NO_PHONE + " TEXT NOT NULL, "
            + COLUMN_NO_EMAIL + " TEXT NOT NULL, "
            + COLUMN_DELETED + " TEXT NOT NULL " + ");";

    public static final String SQL_CREATE_TABLE_CONTACT_ACCOUNT = "CREATE TABLE " + TABLE_CONTACT_ACCOUNT + "("
            + COLUMN_ID_TABLE_ACCOUNT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID_CONTACT_ACCOUNT + " TEXT NOT NULL, "
            + COLUMN_ACCOUNT_NAME + " TEXT NOT NULL, "
            + COLUMN_ACCOUNT_TYPE + " TEXT NOT NULL, "
            + COLUMN_DELETED_ACCOUNT + " TEXT NOT NULL " + ");";

    public static final String SQL_CREATE_TABLE_CONTACT_PHONE = "CREATE TABLE " + TABLE_CONTACT_PHONE + "("
            + COLUMN_ID_TABLE_PHONE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID_CONTACT_PHONE + " TEXT NOT NULL, "
            + COLUMN_PHONE_NAME + " TEXT NOT NULL, "
            + COLUMN_DELETED_PHONE + " TEXT NOT NULL " + ");";

    public static final String SQL_CREATE_TABLE_CONTACT_EMAIL = "CREATE TABLE " + TABLE_CONTACT_EMAIL + "("
            + COLUMN_ID_TABLE_EMAIL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID_CONTACT_EMAIL + " TEXT NOT NULL, "
            + COLUMN_EMAIL_NAME + " TEXT NOT NULL, "
            + COLUMN_DELETED_EMAIL + " TEXT NOT NULL " + ");";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public void addContact(String idContact, String name, String image, String deleted, String noName, String noPhone, String noEmail) {

        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_ID, idContact);
        values.put(COLUMN_CONTACT_NAME, name);
        values.put(COLUMN_CONTACT_IMAGE, image);
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

    public void addAccount(String idContact, String account, String type, String delete) {
        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_ACCOUNT, idContact);
        values.put(COLUMN_ACCOUNT_NAME, account);
        values.put(COLUMN_ACCOUNT_TYPE, type);
        values.put(COLUMN_DELETED_ACCOUNT, delete);
        database.insert(TABLE_CONTACT_ACCOUNT, null, values);
        database.close();
    }

    public void addPhone(String contactId, String phone, String delete) {
        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_PHONE, contactId);
        values.put(COLUMN_PHONE_NAME, phone);
        values.put(COLUMN_DELETED_PHONE, delete);
        database.insert(TABLE_CONTACT_PHONE, "", values);
        database.close();
    }

    public void addEmail(String contactId, String email, String delete) {
        SQLiteDatabase database = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_CONTACT_EMAIL, contactId);
        values.put(COLUMN_EMAIL_NAME, email);
        values.put(COLUMN_DELETED_EMAIL, delete);
        database.insert(TABLE_CONTACT_EMAIL, null, values);
        database.close();
    }

    public List<Contact> getAllContact() {
        SQLiteDatabase db = instance.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)).equals(KEY.FALSE)) {
                    String idContact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID));
                    contacts.add(new Contact(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)),
                            getPhone(idContact),
                            getAccount(idContact),
                            getEmail(idContact)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        Collections.sort(contacts);
        return contacts;
    }

    public List<Contact> getAllBackUpContact() {
        SQLiteDatabase db = instance.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)).equals(KEY.TRUE)) {
                    String idContact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID));
                    contacts.add(new Contact(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)),
                            getBackupPhone(idContact),
                            getBackupAccount(idContact),
                            getBackupEmail(idContact)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        Collections.sort(contacts);
        return contacts;
    }

    private List<Email> getBackupEmail(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_EMAIL), null);
        List<Email> emails = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_EMAIL)).equals(idContact) && cursor.getString(cursor.getColumnIndex(COLUMN_DELETED_EMAIL)).equals(KEY.TRUE)) {
                    emails.add(new Email(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_NAME))));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return emails;
    }

    private List<Account> getBackupAccount(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_ACCOUNT), null);
        List<Account> accounts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_ACCOUNT)).equals(idContact) && cursor.getString(cursor.getColumnIndex(COLUMN_DELETED_ACCOUNT)).equals(KEY.TRUE)) {
                    accounts.add(new Account(cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NAME)), cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_TYPE))));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return accounts;
    }

    private List<Phone> getBackupPhone(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_PHONE), null);
        List<Phone> phones = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_PHONE)).equals(idContact) && cursor.getString(cursor.getColumnIndex(COLUMN_DELETED_PHONE)).equals(KEY.TRUE)) {
                    phones.add(new Phone(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NAME))));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return phones;
    }

    public Contact getContactBackUp(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        Contact contact = null;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)).equals(idContact)) {
                    contact = new Contact(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT)), idContact,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)),
                            getBackupPhone(idContact), getBackupAccount(idContact), getBackupEmail(idContact));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return contact;
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

    public List<Contact> getContactNoName() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_NO_NAME)).equals(KEY.TRUE) && cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)).equals(KEY.FALSE)) {
                    String idContact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID));
                    contacts.add(new Contact(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT)),
                            idContact,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
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

    public String getNumberContactNoName() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        int count = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_NO_NAME)).equals(KEY.TRUE) && cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)).equals(KEY.FALSE)) {
                    count++;
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return String.valueOf(count);
    }

    public String getNumberContactNoPhone() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        int count = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_NO_PHONE)).equals(KEY.TRUE) && cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)).equals(KEY.FALSE)) {
                    count++;
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return String.valueOf(count);
    }

    public String getNumberContactNoEmail() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        int count = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_NO_EMAIL)).equals(KEY.TRUE) && cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)).equals(KEY.FALSE)) {
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
                if (cursor.getString(cursor.getColumnIndex(COLUMN_NO_PHONE)).equals(KEY.TRUE) && cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)).equals(KEY.FALSE)) {
                    String idContact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID));
                    contacts.add(new Contact(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT)),
                            idContact,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
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

    public List<Contact> getContactNoEmail() {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT), null);
        List<Contact> contacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_NO_EMAIL)).equals(KEY.TRUE) && cursor.getString(cursor.getColumnIndex(COLUMN_DELETED)).equals(KEY.FALSE)) {
                    String idContact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID));
                    contacts.add(new Contact(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_CONTACT)),
                            idContact,
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_IMAGE)),
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
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_PHONE)).equals(contactID) && cursor.getString(cursor.getColumnIndex(COLUMN_DELETED_PHONE)).equals(KEY.FALSE)) {
                    phones.add(new Phone(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_PHONE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NAME))));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return phones;
    }

    public List<Email> getEmail(String contactID) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_EMAIL), null);
        List<Email> emails = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_EMAIL)).equals(contactID) && cursor.getString(cursor.getColumnIndex(COLUMN_DELETED_EMAIL)).equals(KEY.FALSE)) {
                    emails.add(new Email(cursor.getString(cursor.getColumnIndex(COLUMN_ID_TABLE_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_EMAIL)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_NAME))));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return emails;
    }

    public List<Account> getAccount(String contactID) {
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_CONTACT_ACCOUNT), null);
        List<Account> accounts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex(COLUMN_ID_CONTACT_ACCOUNT)).equals(contactID) && cursor.getString(cursor.getColumnIndex(COLUMN_DELETED_ACCOUNT)).equals(KEY.FALSE)) {
                    accounts.add(new Account(cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NAME)), cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_TYPE))));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return accounts;
    }


    /*public void deleteAllContact() {
        SQLiteDatabase db = instance.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CONTACT);
        db.execSQL("delete from " + TABLE_CONTACT_PHONE);
        db.execSQL("delete from " + TABLE_CONTACT_EMAIL);
        db.execSQL("delete from " + TABLE_CONTACT_ACCOUNT);
        db.close();
    }*/

    public void deleteBackupContact(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete(TABLE_CONTACT, COLUMN_CONTACT_ID + "='" + idContact + "'", new String[]{});
        db.close();
        deleteBackupPhone(idContact);
        deleteBackupAccount(idContact);
        deleteBackupEmail(idContact);
    }

    private void deleteBackupPhone(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete(TABLE_CONTACT_PHONE, COLUMN_ID_CONTACT_PHONE + "='" + idContact + "'", new String[]{});
        db.close();
    }

    private void deleteBackupAccount(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete(TABLE_CONTACT_ACCOUNT, COLUMN_ID_CONTACT_ACCOUNT + "='" + idContact + "'", new String[]{});
        db.close();
    }

    private void deleteBackupEmail(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete(TABLE_CONTACT_EMAIL, COLUMN_ID_CONTACT_EMAIL + "='" + idContact + "'", new String[]{});
        db.close();
    }

    public void updateDisableContact(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DELETED, KEY.TRUE);
        db.update(TABLE_CONTACT, values, COLUMN_CONTACT_ID + "='" + idContact + "'", null);
        db.close();
        updateDisablePhone(idContact);
        updateDisableAccount(idContact);
        updateDisableEmail(idContact);
    }

    private void updateDisablePhone(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DELETED_PHONE, KEY.TRUE);
        db.update(TABLE_CONTACT_PHONE, values, COLUMN_ID_CONTACT_PHONE + "='" + idContact + "'", null);
        db.close();
    }

    private void updateDisableAccount(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DELETED_ACCOUNT, KEY.TRUE);
        db.update(TABLE_CONTACT_ACCOUNT, values, COLUMN_ID_CONTACT_ACCOUNT + "='" + idContact + "'", null);
        db.close();
    }

    private void updateDisableEmail(String idContact) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DELETED_EMAIL, KEY.TRUE);
        db.update(TABLE_CONTACT_EMAIL, values, COLUMN_ID_CONTACT_EMAIL + "='" + idContact + "'", null);
        db.close();
    }

    public void reloadContact() {
        Log.e("111", "reloadContact: " );
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete(TABLE_CONTACT, COLUMN_DELETED + "='" + KEY.FALSE + "'", new String[]{});
        db.close();
        reloadPhone();
        reloadAccount();
        reloadEmail();
    }

    private void reloadPhone() {
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete(TABLE_CONTACT_PHONE, COLUMN_DELETED_PHONE + "='" + KEY.FALSE + "'", new String[]{});
        db.close();
    }

    private void reloadAccount() {
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete(TABLE_CONTACT_ACCOUNT, COLUMN_DELETED_ACCOUNT + "='" + KEY.FALSE + "'", new String[]{});
        db.close();
    }

    private void reloadEmail() {
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete(TABLE_CONTACT_EMAIL, COLUMN_DELETED_EMAIL + "='" + KEY.FALSE + "'", new String[]{});
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
