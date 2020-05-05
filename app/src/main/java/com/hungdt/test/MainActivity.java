package com.hungdt.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int FILE_SHARE_PERMISSION = 102;

    private EditText edtTextQR;
    private Button btnCreateQR, btnScanQR, btnShare;
    private ImageView imgQR;
    private TextView txtScanQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTextQR = findViewById(R.id.edtTextQR);
        btnCreateQR = findViewById(R.id.btnCreateQR);
        btnScanQR = findViewById(R.id.btnScanQR);
        btnShare = findViewById(R.id.btnShare);
        imgQR = findViewById(R.id.imgQR);
        txtScanQR = findViewById(R.id.txtScanQR);

        //Generate
        final MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        btnCreateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(edtTextQR.getText().toString(), BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imgQR.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        //Scan

        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission(Manifest.permission.CAMERA)) {
                        openScanner();
                    } else {
                        requestPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                    }
                } else {
                    openScanner();
                }
            }
        });

        //Share

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        shareQrCode();
                    } else {
                        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, FILE_SHARE_PERMISSION);
                    }
                } else {
                    shareQrCode();
                }
            }
        });


    }

    private void shareQrCode() {
        imgQR.setDrawingCacheEnabled(true);
        Bitmap bitmap = imgQR.getDrawingCache();
        File file = new File(Environment.getExternalStorageDirectory(), "bar_code.jpg");
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 200, fileOutputStream);
            fileOutputStream.close();

            //sharing File
            Intent intent = new Intent(Intent.ACTION_SEND);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(MainActivity.this, "com.hungdt.test", file));
            } else {
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            }
            intent.setType("images/*");
            startActivity(intent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openScanner() {
        new IntentIntegrator(MainActivity.this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Blank", Toast.LENGTH_SHORT).show();
            } else {
                txtScanQR.setText("Data: " + result.getContents());
            }
        } else Toast.makeText(this, "Blank", Toast.LENGTH_SHORT).show();
    }

    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(String permission, int code) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openScanner();
                }
                break;
        }

    }
}
