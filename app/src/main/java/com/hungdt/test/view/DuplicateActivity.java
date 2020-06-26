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

import static com.hungdt.test.view.MainActivity.showInterstitial;

public class DuplicateActivity extends AppCompatActivity {
    private ImageView imgBack, imgBtnDelete;
    private TextView txtTitleDelete, txtBtnDelete;
    private CheckBox cbAll;
    private LinearLayout llDelete;
    private RecyclerView rcvList;
    private List<Contact> contacts = new ArrayList<>();
    private DuplicateAdapter duplicateAdapter;
    private String type;
    private int kind = 1;
    private List<String> idContact = new ArrayList<>();
    private ArrayList<Integer> typeList = new ArrayList<>();
    public static final String ACTION_FINISH_ACTIVITY= "finishDuplicateActivity";

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
        registerReceiver(broadCastUpdate,intentFilter);

        Intent intent = getIntent();
        type = intent.getStringExtra(KEY.DUP);
        idContact.addAll(Objects.requireNonNull(intent.getStringArrayListExtra(KEY.LIST_ID)));
        Log.e("111111", "onCreate: dup "+type +idContact);
        for (int i = 0; i < idContact.size(); i++) {
            contacts.add(DBHelper.getInstance(this).getDuplicateContact(String.valueOf(idContact.get(i))));
        }

        setTypeContact();
        Collections.sort(contacts);
        Log.e("111111", "onCreate:typeList "+typeList);
        duplicateAdapter = new DuplicateAdapter(this, contacts, typeList, type);
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
        if (broadCastUpdate!=null)unregisterReceiver(broadCastUpdate);
    }

    private void setTypeContact() {
        List<Contact> contactList = new ArrayList<>(contacts);
        switch (type) {
            case "contact":
                txtTitleDelete.setText("Duplicate Contact");
                for (int i = 0; i < contacts.size(); i++) {
                    for (int j = 0; j < contactList.size(); j++) {
                        if (i != j) {
                            if (contacts.get(i).getName().equalsIgnoreCase(contactList.get(j).getName())) {
                                if (contacts.get(j).getType() == 0) {
                                    contacts.get(i).setType(kind);
                                    kind++;
                                    break;
                                }
                                if (contacts.get(j).getType() != 0) {
                                    contacts.get(i).setType(contacts.get(j).getType());
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case "phone":
                txtTitleDelete.setText("Duplicate Phone");
                for (int i = 0; i < contacts.size(); i++) {
                    for (int j = 0; j < contactList.size(); j++) {
                        if (i != j) {
                            if (contacts.get(i).getPhones().equals(contactList.get(j).getPhones())) {
                                if (contacts.get(j).getType() == 0) {
                                    contacts.get(i).setType(kind);
                                    kind++;
                                    break;
                                }
                                if (contacts.get(j).getType() != 0) {
                                    contacts.get(i).setType(contacts.get(j).getType());
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case "email":
                txtTitleDelete.setText("Duplicate Email");
                for (int i = 0; i < contacts.size(); i++) {
                    for (int j = 0; j < contactList.size(); j++) {
                        if (i != j) {
                            if (contacts.get(i).getEmails().equals(contactList.get(j).getEmails())) {
                                if (contacts.get(j).getType() == 0) {
                                    contacts.get(i).setType(kind);
                                    kind++;
                                    break;
                                }
                                if (contacts.get(j).getType() != 0) {
                                    contacts.get(i).setType(contacts.get(j).getType());
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case "name":
                txtTitleDelete.setText("Duplicate Name");
                for (int i = 0; i < contacts.size(); i++) {
                    for (int j = 0; j < contactList.size(); j++) {
                        if (i != j) {
                            if (contacts.get(i).getName().equalsIgnoreCase(contactList.get(j).getName())) {
                                if (contacts.get(j).getType() == 0) {
                                    contacts.get(i).setType(kind);
                                    kind++;
                                    break;
                                }
                                if (contacts.get(j).getType() != 0) {
                                    contacts.get(i).setType(contacts.get(j).getType());
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            default:
                break;

        }
        for (int i = 0; i < contacts.size(); i++) {
            Log.i("123123", "onCreate: " + contacts.get(i).getName() + contacts.get(i).getType());
            if (!typeList.contains(contacts.get(i).getType())) {
                typeList.add(contacts.get(i).getType());
            }
        }

    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        txtTitleDelete = findViewById(R.id.txtTitleDelete);
        cbAll = findViewById(R.id.cbAll);
        txtBtnDelete = findViewById(R.id.txtBtnDelete);
        imgBtnDelete = findViewById(R.id.imgBtnDelete);
        rcvList = findViewById(R.id.rcvList);
        llDelete = findViewById(R.id.llDelete);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ContactConfig.getInstance().getConfig().getBoolean("config_on")) {
            if (!showInterstitial()) {
                if (UnityAds.isInitialized() && UnityAds.isReady(getString(R.string.INTER_UNI)))
                    UnityAds.show(DuplicateActivity.this, getString(R.string.INTER_UNI));
            }
        }
        finish();
    }
}
