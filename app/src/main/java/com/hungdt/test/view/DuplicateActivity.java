package com.hungdt.test.view;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.test.ContactConfig;
import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Contact;
import com.hungdt.test.model.Duplicate;
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.DuplicateAdapter;
import com.unity3d.ads.UnityAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.hungdt.test.view.MainActivity.contactList;
import static com.hungdt.test.view.MainActivity.showInterstitial;

public class DuplicateActivity extends AppCompatActivity {
    private ImageView imgBack, imgBtnDelete;
    private TextView txtTitleDelete, txtBtnDelete, txtEmpty;
    private CheckBox cbAll;
    private LinearLayout llDelete, llEmpty;
    private RecyclerView rcvList;
    private List<Contact> listContact = new ArrayList<>();
    private List<Duplicate> names = new ArrayList<>();
    private List<Duplicate> phones = new ArrayList<>();
    private List<Duplicate> emails = new ArrayList<>();
    private List<Duplicate> contacts = new ArrayList<>();
    private DuplicateAdapter duplicateAdapter;
    private String type;
    private List<String> idContact = new ArrayList<>();
    private ArrayList<Integer> typeList = new ArrayList<>();
    public static final String ACTION_FINISH_ACTIVITY = "finishDuplicateActivity";
    private Random rd;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initView();
        imgBtnDelete.setImageResource(R.drawable.merge);
        txtBtnDelete.setText("MERGER CONTACT");

        Ads.initNativeGgFb((LinearLayout) findViewById(R.id.lnNative), this, true);

        IntentFilter intentFilter = new IntentFilter(ACTION_FINISH_ACTIVITY);
        registerReceiver(broadCastUpdate, intentFilter);

