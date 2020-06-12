package com.hungdt.test.view;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hungdt.test.R;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * samsung: "vnd.sec.contact.phone: "vnd.sec.contact.phone"
 * htc: "com.htc.android.pcsc: "pcsc"
 * sony: "com.sonyericsson.localcontacts: "Phone contacts"
 * lge: "com.lge.sync: "Phone"
 * lge (option 2): "com.lge.phone"
 * t-mobile: "vnd.tmobileus.contact.phone: "MobileLife Contacts"
 * huawei: "com.android.huawei.phone: "Phone"
 * lenovo: "Local Phone Account: "Phone"
 */

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_PICK_CONTACT = 102;
    private static final int PICK_CONTACT = 103;
    private Button btnOpen,btnTest;
    private TextView txtData;
    ArrayList<String> arrayList;
    final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOpen = findViewById(R.id.btnOpen);
        btnTest = findViewById(R.id.btnTest);
        txtData = findViewById(R.id.txtData);

        arrayList = new ArrayList<>();
        getContactFromSIM();
        //getContactFromPhoneUse();
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListContactActivity.class);
                startActivity(intent);
            }
        });
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();
                            Log.i("ABC","number is:"+phones.getString(phones.getColumnIndex("data1")));
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Log.i("ABC","Name is"+name);

                    }
                }
                break;
        }
        //////////////////////////////////////////////////////////
        //Cái  này chọn trong danh bạ và hiển thị thông tin
        /*Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);*/
        ////
        /*if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    Cursor cursor = null;
                    try {
                        String phoneNo = null;
                        String name = null;

                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        phoneNo = cursor.getString(phoneIndex);
                        name = cursor.getString(nameIndex);

                        Log.e("ABC","Name and Contact number is"+name + "," + phoneNo);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else {
            Log.e("Failed", "Not able to pick contact");
        }*/
    }

    private void checkPermission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
            startActivity(new Intent(this, AskPermissionActivity.class));
        } else {
            //getContact();
            readContacts();
            Intent intent = new Intent(MainActivity.this, ListContactActivity.class);
            startActivity(intent);
        }
    }

    private void getContact() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        assert cursor != null;
        while (cursor.moveToNext()) {
            String DISPLAY_NAME = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));          //Chức vụ Tên Tên lót họ, chức danh
            String NAME_RAW_CONTACT_ID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID)); //1059
            String PHOTO_ID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));                         //null(3038)
            String PHOTO_FILE_ID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_FILE_ID));               //null(1)
            String PHOTO_URI = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));                       //null(content://com.android.contact/display_photo/1)
            String PHOTO_THUMBNAIL_URI = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));    //null(content://com.android.contact/contacts/1063/photo)
            String IN_DEFAULT_DIRECTORY = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IN_DEFAULT_DIRECTORY));     //1
            String IN_VISIBLE_GROUP = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IN_VISIBLE_GROUP));             //1
            //String IS_USER_PROFILE = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IS_USER_PROFILE));
            String HAS_PHONE_NUMBER = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));             //1
            String LOOKUP_KEY = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));                         //key dài
            String CONTACT_LAST_UPDATED_TIMESTAMP = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP)); //1591189343196
            String NUMBER = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));                         //sdt
            String NORMALIZED_NUMBER = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)); //null
            int indexPhoneType = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE);                                 //70
            int label = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LABEL);                                         //69
            arrayList.add(DISPLAY_NAME + "\n" + NAME_RAW_CONTACT_ID + "\n" + PHOTO_ID + "\n" + PHOTO_FILE_ID + "\n" + PHOTO_URI + "\n" + PHOTO_THUMBNAIL_URI +
                    "\n" + IN_DEFAULT_DIRECTORY + "\n" + IN_VISIBLE_GROUP + "\n" + HAS_PHONE_NUMBER + "\n" + LOOKUP_KEY + "\n" + CONTACT_LAST_UPDATED_TIMESTAMP
                    + "\n" + NUMBER + "\n" + NORMALIZED_NUMBER + "\n");
            txtData.setText(arrayList.toString());

            Log.e("123123", "getContact: " + indexPhoneType);
            Log.e("123123", "getContact: " + label);
        }

        Log.e("123123", "calender " + calendar.getTimeInMillis());


        ContentResolver resolver = getContentResolver();

        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        Cursor contacts = resolver.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
        while (contacts.moveToNext()) {

            long contactId = contacts.getLong(0);
            String name = contacts.getString(1);

            Log.i("Contacts", "Contact " + contactId + " " + name + " - has the following raw-contacts:");

            String[] projection2 = new String[]{ContactsContract.RawContacts._ID, ContactsContract.RawContacts.ACCOUNT_TYPE, ContactsContract.RawContacts.ACCOUNT_NAME};
            Cursor raws = resolver.query(ContactsContract.RawContacts.CONTENT_URI, null, ContactsContract.RawContacts.CONTACT_ID, null, null);

            while (raws.moveToNext()) {

                long rawId = raws.getLong(0);
                String accountType = raws.getString(1);
                String accountName = raws.getString(2);

                Log.i("Contacts", "\t RawContact " + rawId + " from " + accountType + " / " + accountName);
            }
            raws.close();
        }
        contacts.close();
        getDefaultAccountNameAndType();
        getContactFromSIM();
        getContactFromPhoneUse();
    }

    private void getContactFromPhoneUse() {
        Cursor cursor = getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts._ID, ContactsContract.RawContacts.ACCOUNT_TYPE},
                ContactsContract.RawContacts.ACCOUNT_TYPE + " <> 'com.android.contacts.sim' "
                        + " AND " + ContactsContract.RawContacts.ACCOUNT_TYPE + " <> 'com.google' " //if you don't want to google contacts also
                ,
                null,
                null);
        List<String> listName = new ArrayList<>();
        List<String> listContactId = new ArrayList<>();
        List<String> listMobileNo = new ArrayList<>();
        while (cursor.moveToNext()) {
            listName.add(cursor.getString(cursor.getColumnIndex("name")));
            listContactId.add(cursor.getString(cursor.getColumnIndex("_id")));
            listMobileNo.add(cursor.getString(cursor.getColumnIndex("number")));
        }
        Log.e("Contacts", "getDefaultAccountNameAndType: listName, " + listName);
        Log.e("Contacts", "getDefaultAccountNameAndType: listContactId, " + listContactId);
        Log.e("Contacts", "getDefaultAccountNameAndType: listMobileNo, " + listMobileNo);
    }

    private void getContactFromSIM() {
        Uri simUri = Uri.parse("content://icc/adn");
        Cursor cursorSim = this.getContentResolver().query(simUri, null, null, null, null);
        List<String> listName = new ArrayList<>();
        List<String> listContactId = new ArrayList<>();
        List<String> listMobileNo = new ArrayList<>();
        while (cursorSim.moveToNext()) {
            listName.add(cursorSim.getString(cursorSim.getColumnIndex("name")));
            listContactId.add(cursorSim.getString(cursorSim.getColumnIndex("_id")));
            listMobileNo.add(cursorSim.getString(cursorSim.getColumnIndex("number")));
        }
        Log.e("Contacts", "getDefaultAccountNameAndType: listName, " + listName);
        Log.e("Contacts", "getDefaultAccountNameAndType: listContactId, " + listContactId);
        Log.e("Contacts", "getDefaultAccountNameAndType: listMobileNo, " + listMobileNo);
    }


    public void getDefaultAccountNameAndType() {
        String accountType = "";
        String accountName = "";

        long rawContactId = 0;
        Uri rawContactUri = null;
        ContentProviderResult[] results = null;

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).build());

        try {
            results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ops.clear();
        }

        for (ContentProviderResult result : results) {
            rawContactUri = result.uri;
            rawContactId = ContentUris.parseId(rawContactUri);
        }

        Cursor c = getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI
                , new String[]{ContactsContract.RawContacts.ACCOUNT_TYPE, ContactsContract.RawContacts.ACCOUNT_NAME}
                , ContactsContract.RawContacts._ID + "=?"
                , new String[]{String.valueOf(rawContactId)}
                , null);

        if (c.moveToFirst()) {
            if (!c.isAfterLast()) {
                accountType = c.getString(c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
                accountName = c.getString(c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
            }
        }

        getContentResolver().delete(rawContactUri, null, null);

        c.close();
        c = null;


        Log.e("Contacts", "getDefaultAccountNameAndType: accountType, " + accountType);
        Log.e("Contacts", "getDefaultAccountNameAndType: accountName, " + accountName);
    }


    public void readContacts() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Log.i("ABC", "aaa\n"+id+"\n"+name+"\n");

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.e("ABC", "phone: " + phone);
                    }
                    pCur.close();


                    // get email and type

                    Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        // This would allow you get several email addresses
                        // if the email addresses were stored in an array
                        String email = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        String emailType = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        Log.e("ABC", "email and type - Email " + email + " Email Type : " + emailType);
                    }
                    emailCur.close();

                    // Get note.......
                    String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] noteWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                    Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                    if (noteCur.moveToFirst()) {
                        String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                        Log.e("ABC", "Note " + note);
                    }
                    noteCur.close();

                    //Get Postal Address.... Nhận địa chỉ bưu điện

                    String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] addrWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                    Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, null, null, null);
                    while (addrCur.moveToNext()) {
                        String poBox = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                        String street = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                        String city = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                        String state = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                        String postalCode = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                        String country = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                        String type = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

                    }
                    addrCur.close();

                    // Get Instant Messenger......... Nhận tin nhắn tức thời.

                    String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] imWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
                    Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, imWhere, imWhereParams, null);
                    if (imCur.moveToFirst()) {
                        String imName = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
                        String imType;
                        imType = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
                        Log.e("ABC", "Instant Messenger - DATA: " + imName + " TYPE : " + imType);
                    }
                    imCur.close();

                    // Get Organizations......... Nhận tổ chức

                    String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] orgWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                    Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, orgWhere, orgWhereParams, null);
                    if (orgCur.moveToFirst()) {
                        String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                        String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                        Log.e("ABC", "Organizations - DATA " + orgName + " TITLE : " + title);
                    }
                    orgCur.close();
                }
            }
        }
    }


}
