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
import com.hungdt.test.model.ContactBum;
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
    private List<Contact> listContacts = new ArrayList<>();
    private List<Duplicate> names = new ArrayList<>();
    private List<Duplicate> phones = new ArrayList<>();
    private List<Duplicate> emails = new ArrayList<>();
    private List<Duplicate> contacts = new ArrayList<>();
    private List<ContactBum> contactBums = new ArrayList<>();
    private DuplicateAdapter duplicateAdapter;
    private String type;
    private List<String> idContact = new ArrayList<>();
    private ArrayList<String> typeList = new ArrayList<>();
    public static final String ACTION_FINISH_ACTIVITY = "finishDuplicateActivity";
    private Random rd = new Random();

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
            for (int j = 0; j < contactList.size(); j++) {
                if (idContact.get(i).equals(contactList.get(j).getIdContact())) {
                    listContacts.add(contactList.get(j));
                }
            }
        }
        if (listContacts.size() != 0) {
            llEmpty.setVisibility(View.GONE);
        } else {
            switch (type) {
                case "contact":
                    txtEmpty.setText("No Duplicate Contacts Yet");
                    break;
                case "name":
                    txtEmpty.setText("No Similar Names Yet");
                    break;
                case "phone":
                    txtEmpty.setText("No Duplicate Phone Numbers Yet");
                    break;
                case "email":
                    txtEmpty.setText("No Duplicate Emails Yet");
                    break;
            }

        }

        switch (type) {
            case "contact":
                txtTitleDelete.setText("Duplicate Contacts");
                for (int i = 0; i < listContacts.size(); i++) {
                    contactBums.add(new ContactBum(listContacts.get(i).getIdContact(),
                            listContacts.get(i).getName(),
                            listContacts.get(i).getName(),
                            listContacts.get(i).getPhones(),
                            listContacts.get(i).getEmails()));
                }
                ArrayList<String> bumC = new ArrayList<>();
                for (int i = 0; i < contactBums.size(); i++) {
                    for (int j = 0; j < contactBums.size(); j++) {
                        if (i == j) {
                            break;
                        }
                        if (contactBums.get(i).getName().equals(contactBums.get(j).getName())) {
                            if (!bumC.contains(contactBums.get(i).getName())) {
                                bumC.add(contactBums.get(i).getName());
                            }
                        }
                    }

                }
                Log.e("123", "onCreate: bumC " + bumC);
                for (int i = 0; i < contactBums.size(); i++) {
                    Log.e("123", "before: " + contactBums.get(i).getBum());
                    for (int j = 0; j < bumC.size(); j++) {
                        if (contactBums.get(i).getName().equals(bumC.get(j))) {
                            contactBums.get(i).setBum(bumC.get(j));
                        }
                    }
                    Log.e("123", "apter: " + contactBums.get(i).getBum());
                }
                break;
            case "name":
                txtTitleDelete.setText("Similar Names");
                for (int i = 0; i < listContacts.size(); i++) {
                    contactBums.add(new ContactBum(listContacts.get(i).getIdContact(),
                            listContacts.get(i).getName(),
                            listContacts.get(i).getName(),
                            null,
                            null));
                }
                ArrayList<String> bumN = new ArrayList<>();
                for (int i = 0; i < contactBums.size(); i++) {
                    for (int j = 0; j < contactBums.size(); j++) {
                        if (i == j) {
                            break;
                        }
                        if (contactBums.get(i).getName().equalsIgnoreCase(contactBums.get(j).getName())) {
                            if (!bumN.contains(contactBums.get(i).getName())) {
                                bumN.add(contactBums.get(i).getName());
                            }
                        }
                    }

                }
                Log.e("123", "onCreate: bumN " + bumN);
                for (int i = 0; i < contactBums.size(); i++) {
                    Log.e("123", "before: " + contactBums.get(i).getBum());
                    for (int j = 0; j < bumN.size(); j++) {
                        if (contactBums.get(i).getName().equalsIgnoreCase(bumN.get(j))) {
                            contactBums.get(i).setBum(bumN.get(j));
                        }
                    }
                    Log.e("123", "apter: " + contactBums.get(i).getBum());
                }
                break;
            case "phone":
                txtTitleDelete.setText("Duplicate Phone Numbers");
                for (int i = 0; i < listContacts.size(); i++) {
                    for (int j = 0; j < listContacts.get(i).getPhones().size(); j++) {
                        contactBums.add(new ContactBum(listContacts.get(i).getIdContact(),
                                listContacts.get(i).getName(),
                                listContacts.get(i).getPhones().get(j).getPhone(),
                                null,
                                null));
                    }
                }
                ArrayList<String> bumP = new ArrayList<>();
                for (int i = 0; i < contactBums.size(); i++) {
                    for (int j = 0; j < contactBums.size(); j++) {
                        if (i == j) {
                            break;
                        }
                        if (contactBums.get(i).getName().equals(contactBums.get(j).getName())) {
                            if (!bumP.contains(contactBums.get(i).getName())) {
                                bumP.add(contactBums.get(i).getName());
                            }
                        }
                    }

                }
                Log.e("123", "onCreate: bumP " + bumP);
                for (int i = 0; i < contactBums.size(); i++) {
                    Log.e("123", "before: " + contactBums.get(i).getBum());
                    for (int j = 0; j < bumP.size(); j++) {
                        if (contactBums.get(i).getName().equals(bumP.get(j))) {
                            contactBums.get(i).setBum(bumP.get(j));
                        }
                    }
                    Log.e("123", "apter: " + contactBums.get(i).getBum());
                }
                break;
            case "email":
                txtTitleDelete.setText("Duplicate Emails");
                for (int i = 0; i < listContacts.size(); i++) {
                    for (int j = 0; j < listContacts.get(i).getEmails().size(); j++) {
                        contactBums.add(new ContactBum(listContacts.get(i).getIdContact(),
                                listContacts.get(i).getName(),
                                listContacts.get(i).getEmails().get(j).getEmail(),
                                null,
                                null));
                    }
                }
                ArrayList<String> bumE = new ArrayList<>();
                for (int i = 0; i < contactBums.size(); i++) {
                    for (int j = 0; j < contactBums.size(); j++) {
                        if (i == j) {
                            break;
                        }
                        if (contactBums.get(i).getName().equals(contactBums.get(j).getName())) {
                            if (!bumE.contains(contactBums.get(i).getName())) {
                                bumE.add(contactBums.get(i).getName());
                            }
                        }
                    }

                }
                Log.e("123", "onCreate: bumP " + bumE);
                for (int i = 0; i < contactBums.size(); i++) {
                    Log.e("123", "before: " + contactBums.get(i).getBum());
                    for (int j = 0; j < bumE.size(); j++) {
                        if (contactBums.get(i).getName().equals(bumE.get(j))) {
                            contactBums.get(i).setBum(bumE.get(j));
                        }
                    }
                    Log.e("123", "apter: " + contactBums.get(i).getBum());
                }
                break;
        }
        ArrayList<ContactBum> contacts = new ArrayList<>();
        for (int i = 0; i < contactBums.size(); i++) {
            if (contactBums.get(i).getBum() != null) {
                contacts.add(contactBums.get(i));
            }
        }
        contactBums.clear();
        contactBums.addAll(contacts);
        for (int i = 0; i < contactBums.size(); i++) {
            Log.e("123", "onCreate: contactbum" + contactBums.get(i).getName() + " /" + contactBums.get(i).getBum());
            if (!typeList.contains(contactBums.get(i).getBum())) {
                typeList.add(contactBums.get(i).getBum());
            }
        }
        duplicateAdapter = new DuplicateAdapter(this, contactBums, typeList, type);
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
            //sendBroadcast(new Intent(ManageFragment.ACTION_RELOAD_FRAGMENT_MANAGE));
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

                List<Duplicate> contactList = new ArrayList<>(contacts);
                for (int i = 0; i < contacts.size(); i++) {
                    if (!contacts.get(i).getMerged().equals(KEY.FALSE)) {
                        break;
                    }
                    for (int j = 0; j < contactList.size(); j++) {
                        if (i != j && !contacts.get(i).getContactID().equals(contactList.get(j).getContactID())) {
                            if (contacts.get(i).getName().equals(contactList.get(j).getName()) &&
                                    contacts.get(i).getPhone().equals(contacts.get(j).getPhone()) &&
                                    contacts.get(i).getEmail().equals(contacts.get(j).getEmail())) {
                                if (!contacts.get(j).getTypeMer().isEmpty()) {
                                    contacts.get(i).setTypeMer(contacts.get(j).getTypeMer());
                                    break;
                                }
                                if (contacts.get(j).getTypeMer().isEmpty()) {
                                    contacts.get(i).setTypeMer(contacts.get(i).getName());
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
                        //khác vị trí và khác id
                        if (i != j && !names.get(i).getContactID().equals(nameList.get(j).getContactID())) {
                            //if (phones.get(i).getName().equalsIgnoreCase(phoneList.get(j).getName()) && Integer.parseInt(phones.get(i).getContactID()) != Integer.parseInt(phoneList.get(j).getContactID())) {

                            if (names.get(i).getName().equalsIgnoreCase(nameList.get(j).getName())) {
                                Log.e("123123", "loadDub: phone i " + names.get(i).getTypeMer());
                                if (!names.get(j).getTypeMer().isEmpty()) {
                                    names.get(i).setTypeMer(names.get(j).getTypeMer());
                                    Log.e("123123", "loadDub: phone i " + names.get(i).getTypeMer());
                                    break;
                                }
                                if (names.get(j).getTypeMer().isEmpty()) {
                                    names.get(i).setTypeMer(names.get(i).getName());
                                    Log.e("123123", "loadDub: phone i " + names.get(i).getTypeMer());
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
                                if (!phones.get(j).getTypeMer().isEmpty()) {
                                    phones.get(i).setTypeMer(phones.get(j).getTypeMer());
                                    Log.e("123123", "loadDub: phone i " + phones.get(i).getTypeMer());
                                    break;
                                }
                                if (phones.get(j).getTypeMer().isEmpty()) {
                                    phones.get(i).setTypeMer(phones.get(i).getName());
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
                List<Duplicate> emailList = new ArrayList<>(emails);
                for (int i = 0; i < emails.size(); i++) {
                    if (!emails.get(i).getMerged().equals(KEY.FALSE)) {
                        break;
                    }
                    for (int j = 0; j < emailList.size(); j++) {
                        //khác vị trí và khác id
                        if (i != j && !emails.get(i).getContactID().equals(emailList.get(j).getContactID())) {
                            //if (phones.get(i).getName().equalsIgnoreCase(phoneList.get(j).getName()) && Integer.parseInt(phones.get(i).getContactID()) != Integer.parseInt(phoneList.get(j).getContactID())) {
                            //trùng sdt
                            if (emails.get(i).getName().equals(emailList.get(j).getName())) {
                                if (!emails.get(j).getTypeMer().isEmpty()) {
                                    emails.get(i).setTypeMer(emails.get(j).getTypeMer());
                                    break;
                                }
                                if (emails.get(j).getTypeMer().isEmpty()) {
                                    emails.get(i).setTypeMer(emails.get(i).getName());
                                    Log.e("123123", "loadDub: phone i " + emails.get(i).getTypeMer());
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
