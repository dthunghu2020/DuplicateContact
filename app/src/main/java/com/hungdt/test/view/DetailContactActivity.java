package com.hungdt.test.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.hungdt.test.R;

import java.io.IOException;

public class DetailContactActivity extends AppCompatActivity {

    private ImageView imgContact;
    private TextView txtName, txtPhone;
    private Button btnCall, btnSMS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        imgContact = findViewById(R.id.imgContact);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        btnCall = findViewById(R.id.btnCall);
        btnSMS = findViewById(R.id.btnSMS);


        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String image = intent.getStringExtra("image");
        final String phone = intent.getStringExtra("phone");


        Glide.with(this)
                .load(image)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(imgContact);

        txtName.setText(name);
        txtPhone.setText(phone);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_CALL);
                intentCall.setData(Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(DetailContactActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intentCall);
                } else {
                    Toast.makeText(DetailContactActivity.this, "Please Grant Permission!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("sms:"+phone));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }*/
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("smsto:" + phone)); // This ensures only SMS apps respond
                startActivity(intent);
            }
        });
    }

}