        Intent intent = getIntent();
        type = intent.getStringExtra(KEY.DUP);
        idContact.addAll(Objects.requireNonNull(intent.getStringArrayListExtra(KEY.LIST_ID)));
        Log.e("111111", "onCreate: dup " + type + idContact);
        for (int i = 0; i < idContact.size(); i++) {
            listContact.add(DBHelper.getInstance(this).getDuplicateContact(String.valueOf(idContact.get(i))));
        }
        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).getFather().equals(KEY.TRUE)) {
                break;
            }
            //addContact
            if (contactList.get(i).getmContact().equals(KEY.FALSE)) {
                contacts.add(new Duplicate(contactList.get(i).getIdTable(),
                        contactList.get(i).getIdContact(),
                        contactList.get(i).getName(),
                        KEY.FALSE, 0,
                        contactList.get(i).getPhones(), contactList.get(i).getEmails()));
            }
            //addName
            if (contactList.get(i).getmName().equals(KEY.FALSE)) {
                names.add(new Duplicate(contactList.get(i).getIdTable(),
                        contactList.get(i).getIdContact(),
                        contactList.get(i).getName(),
                        KEY.FALSE, 0, null, null));
            }
            //add phone
            for (int j = 0; j < contactList.get(i).getPhones().size(); j++) {
                if (contactList.get(i).getPhones().get(j).getmPhone().equals(KEY.FALSE)) {
                    phones.add(new Duplicate(contactList.get(i).getPhones().get(j).getIdTable(),
                            contactList.get(i).getPhones().get(j).getIdContact(),
                            contactList.get(i).getPhones().get(j).getPhone(),
                            KEY.FALSE, 0, null, null));
                }
            }
            //addEmail
            for (int j = 0; j < contactList.get(i).getEmails().size(); j++) {
                if (contactList.get(i).getEmails().get(j).getmEmail().equals(KEY.FALSE)) {
                    emails.add(new Duplicate(contactList.get(i).getEmails().get(j).getIdTable(),
                            contactList.get(i).getEmails().get(j).getIdContact(),
                            contactList.get(i).getEmails().get(j).getEmail(),
                            KEY.FALSE, 0, null, null));
                }
            }

        }
        rd = new Random();
        setTypeContact();
        if (listContact.size() != 0) {
            llEmpty.setVisibility(View.GONE);
        } else {
            txtEmpty.setText("No duplicate " + type + " yet");
        }
        switch (type){
            case "contact":
                duplicateAdapter = new DuplicateAdapter(this, contacts, typeList, type);
                break;
            case "name":
                duplicateAdapter = new DuplicateAdapter(this,names , typeList, type);
                break;
            case "phone":
                duplicateAdapter = new DuplicateAdapter(this,phones , typeList, type);
                break;
            case "email":
                duplicateAdapter = new DuplicateAdapter(this,emails , typeList, type);
                break;
            }

        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(duplicateAdapter);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cbAll.setVisibility(View.GONE);
        llDelete.setVisibility(View.GONE);


    }

    private BroadcastReceiver broadCastUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendBroadcast(new Intent(ManageFragment.ACTION_RELOAD_FRAGMENT_MANAGE));
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadCastUpdate != null) unregisterReceiver(broadCastUpdate);
    }

    private void setTypeContact() {
        switch (type) {
            case "contact":
                txtTitleDelete.setText("Duplicate Contact");
                List<Duplicate> contactList = new ArrayList<>(contacts);
                for (int i = 0; i < contacts.size(); i++) {
                    if (!contacts.get(i).getMerged().equals(KEY.FALSE)) {
                        break;
                    }
                    for (int j = 0; j < contactList.size(); j++) {
                        if (i != j && !contacts.get(i).getContactID().equals(contactList.get(j).getContactID())) {
                            if (contacts.get(i).getName().equalsIgnoreCase(contactList.get(j).getName()) &&
                                    contacts.get(i).getPhone().equals(contacts.get(j).getPhone()) &&
                                    contacts.get(i).getEmail().equals(contacts.get(j).getEmail())) {
                                if (contacts.get(j).getTypeMer() == 0) {
                                    contacts.get(i).setTypeMer(rd.nextInt());
                                    break;
                                }
                                if (contacts.get(j).getTypeMer() != 0) {
                                    contacts.get(i).setTypeMer(contacts.get(j).getTypeMer());
                                    break;
                                }
                            }
                        }
                    }
                }
                for (int i = 0; i < contacts.size(); i++) {
                    if (!typeList.contains(contacts.get(i).getTypeMer())) {
                        typeList.add(contacts.get(i).getTypeMer());
                    }
                }
                break;
            case "name":
                List<Duplicate> nameList = new ArrayList<>(names);
                for (int i = 0; i < names.size(); i++) {
                    if (!names.get(i).getMerged().equals(KEY.FALSE)) {
                        break;
                    }
                    for (int j = 0; j < nameList.size(); j++) {
                        if (i != j && !names.get(i).getContactID().equals(nameList.get(j).getContactID())) {
                            if (names.get(i).getName().equalsIgnoreCase(nameList.get(j).getName())) {
                                Log.e("123123", "loadDub: " + names.get(i).getName() + nameList.get(j).getName());
                                if (names.get(j).getTypeMer() != 0) {
                                    names.get(i).setTypeMer(names.get(j).getTypeMer());
                                    Log.e("123123", "names(i): " + names.get(i).getTypeMer());
                                    break;
                                }
                                if (names.get(j).getTypeMer() == 0) {
                                    names.get(i).setTypeMer(rd.nextInt());
                                    Log.e("123123", "names(i): " + names.get(i).getTypeMer());
                                    break;
                                }
                            }
                        }
                    }
                }
                for (int i = 0; i < names.size(); i++) {
                    if (!typeList.contains(names.get(i).getTypeMer())) {
                        typeList.add(names.get(i).getTypeMer());
                    }
                }
                break;
            case "phone":
                txtTitleDelete.setText("Duplicate Phone");
                List<Duplicate> phoneList = new ArrayList<>(phones);
                for (int i = 0; i < phones.size(); i++) {
                    if (!phones.get(i).getMerged().equals(KEY.FALSE)) {
                        break;
                    }
                    for (int j = 0; j < phoneList.size(); j++) {
                        //khác vị trí và khác id
                        if (i != j && !phones.get(i).getContactID().equals(phoneList.get(j).getContactID())) {
                            //if (phones.get(i).getName().equalsIgnoreCase(phoneList.get(j).getName()) && Integer.parseInt(phones.get(i).getContactID()) != Integer.parseInt(phoneList.get(j).getContactID())) {
                            //trùng sdt
                            if (phones.get(i).getName().equals(phoneList.get(j).getName())) {
                                Log.e("123123", "loadDub: phone i " + phones.get(i).getTypeMer());
                                if (phones.get(j).getTypeMer() != 0) {
                                    phones.get(i).setTypeMer(phones.get(j).getTypeMer());
                                    Log.e("123123", "loadDub: phone i " + phones.get(i).getTypeMer());
                                    break;
                                }
                                if (phones.get(j).getTypeMer() == 0) {
                                    phones.get(i).setTypeMer(rd.nextInt());
                                    Log.e("123123", "loadDub: phone i " + phones.get(i).getTypeMer());
                                    break;
                                }
                            }
                        }
                    }
                }
                for (int i = 0; i < phones.size(); i++) {
                    if (!typeList.contains(phones.get(i).getTypeMer())) {
                        typeList.add(phones.get(i).getTypeMer());
                    }
                }
                break;
            case "email":
                txtTitleDelete.setText("Duplicate Email");
                List<Duplicate> emailList = new ArrayList<>(emails);
                for (int i = 0; i < emails.size(); i++) {
                    if (!emails.get(i).getMerged().equals(KEY.FALSE)) {
                        break;
                    }
                    for (int j = 0; j < emailList.size(); j++) {
                        if (i != j && !emails.get(i).getContactID().equals(emailList.get(j).getContactID())) {
                            if (emails.get(i).getName().equals(emailList.get(j).getName())) {
                                if (emails.get(j).getTypeMer() != 0) {
                                    emails.get(i).setTypeMer(emails.get(j).getTypeMer());
                                    break;
                                }
                                if (emails.get(j).getTypeMer() == 0) {
                                    emails.get(i).setTypeMer(rd.nextInt());
                                    break;
                                }
                            }
                        }
                    }
                }
                for (int i = 0; i < emails.size(); i++) {
                    if (!typeList.contains(emails.get(i).getTypeMer())) {
                        typeList.add(emails.get(i).getTypeMer());
                    }
                }
                break;
            default:
                break;

        }


    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        txtTitleDelete = findViewById(R.id.txtTitleDelete);
        cbAll = findViewById(R.id.cbAll);
        txtBtnDelete = findViewById(R.id.txtBtnDelete);
        txtEmpty = findViewById(R.id.txtEmpty);
        imgBtnDelete = findViewById(R.id.imgBtnDelete);
        rcvList = findViewById(R.id.rcvList);
        llDelete = findViewById(R.id.llDelete);
        llEmpty = findViewById(R.id.llEmpty);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
