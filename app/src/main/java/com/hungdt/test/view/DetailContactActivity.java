package com.hungdt.test.view;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

    private ImageView imgContact,imgBack;
    private Contact contact ;
    private TextView txtAccountName,txtAccountNumber;
    private RecyclerView rcvPhone, rcvEmail,rcvAccount;
    private PhoneAdapter phoneAdapter;
    private EmailAdapter emailAdapter;
    private AccountAdapter accountAdapter;
    private ConstraintLayout clPhone,clEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        initView();

        Intent intent = getIntent();
         contact = DBHelper.getInstance(this).getContact(intent.getStringExtra(KEY.ID));


        Glide.with(this)
                .load(contact.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.color.colorAccent)
                .into(imgContact);

        Log.e("123", "onCreate: "+contact );

        txtAccountName.setText(contact.getName());

        accountAdapter = new AccountAdapter(this, contact.getAccount());
        rcvAccount.setLayoutManager(new LinearLayoutManager(this));
        rcvAccount.setAdapter(accountAdapter);

        if(contact.getEmail().size()==0){
            clEmail.setVisibility(View.GONE);
        }else {
            emailAdapter = new EmailAdapter(this, contact.getEmail());
            rcvEmail.setLayoutManager(new LinearLayoutManager(this));
            rcvEmail.setAdapter(emailAdapter);
        }
        if(contact.getPhone().size()==0){
            clPhone.setVisibility(View.GONE);
        }else {
            txtAccountNumber.setText(contact.getPhone().get(0));
            phoneAdapter = new PhoneAdapter(this, contact.getPhone());
            rcvPhone.setLayoutManager(new LinearLayoutManager(this));
            rcvPhone.setAdapter(phoneAdapter);

        }
        imgContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
                        String.valueOf(contact.getIdContact()));
                intent.setData(uri);
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
    }

}
