package com.hungdt.test.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hungdt.test.R;

import java.util.Objects;

public class AskPermissionActivity extends AppCompatActivity {
    private static final int PERMISSION_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_permission);

        Button btnSet = findViewById(R.id.btnSet);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermissions();
            }
        });

    }


    private void askPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(AskPermissionActivity.this,MainActivity.class);
                startActivity(intent);
                Intent intent2 = new Intent(AskPermissionActivity.this,WaitingActivity.class);
                startActivity(intent2);
                finish();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                // do your work here

                Intent intent = new Intent(AskPermissionActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(this, "All permission accepted!", Toast.LENGTH_SHORT).show();
            } else if (Build.VERSION.SDK_INT >= 23) {
                // User selected the Never Ask Again Option
                if(!shouldShowRequestPermissionRationale(permissions[0]) ||
                        !shouldShowRequestPermissionRationale(permissions[1]) ||
                        !shouldShowRequestPermissionRationale(permissions[2]) ||
                        !shouldShowRequestPermissionRationale(permissions[3])){
                    //Cái này bật setting trên màn hình app
                    openSettingPermissionDialog();
                }
            }
        }
    }

    private void openSettingPermissionDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_qs_yes_no);

        TextView txtTitle = dialog.findViewById(R.id.txtTitleToolBar);
        TextView txtBody = dialog.findViewById(R.id.txtBody);
        final Button btnYes = dialog.findViewById(R.id.btnYes);
        final Button btnNo = dialog.findViewById(R.id.btnNo);

        btnNo.setText("Cancel");
        txtTitle.setText("Setting Permission");
        txtBody.setText("You have deny permission!\nDo you want to turn it back in\nSetting option?!");

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                dialog.dismiss();

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void exitApp () {
        finishAffinity();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }
}
