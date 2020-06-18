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
import android.util.Log;
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
import com.hungdt.test.model.Duplicate;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.DeleteAdapter;
import com.hungdt.test.view.adapter.DuplicateAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DuplicateActivity extends AppCompatActivity {
    private ImageView imgBack, imgBtnDelete;
    private TextView txtTitleDelete, txtBtnDelete;
    private CheckBox cbAll;
    private LinearLayout llDelete, llButtonDelete;
    private RecyclerView rcvList;
    private List<Contact> contacts = new ArrayList<>();
    private DuplicateAdapter duplicateAdapter;
    private String type;
    private int kind = 1;
    private List<String> idContact = new ArrayList<>();
    private ArrayList<Integer> typeList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initView();

        imgBtnDelete.setImageResource(R.drawable.merge);
        txtBtnDelete.setText("MERGER CONTACT");

        Intent intent = getIntent();
        type = intent.getStringExtra(KEY.DUP);
        idContact.addAll(Objects.requireNonNull(intent.getStringArrayListExtra(KEY.LIST_ID)));

        for (int i = 0; i < idContact.size(); i++) {
            contacts.add(DBHelper.getInstance(this).getDubContact(String.valueOf(idContact.get(i))));
        }
        setTypeContact();

        Collections.sort(contacts);
        duplicateAdapter = new DuplicateAdapter(this, contacts, typeList, type);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(duplicateAdapter);
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
                duplicateAdapter.notifyDataSetChanged();
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

    private void setTypeContact() {
        List<Contact> contactList = new ArrayList<>(contacts);
        switch (type) {
            case "contact":
                txtTitleDelete.setText("Duplicate Contact");
                for (int i = 0; i < contacts.size(); i++) {
                    for (int j = 0; j < contactList.size(); j++) {
                        if (i != j) {
                            if (contacts.get(i).getName().equalsIgnoreCase(contactList.get(j).getName())) {
                                if (contacts.get(j).getType() == 0) {
                                    contacts.get(i).setType(kind);
                                    kind++;
                                    break;
                                }
                                if (contacts.get(j).getType() != 0) {
                                    contacts.get(i).setType(contacts.get(j).getType());
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case "phone":
                txtTitleDelete.setText("Duplicate Phone");
                for (int i = 0; i < contacts.size(); i++) {
                    for (int j = 0; j < contactList.size(); j++) {
                        if (i != j) {
                            if (contacts.get(i).getPhone().equals(contactList.get(j).getPhone())) {
                                if (contacts.get(j).getType() == 0) {
                                    contacts.get(i).setType(kind);
                                    kind++;
                                    break;
                                }
                                if (contacts.get(j).getType() != 0) {
                                    contacts.get(i).setType(contacts.get(j).getType());
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case "email":
                txtTitleDelete.setText("Duplicate Email");
                for (int i = 0; i < contacts.size(); i++) {
                    for (int j = 0; j < contactList.size(); j++) {
                        if (i != j) {
                            if (contacts.get(i).getEmail().equals(contactList.get(j).getEmail())) {
                                if (contacts.get(j).getType() == 0) {
                                    contacts.get(i).setType(kind);
                                    kind++;
                                    break;
                                }
                                if (contacts.get(j).getType() != 0) {
                                    contacts.get(i).setType(contacts.get(j).getType());
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case "name":
                txtTitleDelete.setText("Duplicate Name");
                for (int i = 0; i < contacts.size(); i++) {
                    for (int j = 0; j < contactList.size(); j++) {
                        if (i != j) {
                            if (contacts.get(i).getName().equalsIgnoreCase(contactList.get(j).getName())) {
                                if (contacts.get(j).getType() == 0) {
                                    contacts.get(i).setType(kind);
                                    kind++;
                                    break;
                                }
                                if (contacts.get(j).getType() != 0) {
                                    contacts.get(i).setType(contacts.get(j).getType());
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            default:
                break;

        }
        for (int i = 0; i < contacts.size(); i++) {
            Log.i("123123", "onCreate: " + contacts.get(i).getName() + contacts.get(i).getType());
            if (contacts.get(i).getType() != 0 && !typeList.contains(contacts.get(i).getType())) {
                typeList.add(contacts.get(i).getType());
            }
        }

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
                        DBHelper.getInstance(DuplicateActivity.this).deleteContact(contacts.get(i).getId());
                    }
                }
                contacts.clear();
                //getContactFromDB();
                duplicateAdapter.notifyDataSetChanged();
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
        imgBack = findViewById(R.id.imgBack);
        txtTitleDelete = findViewById(R.id.txtTitleDelete);
        cbAll = findViewById(R.id.cbAll);
        llDelete = findViewById(R.id.llDelete);
        llButtonDelete = findViewById(R.id.llButtonDelete);
        txtBtnDelete = findViewById(R.id.txtBtnDelete);
        imgBtnDelete = findViewById(R.id.imgBtnDelete);
        rcvList = findViewById(R.id.rcvList);
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
