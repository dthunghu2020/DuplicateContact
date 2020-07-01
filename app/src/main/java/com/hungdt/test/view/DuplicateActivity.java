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
    private List<ContactBum> contactBums = new ArrayList<>();
    private DuplicateAdapter duplicateAdapter;
    private String type;
    private List<String> idContact = new ArrayList<>();
    private ArrayList<String> typeList = new ArrayList<>();
    public static final String ACTION_FINISH_ACTIVITY = "finishDuplicateActivity";

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
        idContact.addAll(intent.getStringArrayListExtra(KEY.LIST_ID));
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
                for (int i = 0; i < contactBums.size(); i++) {
                    for (int j = 0; j < bumC.size(); j++) {
                        if (contactBums.get(i).getName().equals(bumC.get(j))) {
                            contactBums.get(i).setBum(bumC.get(j));
                        }
                    }
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
                for (int i = 0; i < contactBums.size(); i++) {
                    for (int j = 0; j < bumN.size(); j++) {
                        if (contactBums.get(i).getName().equalsIgnoreCase(bumN.get(j))) {
                            contactBums.get(i).setBum(bumN.get(j));
                        }
                    }
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
                for (int i = 0; i < contactBums.size(); i++) {
                    for (int j = 0; j < bumP.size(); j++) {
                        if (contactBums.get(i).getName().equals(bumP.get(j))) {
                            contactBums.get(i).setBum(bumP.get(j));
                        }
                    }
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
                for (int i = 0; i < contactBums.size(); i++) {
                    for (int j = 0; j < bumE.size(); j++) {
                        if (contactBums.get(i).getName().equals(bumE.get(j))) {
                            contactBums.get(i).setBum(bumE.get(j));
                        }
                    }
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
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadCastUpdate != null) unregisterReceiver(broadCastUpdate);
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
