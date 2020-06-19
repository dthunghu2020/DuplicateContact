package com.hungdt.test.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Account;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.DuplicateContactAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MergerDuplicateActivity extends AppCompatActivity {
    private static final int REQUEST_CONTACT_CODE = 100;
    private String type;
    private List<String> idContact = new ArrayList<>();
    private List<String> listPhones = new ArrayList<>();
    private List<String> listEmails = new ArrayList<>();
    private List<Contact> contacts = new ArrayList<>();
    private Contact contactMerger;
    private ImageView imgContact, imgBack;
    private TextView txtContactName;
    private RecyclerView rcvDupContact;
    private ConstraintLayout clContactMerger;
    private DuplicateContactAdapter dupContactAdapter;
    private LinearLayout llButtonMerger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merger_duplicate);

        rcvDupContact = findViewById(R.id.rcvDupContact);
        imgContact = findViewById(R.id.imgContact);
        imgBack = findViewById(R.id.imgBack);
        txtContactName = findViewById(R.id.txtContactName);
        clContactMerger = findViewById(R.id.clContactMerger);
        llButtonMerger = findViewById(R.id.llButtonMerger);

        final Intent intent = getIntent();
        type = intent.getStringExtra(KEY.DUP);


        assert type != null;
        if (type.equals("contact") || type.equals("name") || type.equals("email") || type.equals("phone")) {
            idContact.addAll(Objects.requireNonNull(intent.getStringArrayListExtra(KEY.LIST_ID)));
            for (int i = 0; i < idContact.size(); i++) {
                contacts.add(DBHelper.getInstance(this).getDubContact(String.valueOf(idContact.get(i))));
                txtContactName.setText(contacts.get(0).getName());
            }
            Collections.sort(contacts);
            for (int i = 0; i < contacts.size(); i++) {
                listPhones.addAll(contacts.get(i).getPhone());
                listEmails.addAll(contacts.get(i).getEmail());
            }
            ArrayList<String> listP = new ArrayList<>();
            for (int i = 0; i < listPhones.size(); i++) {
                if (!listP.contains(listPhones.get(i))) {
                    listP.add(listPhones.get(i));
                }
            }
            listPhones.clear();
            listPhones.addAll(listP);
            ArrayList<String> listE = new ArrayList<>();
            for (int i = 0; i < listEmails.size(); i++) {
                if (!listE.contains(listEmails.get(i))) {
                    listE.add(listEmails.get(i));
                }
            }
            listEmails.clear();
            listEmails.addAll(listE);
            List<Account> accounts = new ArrayList<>();
            accounts.add(contacts.get(0).getAccount().get(0));
            Random rd = new Random();
            int number = rd.nextInt();
            Log.e("123123", "onCreate: "+listPhones+listEmails);
            Log.e("123123", "onCreate: "+listP+listE);
            contactMerger = new Contact(String.valueOf(number), "0", contacts.get(0).getName(), "image", "0", KEY.TRUE, KEY.TRUE, KEY.FALSE, listPhones, accounts, listEmails);
        } else {
            contacts.addAll(DBHelper.getInstance(this).getContactMerged(type));
            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).getFather().equals(KEY.TRUE)) {
                    txtContactName.setText(contacts.get(i).getName());
                    contactMerger = contacts.get(i);
                }
            }
            contacts.remove(contactMerger);
        }


        imgContact.setImageResource(R.drawable.ic_code);


        dupContactAdapter = new DuplicateContactAdapter(this, contacts);
        rcvDupContact.setLayoutManager(new LinearLayoutManager(this));
        rcvDupContact.setAdapter(dupContactAdapter);

        clContactMerger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MergerDuplicateActivity.this, DetailContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY.CONTACT, contactMerger);
                intent1.putExtras(bundle);
                intent1.putExtra(KEY.TYPE, KEY.MERGER);
                startActivityForResult(intent1, REQUEST_CONTACT_CODE);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        llButtonMerger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rd = new Random();
                int number = rd.nextInt();
                for (int i = 0; i < contacts.size(); i++) {
                    DBHelper.getInstance(MergerDuplicateActivity.this).updateContactMerger(contacts.get(i).getId(), String.valueOf(number));
                }
                DBHelper.getInstance(MergerDuplicateActivity.this).addContact(contactMerger.getIdContact(), contactMerger.getName(), contactMerger.getImage(), contactMerger.getLastCT(), String.valueOf(number), KEY.TRUE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE);
                //todo
                Toast.makeText(MergerDuplicateActivity.this, "Save to Merger!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONTACT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                contactMerger.setName(data.getStringExtra(KEY.RESULT));
                txtContactName.setText(data.getStringExtra(KEY.RESULT));
            }
        }
    }
}
