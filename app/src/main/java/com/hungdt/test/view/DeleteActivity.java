package com.hungdt.test.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.DeleteAdapter;
import com.unity3d.ads.UnityAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.hungdt.test.view.MainActivity.contactList;

public class DeleteActivity extends AppCompatActivity {
    private ImageView imgBack;
    private TextView txtTitleDelete,txtEmpty;
    private CheckBox cbAll;
    private LinearLayout llDelete, llButtonDelete,llEmpty;
    private RecyclerView rcvDelete;
    private List<Contact> contacts = new ArrayList<>();
    private DeleteAdapter deleteAdapter;
    private String type;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initView();

        Ads.initNativeGgFb((LinearLayout) findViewById(R.id.lnNative), this, true);
        Intent intent = getIntent();
        type = intent.getStringExtra(KEY.DELETE);
        getContactFromDB();

        if(contacts.size()!=0){
            llEmpty.setVisibility(View.GONE);
        }else {
            switch (type) {
                case "noName":
                case "noPhone":
                case "noEmail":
                    txtEmpty.setText("No Contacts");
                    break;
                default:
                    break;
            }
        }
        deleteAdapter = new DeleteAdapter(this, contacts);
        rcvDelete.setLayoutManager(new LinearLayoutManager(this));
        rcvDelete.setAdapter(deleteAdapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cbAll.isChecked()) {
                    cbAll.setChecked(false);
                    setAllUnChecked();
                } else {
                    cbAll.setChecked(true);
                    setAllChecked();
                }
                deleteAdapter.notifyDataSetChanged();
            }
        });

        if (contacts.size() == 0) {
            llDelete.setVisibility(View.GONE);
        }

        llButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog();
            }
        });
    }

    private void setAllChecked() {
        for (int i = 0; i < contacts.size(); i++) {
            contacts.get(i).setTicked(true);
        }
    }

    private void setAllUnChecked() {
        for (int i = 0; i < contacts.size(); i++) {
            contacts.get(i).setTicked(false);
        }
    }

    private void getContactFromDB() {
        assert type != null;
        switch (type) {
            case "noName":
                txtTitleDelete.setText("No Name");
                contacts.addAll(DBHelper.getInstance(DeleteActivity.this).getContactNoName());
                break;
            case "noPhone":
                txtTitleDelete.setText("No Phone");
                contacts.addAll(DBHelper.getInstance(DeleteActivity.this).getContactNoPhone());
                break;
            case "noEmail":
                txtTitleDelete.setText("No Emails");
                contacts.addAll(DBHelper.getInstance(DeleteActivity.this).getContactNoEmail());
                break;
            default:
                break;
        }
        Collections.sort(contacts);
    }

    private void openDeleteDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_qs_yes_no);

        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < contacts.size(); i++) {
                    if (contacts.get(i).isTicked()) {
                        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
                        String[] args = new String[]{contacts.get(i).getIdContact()};
                        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args).build());
                        try {
                            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                        } catch (RemoteException | OperationApplicationException e) {
                            e.printStackTrace();
                        }
                        DBHelper.getInstance(DeleteActivity.this).deleteContact(contacts.get(i).getIdContact());
                        contactList.remove(contacts.get(i));
                    }
                }
                contacts.clear();
                getContactFromDB();
                deleteAdapter.notifyDataSetChanged();
                sendBroadcast(new Intent(DeleteFragment.ACTION_UPDATE_DELETE_FRAGMENT));
                sendBroadcast(new Intent(ContactFragment.ACTION_UPDATE_LIST_CONTACT));
                sendBroadcast(new Intent(ManageFragment.ACTION_RELOAD_FRAGMENT_MANAGE));
                if (MainActivity.ggInterstitialAd != null && MainActivity.ggInterstitialAd.isLoaded())
                    MainActivity.ggInterstitialAd.show();
                else if (UnityAds.isInitialized() && UnityAds.isReady(getString(R.string.INTER_UNI)))
                    UnityAds.show(DeleteActivity.this, getString(R.string.INTER_UNI));

                Toast.makeText(DeleteActivity.this, "Delete Success!!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        txtTitleDelete = findViewById(R.id.txtTitleDelete);
        cbAll = findViewById(R.id.cbAll);
        llDelete = findViewById(R.id.llDelete);
        llButtonDelete = findViewById(R.id.llButtonDelete);
        rcvDelete = findViewById(R.id.rcvList);
        llEmpty = findViewById(R.id.llEmpty);
        txtEmpty = findViewById(R.id.txtEmpty);
    }

    private void setDefaultTick() {
        for (int i = 0; i < contacts.size(); i++) {
            contacts.get(i).setTicked(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setDefaultTick();
    }
}
