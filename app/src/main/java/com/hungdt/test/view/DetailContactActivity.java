package com.hungdt.test.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hungdt.test.R;

import java.io.IOException;

public class DetailContactActivity extends AppCompatActivity {

    private ImageView imgContact;
    private TextView txtName,txtPhone;
    private Button btnCall,btnSMS;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        imgContact = findViewById(R.id.imgContact);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        btnCall = findViewById(R.id.btnCall);
        btnSMS = findViewById(R.id.btnSMS);


        Intent intent =getIntent();
        String name = intent.getStringExtra("name");
        String image = intent.getStringExtra("image");
        String phone = intent.getStringExtra("phone");

        if(image!=null){
            imgContact.setImageBitmap(getPhotoFromURI(image));
        }else {
            imgContact.setImageResource(R.drawable.ic_launcher_background);
        }

        txtName.setText(name);
        txtPhone.setText(phone);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailContactActivity.this, "Call", Toast.LENGTH_SHORT).show();
            }
        });

        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailContactActivity.this, "SMS", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Bitmap getPhotoFromURI(String photoUri){
        if(photoUri !=null){
            try {
                return MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(photoUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
