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
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.DeleteAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DuplicateActivity extends AppCompatActivity {
    private ImageView imgBack;
    private TextView txtTitleDelete;
    private CheckBox cbAll;
    private LinearLayout llDelete,llButtonDelete;
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

        Intent intent = getIntent();
        type = intent.getStringExtra(KEY.DUP);

        contacts.addAll(DBHelper.getInstance(this).getAllContact());
        for(int i =0;i<contacts.size();i++){
            for(int j = 0; j< contacts.size();j++){
                if(i!=j){

                }
            }
        }
        getContactFromDB();
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

        if(contacts.size()==0){
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
        switch (type){
            case "contact":
                break;
            case "no6":
                break;
            case "phone":
                break;
            case "email":
                break;
            case "name":
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
                for(int i = 0 ; i< contacts.size();i++){
                    if(contacts.get(i).isTicked()){
                        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
                        String[] args = new String[] {contacts.get(i).getIdContact()};
                        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args).build());
                        try {
                            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                        } catch (RemoteException | OperationApplicationException e) {
                            e.printStackTrace();
                        }
                        DBHelper.getInstance(DuplicateActivity.this).deleteContact(contacts.get(i).getId());
                    }
                }
                contacts.clear();
                getContactFromDB();
                deleteAdapter.notifyDataSetChanged();
                Toast.makeText(DuplicateActivity.this, "Delete Success!!!", Toast.LENGTH_SHORT).show();
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
        imgBack= findViewById(R.id.imgBack);
        txtTitleDelete= findViewById(R.id.txtTitleDelete);
        cbAll= findViewById(R.id.cbAll);
        llDelete= findViewById(R.id.llDelete);
        llButtonDelete= findViewById(R.id.llButtonDelete);
        rcvDelete= findViewById(R.id.rcvDelete);
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
