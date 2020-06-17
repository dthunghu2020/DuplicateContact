package com.hungdt.test.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Duplicate;
import com.hungdt.test.utils.KEY;

import java.util.ArrayList;
import java.util.List;

public class ManageFragment extends Fragment {
    private List<Duplicate> names = new ArrayList<>();
    private List<Duplicate> phones = new ArrayList<>();
    private List<Duplicate> emails = new ArrayList<>();
    private ConstraintLayout clDupContact, clDupPhone, clDupEmail, clSimilarName;
    private TextView txtDupContact, txtDupPhone, txtDupEmail, txtSimilarName;
    private int dubContact = 0;
    private int dubPhone = 0;
    private int dubEmail = 0;
    private int similarName = 0;
    private int type = 1;

    public ManageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

        phones.addAll(DBHelper.getInstance(getContext()).getDupPhone());
        emails.addAll(DBHelper.getInstance(getContext()).getDupEmail());
        names.addAll(DBHelper.getInstance(getContext()).getDupName());
        for (int i = 0; i < phones.size() - 1; i++) {
            for (int j = i + 1; j < phones.size(); j++) {
                if (i != j) {
                    if (phones.get(i).getName().equals(phones.get(j).getName()) && Integer.parseInt(phones.get(i).getContactID()) != Integer.parseInt(phones.get(j).getContactID())) {
                        if (phones.get(i).getType() == 0 && phones.get(j).getType() == 0) {
                            dubPhone++;
                            phones.get(i).setType(type);
                            phones.get(j).setType(type);
                            type++;
                        }
                        if (phones.get(i).getType() != 0 && phones.get(j).getType() == 0) {
                            dubPhone++;
                            phones.get(j).setType(phones.get(i).getType());
                        }
                        if (phones.get(i).getType() == 0 && phones.get(j).getType() != 0) {
                            dubPhone++;
                            phones.get(i).setType(phones.get(j).getType());
                        }
                    }
                }
            }
        }

        for (int i = 0; i < emails.size() - 1; i++) {
            for (int j = i + 1; j < emails.size(); j++) {
                if (i != j) {
                    if (emails.get(i).getName().equals(emails.get(j).getName()) && Integer.parseInt(emails.get(i).getContactID()) != Integer.parseInt(emails.get(j).getContactID())) {
                        if (emails.get(i).getType() == 0 && emails.get(j).getType() == 0) {
                            dubEmail++;
                            emails.get(i).setType(type);
                            emails.get(j).setType(type);
                            type++;
                        }
                        if (emails.get(i).getType() != 0 && emails.get(j).getType() == 0) {
                            dubEmail++;
                            emails.get(j).setType(emails.get(i).getType());
                        }
                        if (phones.get(i).getType() == 0 && emails.get(j).getType() != 0) {
                            dubEmail++;
                            emails.get(i).setType(emails.get(j).getType());
                        }
                    }
                }
            }
        }

        for (int i = 0; i < names.size() - 1; i++) {
            for (int j = i + 1; j < names.size(); j++) {
                if (i != j) {
                    if (names.get(i).getName().equals(names.get(j).getName()) && Integer.parseInt(names.get(i).getContactID()) != Integer.parseInt(names.get(j).getContactID())) {
                        if (names.get(i).getType() == 0 && names.get(j).getType() == 0) {
                            similarName++;
                            names.get(i).setType(type);
                            names.get(j).setType(type);
                            type++;
                        }
                        if (names.get(i).getType() != 0 && names.get(j).getType() == 0) {
                            similarName++;
                            names.get(j).setType(names.get(i).getType());
                        }
                        if (phones.get(i).getType() == 0 && names.get(j).getType() != 0) {
                            similarName++;
                            names.get(i).setType(names.get(j).getType());
                        }
                    }
                }
            }
        }


        /*for (int i = 0; i < emails.size() - 1; i++) {
            for (int j = i + 1; j < emails.size(); j++) {
                if (i != j) {
                    if (emails.get(i).getName().equals(emails.get(j).getName()) && !emails.get(i).getContactID().equals(emails.get(j).getContactID())) {
                        dubEmail++;
                    }
                }
            }
        }
        for (int i = 0; i < names.size() - 1; i++) {
            for (int j = i + 1; j < names.size(); j++) {
                if (i != j) {
                    if (names.get(i).getName().equalsIgnoreCase(names.get(j).getName()) && Integer.parseInt(names.get(i).getContactID()) != Integer.parseInt(names.get(j).getContactID())) {
                        Log.e("123123", "onViewCreated: " + names.get(i).getName() + "/" + names.get(j).getName());
                        break;
                    }
                }
            }
            Log.e("123123", "onViewCreated: " + idNames);
        }*/
        //Log.e("123123", "onViewCreated: " + idNames);

        txtDupContact.setText(String.valueOf(dubContact));
        txtDupPhone.setText(String.valueOf(dubPhone));
        txtDupEmail.setText(String.valueOf(dubEmail));
        txtSimilarName.setText(String.valueOf(similarName));

        clDupContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DuplicateActivity.class);
                intent.putExtra(KEY.DUP, "contact");
                startActivity(intent);
            }
        });
        clDupPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DuplicateActivity.class);
                intent.putExtra(KEY.DUP, "phone");
                startActivity(intent);
            }
        });
        clDupEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DuplicateActivity.class);
                intent.putExtra(KEY.DUP, "email");
                startActivity(intent);
            }
        });
        clSimilarName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DuplicateActivity.class);
                intent.putExtra(KEY.DUP, "name");
                startActivity(intent);
            }
        });
    }


    private void initView(View view) {
        clDupContact = view.findViewById(R.id.clDupContact);
        clDupPhone = view.findViewById(R.id.clDupPhone);
        clDupEmail = view.findViewById(R.id.clDupEmail);
        clSimilarName = view.findViewById(R.id.clSimilarName);
        txtDupContact = view.findViewById(R.id.txtDupContact);
        txtDupPhone = view.findViewById(R.id.txtDupPhone);
        txtDupEmail = view.findViewById(R.id.txtDupEmail);
        txtSimilarName = view.findViewById(R.id.txtSimilarName);
    }
}
