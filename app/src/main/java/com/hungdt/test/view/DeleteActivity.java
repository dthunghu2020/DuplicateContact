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

public class DeleteActivity extends AppCompatActivity {
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
        type = intent.getStringExtra(KEY.DELETE);
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
            case "no3":
                txtTitleDelete.setText("Not use in last 3 months");
                contacts.addAll(DBHelper.getInstance(DeleteActivity.this).getContactNo3());
                break;
            case "no6":
                txtTitleDelete.setText("Not use in last 6 months");
                contacts.addAll(DBHelper.getInstance(DeleteActivity.this).getContactNo6());
                break;
            case "no12":
                txtTitleDelete.setText("Not use in last 12 months");
                contacts.addAll(DBHelper.getInstance(DeleteActivity.this).getContactNo12());
                break;
            case "never":
                txtTitleDelete.setText("Contact never use");
                contacts.addAll(DBHelper.getInstance(DeleteActivity.this).getContactNeverUse());
                break;
            case "noName":
                txtTitleDelete.setText("Contact have no name");
                contacts.addAll( DBHelper.getInstance(DeleteActivity.this).getContactNoName());
                break;
            case "noPhone":
                txtTitleDelete.setText("Contact have no phone");
                contacts.addAll(DBHelper.getInstance(DeleteActivity.this).getContactNoPhone());
                break;
            case "noEmail":
                txtTitleDelete.setText("Contact have no emails");
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
                        DBHelper.getInstance(DeleteActivity.this).deleteContact(contacts.get(i).getId());
                    }
                }
                contacts.clear();
                getContactFromDB();
                deleteAdapter.notifyDataSetChanged();
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
        imgBack= findViewById(R.id.imgBack);
        txtTitleDelete= findViewById(R.id.txtTitleDelete);
        cbAll= findViewById(R.id.cbAll);
        llDelete= findViewById(R.id.llDelete);
        llButtonDelete= findViewById(R.id.llButtonDelete);
        rcvDelete= findViewById(R.id.rcvList);
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
