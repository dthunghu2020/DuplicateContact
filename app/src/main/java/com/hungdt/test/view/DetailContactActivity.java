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
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.ContactAdapter;
import com.hungdt.test.view.adapter.EmailAdapter;
import com.hungdt.test.view.adapter.PhoneAdapter;

import java.io.IOException;
import java.util.List;

public class DetailContactActivity extends AppCompatActivity {

    private ImageView imgContact;
    private TextView txtName, txtAccount;
    private Contact contact ;
    private RecyclerView rcvPhone, rcvEmail ;
    private PhoneAdapter phoneAdapter;
    private EmailAdapter emailAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        imgContact = findViewById(R.id.imgContact);
        txtName = findViewById(R.id.txtName);
        rcvEmail = findViewById(R.id.rcvEmail);
        txtAccount = findViewById(R.id.txtAccount);
        rcvPhone = findViewById(R.id.rcvPhone);


        Intent intent = getIntent();
         contact = DBHelper.getInstance(this).getContact(intent.getStringExtra(KEY.ID));


        Glide.with(this)
                .load(contact.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(imgContact);

        Log.e("123", "onCreate: "+contact );

        txtName.setText(contact.getName());
        for (int i = 0; i < contact.getAccount().size(); i++) {
            txtAccount.append(contact.getAccount().get(i)+"\n");
        }

        emailAdapter = new EmailAdapter(this, contact.getEmail());
        rcvEmail.setLayoutManager(new LinearLayoutManager(this));
        rcvEmail.setAdapter(emailAdapter);

        phoneAdapter = new PhoneAdapter(this, contact.getPhone());
        rcvPhone.setLayoutManager(new LinearLayoutManager(this));
        rcvPhone.setAdapter(phoneAdapter);

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

    }

}
