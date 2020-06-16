package com.hungdt.test.view;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Contact;
import com.hungdt.test.view.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListContactActivity extends AppCompatActivity {
    List<Contact> contactList = new ArrayList<>();
    ContactAdapter contactAdapter;
    RecyclerView rcvContactView;
    String idContact = null;
    String name = null;
    String image = null;
    List<String> accounts = new ArrayList<>();
    List<String> phones = new ArrayList<>();
    List<String> emails = new ArrayList<>();
/*
* // Câu lệnh tìm kiếm
    @Query("SELECT * FROM phone WHERE Ten like :timKiem")
    List<Phone> getListTimKiem(String timKiem);
    *
    * imgTimKiem.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String srearch = String.valueOf(edtTimKiem.getText()).trim().replaceAll("\\s+", " ");
                String timKiem = "%" + srearch + "%";
                listphones = AppDatabasePhone.getInstance().getIDatabase().getPhoneDao().getListTimKiem(timKiem);
                phoneAdaper.notifyDataSetChanged();

                checkDataListPhone(listphones.size());
                loadDataTrangChinh(view);

                txtTraVeTimKiem.setVisibility(View.VISIBLE);
                txtTieuDe.setVisibility(View.INVISIBLE);

                txtTraVeTimKiem.setText("Kết quả tìm kiếm '" + edtTimKiem.getText() + "' là : ");
            }
        });
    * */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact);

        rcvContactView = findViewById(R.id.rcvListContact);

        contactList = DBHelper.getInstance(this).getAllContact();
        if(contactList.size()==0){
            readAccountContacts();
            contactList = DBHelper.getInstance(this).getAllContact();
        }

        Collections.sort(contactList);
        contactAdapter = new ContactAdapter(this, contactList);
        rcvContactView.setLayoutManager(new LinearLayoutManager(this));
        rcvContactView.setAdapter(contactAdapter);

    }

    private void readAccountContacts() {

        String[] projections = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI
        };
        Cursor cursorContact = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projections, null, null, null);
        while (cursorContact != null && cursorContact.moveToNext()) {

            idContact = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts._ID));
            name = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            image = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
            String[] projection2 = {
                    ContactsContract.RawContacts._ID,
                    ContactsContract.RawContacts.CONTACT_ID,
                    ContactsContract.RawContacts.ACCOUNT_NAME,
                    ContactsContract.RawContacts.ACCOUNT_TYPE
            };
            //val stringZalo =
            String selection = ContactsContract.RawContacts.CONTACT_ID + " = " + idContact;
            //val selection = "${ContactsContract.RawContacts._ID} = $idContact";
            Cursor cursorAccount = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection2, selection, null, null, null);
            while (cursorAccount != null && cursorAccount.moveToNext()) {
                String idRawContract = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts._ID));
                String account = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
                String accountType = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
                //readDataXXX(idContact)
                accounts.add(account);
                String[] projectionx = {
                        ContactsContract.Data.RAW_CONTACT_ID,
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.Data.DATA1,
                        ContactsContract.Data.DATA2,
                };
                String selectionx = ContactsContract.Data.RAW_CONTACT_ID + " = " + idRawContract;
                Cursor cursorData = getContentResolver().query(ContactsContract.Data.CONTENT_URI, projectionx, selectionx, null, null);
                while (cursorData != null && cursorData.moveToNext()) {
                    if (accountType.equals("vnd.sec.contact.phone") || accountType.equals("vnd.sec.contact.sim") || accountType.equals("vnd.sec.contact.sim2")) {
                        String data1 = cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.DATA1));
                        String mineType = cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.MIMETYPE));
                        if (mineType.equals("vnd.android.cursor.item/phone_v2")) {
                            phones.add(data1);
                        } else if (mineType.equals("vnd.android.cursor.item/email_v2")) {
                            emails.add(data1);
                        }
                    }

                }
                assert cursorData != null;
                cursorData.close();

            }
            assert cursorAccount != null;
            cursorAccount.close();
            Log.e("HDT321", "readAccountContacts:\n" + name + " \n " + phones + "\n" + emails);
            contactList.add(new Contact("0",idContact, name, image, phones, accounts, emails));
            if (image != null) {
                DBHelper.getInstance(this).addContact(idContact, name, image);
            } else {
                DBHelper.getInstance(this).addContact(idContact, name, "image");
            }
            String id = DBHelper.getInstance(this).getLastContactID();
            if (!phones.isEmpty()) {
                for (int i = 0; i < phones.size(); i++) {
                    DBHelper.getInstance(this).addPhone(id, phones.get(i));
                }
            }
            if (!accounts.isEmpty()) {
                for (int i = 0; i < accounts.size(); i++) {
                    DBHelper.getInstance(this).addAccount(id, accounts.get(i));
                }
            }
            if (!emails.isEmpty()) {
                for (int i = 0; i < emails.size(); i++) {
                    DBHelper.getInstance(this).addEmail(id, emails.get(i));
                }
            }
            image = null;
            phones.clear();
            accounts.clear();
            emails.clear();


        }
        assert cursorContact != null;
        cursorContact.close();
    }


    /*private void getContact() {
     *//*  Cursor cursor = getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts._ID, ContactsContract.RawContacts.ACCOUNT_TYPE},
                ContactsContract.RawContacts.ACCOUNT_TYPE + " <> 'com.anddroid.contacts.sim' "
                        + " AND " + ContactsContract.RawContacts.ACCOUNT_TYPE + " <> 'com.google' " //if you don't want to google contacts also
                ,
                null,
                null);*//*
        String[] projections = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projections, null, null, null);

        assert cursor != null;
        int nameIndex = cursor.getColumnIndex(projections[0]);
        int photoIndex = cursor.getColumnIndex(projections[1]);
        int hasPhoneNumber = cursor.getColumnIndex(projections[2]);
        int numberIndex = cursor.getColumnIndex(projections[3]);

        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            String name = cursor.getString(nameIndex);
            String image = cursor.getString(photoIndex);
            String phone = cursor.getString(numberIndex);

            contactList.add(new Contact(image,name,phone));
        }
        cursor.close();

    }*/
}
