package com.hungdt.test.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Account;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ContactFragment extends Fragment {

    private List<Contact> contactList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private RecyclerView rcvContactView;
    private String idContact;
    private String name;
    private String image ;
    private List<Account> accounts = new ArrayList<>();
    private List<String> phones = new ArrayList<>();
    private List<String> emails = new ArrayList<>();

    private Calendar calendar;

    public ContactFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendar = Calendar.getInstance();
        Log.i("TAG", "onViewCreated: " + calendar.getTimeInMillis());

        rcvContactView = view.findViewById(R.id.rcvListContact);

        DBHelper.getInstance(getActivity()).deleteAllContact();
        readAccountContacts();
        contactList = DBHelper.getInstance(getActivity()).getAllContact();

        Collections.sort(contactList);
        contactAdapter = new ContactAdapter(view.getContext(), contactList,KEY.CONTACT);
        rcvContactView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rcvContactView.setAdapter(contactAdapter);

        /*IntentFilter intentFilter = new IntentFilter(MainActivity.ACTION_UPDATE);
        getLayoutInflater().getContext().registerReceiver(bcUpdateContact,intentFilter);*/

    }

    private void readAccountContacts() {

        String[] projections = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI,
        };
        Cursor cursorContact = getLayoutInflater().getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projections, null, null, null);
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
            Cursor cursorAccount = getLayoutInflater().getContext().getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection2, selection, null, null, null);
            while (cursorAccount != null && cursorAccount.moveToNext()) {
                String idRawContract = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts._ID));
                String account = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
                String accountType = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
                //readDataXXX(idContact)
                accounts.add(new Account(account, accountType));
                String[] projectionx = {
                        ContactsContract.Data.RAW_CONTACT_ID,
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.Data.DATA1,
                        ContactsContract.Data.DATA2,
                };
                String selectionx = ContactsContract.Data.RAW_CONTACT_ID + " = " + idRawContract;

                Cursor cursorData = getLayoutInflater().getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, projectionx, selectionx, null, null);
                while (cursorData != null && cursorData.moveToNext()) {
                    if (accountType.contains("xiaomi")||accountType.contains("google")||accountType.contains("pcsc")||
                            accountType.contains("phone") || accountType.contains("Phone") || accountType.contains("PHONE") || accountType.contains("Sim")|| accountType.contains("sim")|| accountType.contains("SIM")) {
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
            boolean noPhone = false;
            boolean noName = true;
            boolean noEmail = false;
            if (phones.isEmpty()) {
                noPhone = true;
                Log.i("TAG", "readAccountContacts: " + name);
            }
            if (!name.equals("")) {
                noName = false;
            }
            if (emails.isEmpty()) {
                noEmail = true;
            }

            contactList.add(new Contact("0", idContact, name, image, KEY.FALSE, KEY.FALSE, KEY.FALSE, phones, accounts, emails));
            if (image != null) {
                DBHelper.getInstance(getActivity()).addContact(idContact, name, image, KEY.FALSE, KEY.FALSE, KEY.FALSE, String.valueOf(noName), String.valueOf(noPhone), String.valueOf(noEmail));
            } else {
                DBHelper.getInstance(getActivity()).addContact(idContact, name, "image", KEY.FALSE, KEY.FALSE, KEY.FALSE,String.valueOf(noName), String.valueOf(noPhone), String.valueOf(noEmail));
            }

            String id = DBHelper.getInstance(getActivity()).getLastID();
            String idContact = DBHelper.getInstance(getActivity()).getLastContactID(id);
            if (!noPhone) {
                for (int i = 0; i < phones.size(); i++) {
                    DBHelper.getInstance(getActivity()).addPhone(id, idContact, KEY.FALSE, phones.get(i));
                }
            }
            if (!accounts.isEmpty()) {
                for (int i = 0; i < accounts.size(); i++) {
                    DBHelper.getInstance(getActivity()).addAccount(id, accounts.get(i).getAccountName(), accounts.get(i).getAccountType());
                }
            }
            if (!noEmail) {
                for (int i = 0; i < emails.size(); i++) {
                    DBHelper.getInstance(getActivity()).addEmail(id, idContact, KEY.FALSE, emails.get(i));
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

    /*private BroadcastReceiver bcUpdateContact = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            *//*String name = intent.getStringExtra(KEY);
            txt.setText(name);*//*
        }
    };*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        //getLayoutInflater().getContext().unregisterReceiver(bcUpdateContact);
    }
}
