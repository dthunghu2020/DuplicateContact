package com.hungdt.test.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Account;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.AccountAdapter;
import com.hungdt.test.view.adapter.ContactAdapter;
import com.hungdt.test.view.adapter.EmailAdapter;
import com.hungdt.test.view.adapter.PhoneAdapter;

import java.io.IOException;
import java.util.List;

public class DetailContactActivity extends AppCompatActivity {

    private ImageView imgContact, imgBack;
    private EditText edtName;
    private Contact contact;
    private TextView txtAccountName, txtAccountNumber;
    private RecyclerView rcvPhone, rcvEmail, rcvAccount;
    private PhoneAdapter phoneAdapter;
    private EmailAdapter emailAdapter;
    private AccountAdapter accountAdapter;
    private ConstraintLayout clPhone, clEmail;
    private CardView cvTitle;

    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        initView();

        Intent intent = getIntent();
        type = intent.getStringExtra(KEY.TYPE);
        assert type != null;
        switch (type) {
            case KEY.DETAIL:
                contact = DBHelper.getInstance(this).getContact(intent.getStringExtra(KEY.ID));
                txtAccountName.setText(contact.getName());
                edtName.setVisibility(View.GONE);
                break;
            case KEY.MERGER:
                contact = (Contact) intent.getSerializableExtra(KEY.CONTACT);
                assert contact != null;
                edtName.setText(contact.getName());
                txtAccountName.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        Glide.with(this)
                .load(contact.getImage())
                .error(R.drawable.ic_code)
                .into(imgContact);


        accountAdapter = new AccountAdapter(this, contact.getAccounts());
        rcvAccount.setLayoutManager(new LinearLayoutManager(this));
        rcvAccount.setAdapter(accountAdapter);

        if (contact.getEmails().size() == 0) {
            clEmail.setVisibility(View.GONE);
        } else {
            emailAdapter = new EmailAdapter(this, contact.getEmails());
            rcvEmail.setLayoutManager(new LinearLayoutManager(this));
            rcvEmail.setAdapter(emailAdapter);
        }
        if (contact.getPhones().size() == 0) {
            clPhone.setVisibility(View.GONE);
        } else {
            txtAccountNumber.setText(contact.getPhones().get(0).getPhone());
            phoneAdapter = new PhoneAdapter(this, contact.getPhones());
            rcvPhone.setLayoutManager(new LinearLayoutManager(this));
            rcvPhone.setAdapter(phoneAdapter);

        }
        if (type.equals(KEY.DETAIL)) {
            imgContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewContact();
                }
            });
            cvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewContact();
                }
            });
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void viewContact() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
                String.valueOf(contact.getIdContact()));
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (type.equals(KEY.MERGER)) {
            if (edtName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Contact Name !", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MergerDuplicateActivity.ACTION_UPDATE_NAME_CONTACT_MERGER);
                intent.putExtra(KEY.RENAME, edtName.getText().toString());
                sendBroadcast(intent);
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void initView() {
        imgContact = findViewById(R.id.imgContact);
        imgBack = findViewById(R.id.imgBack);
        rcvEmail = findViewById(R.id.rcvEmail);
        rcvPhone = findViewById(R.id.rcvPhone);
        rcvAccount = findViewById(R.id.rcvAccount);
        txtAccountName = findViewById(R.id.txtAccountName);
        txtAccountNumber = findViewById(R.id.txtAccountNumber);
        clPhone = findViewById(R.id.clPhone);
        clEmail = findViewById(R.id.clEmail);
        cvTitle = findViewById(R.id.cvTitle);
        edtName = findViewById(R.id.edtName);
    }
}
