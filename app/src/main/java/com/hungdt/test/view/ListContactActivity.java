package com.hungdt.test.view;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.test.R;
import com.hungdt.test.model.Contact;
import com.hungdt.test.view.adapter.ContactAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListContactActivity extends AppCompatActivity {
    List<Contact> contactList = new ArrayList<>();
    ContactAdapter contactAdapter;
    RecyclerView rcvContactView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact);

        rcvContactView = findViewById(R.id.rcvListContact);

        getContact();


        contactAdapter = new ContactAdapter(this, contactList);
        rcvContactView.setLayoutManager(new LinearLayoutManager(this));
        rcvContactView.setAdapter(contactAdapter);

    }

    private void getContact() {
        String[] projections = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projections, null, null, null);

        assert cursor != null;
        int nameIndex = cursor.getColumnIndex(projections[0]);
        int photoIndex = cursor.getColumnIndex(projections[1]);
        int numberIndex = cursor.getColumnIndex(projections[2]);

        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            String name = cursor.getString(nameIndex);
            String image = cursor.getString(photoIndex);
            String phone = cursor.getString(numberIndex);
            contactList.add(new Contact(image,name,phone));
        }
        cursor.close();

    }
}
