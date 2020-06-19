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
        List<Duplicate> emailList = new ArrayList<>(emails);
        for (int i = 0; i < emails.size(); i++) {
            if(!emails.get(i).getMerger().equals(KEY.FALSE)){
                break;
            }
            for (int j = 0; j < emailList.size(); j++) {
                if (i != j) {
                    if (emails.get(i).getName().equalsIgnoreCase(emailList.get(j).getName()) && Integer.parseInt(emails.get(i).getContactID()) != Integer.parseInt(emailList.get(j).getContactID())) {
                        if (emails.get(j).getType() == 0) {
                            emails.get(i).setType(type);
                            type++;
                            break;
                        }
                        if (emails.get(j).getType() != 0) {
                            emails.get(i).setType(emails.get(j).getType());
                            break;
                        }
                    }
                }
            }
        }
        ArrayList<Integer> idCheckEmail = new ArrayList<>();
        for (int i = 0; i < emails.size(); i++) {
            if (emails.get(i).getType() != 0 && !idCheckEmail.contains(emails.get(i).getType())) {
                idCheckEmail.add(emails.get(i).getType());
                dubEmail++;
            }
        }

        List<Duplicate> phoneList = new ArrayList<>(phones);
        for (int i = 0; i < phones.size(); i++) {
            if(!phones.get(i).getMerger().equals(KEY.FALSE)){
                break;
            }
            for (int j = 0; j < phoneList.size(); j++) {
                if (i != j) {
                    if (phones.get(i).getName().equalsIgnoreCase(phoneList.get(j).getName()) && Integer.parseInt(phones.get(i).getContactID()) != Integer.parseInt(phoneList.get(j).getContactID())) {
                        if (phones.get(j).getType() == 0) {
                            phones.get(i).setType(type);
                            type++;
                            break;
                        }
                        if (phones.get(j).getType() != 0) {
                            phones.get(i).setType(phones.get(j).getType());
                            break;
                        }
                    }
                }
            }
        }
        ArrayList<Integer> idCheckPhone = new ArrayList<>();
        for (int i = 0; i < phones.size(); i++) {
            if (phones.get(i).getType() != 0 && !idCheckPhone.contains(phones.get(i).getType())) {
                idCheckPhone.add(phones.get(i).getType());
                dubPhone++;
            }
        }


        List<Duplicate> nameList = new ArrayList<>(names);
        for (int i = 0; i < names.size(); i++) {
            if(!names.get(i).getMerger().equals(KEY.FALSE)){
                break;
            }
            for (int j = 0; j < nameList.size(); j++) {
                if (i != j) {
                    if (names.get(i).getName().equalsIgnoreCase(nameList.get(j).getName()) && Integer.parseInt(names.get(i).getContactID()) != Integer.parseInt(nameList.get(j).getContactID())) {
                        if (names.get(j).getType() == 0) {
                            names.get(i).setType(type);
                            type++;
                            break;
                        }
                        if (names.get(j).getType() != 0) {
                            names.get(i).setType(names.get(j).getType());
                            break;
                        }
                    }
                }
            }
        }
        ArrayList<Integer> idCheckName = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).getType() != 0 && !idCheckName.contains(names.get(i).getType())) {
                idCheckName.add(names.get(i).getType());
                similarName++;
            }
        }

        final ArrayList<Integer> idCheckContact = new ArrayList<>();
        for (int i = 0; i < idCheckName.size(); i++) {
            if (idCheckPhone.contains(idCheckName.get(i))) {
                idCheckContact.add(names.get(i).getType());
                dubContact++;
            }
        }

        txtDupContact.setText(String.valueOf(dubContact));
        txtDupPhone.setText(String.valueOf(dubPhone));
        txtDupEmail.setText(String.valueOf(dubEmail));
        txtSimilarName.setText(String.valueOf(similarName));

        clDupContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DuplicateActivity.class);
                intent.putExtra(KEY.DUP, "contact");
                intent.putIntegerArrayListExtra(KEY.LIST_ID, idCheckContact);
                startActivity(intent);
            }
        });
        clDupPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DuplicateActivity.class);
                intent.putExtra(KEY.DUP, "phone");
                ArrayList<String> id = new ArrayList<>();
                for (int i = 0; i < phones.size(); i++) {
                    if (phones.get(i).getType() != 0) {
                        id.add(phones.get(i).getContactID());
                        Log.i("123123", "onClick: " +phones.get(i).getContactID());
                    }
                }
                intent.putStringArrayListExtra(KEY.LIST_ID, id);
                startActivity(intent);
            }
        });
        clDupEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DuplicateActivity.class);
                intent.putExtra(KEY.DUP, "email");
                ArrayList<String> id = new ArrayList<>();
                for (int i = 0; i < emails.size(); i++) {
                    if (emails.get(i).getType() != 0) {
                        id.add(emails.get(i).getContactID());
                    }
                }
                intent.putStringArrayListExtra(KEY.LIST_ID, id);
                startActivity(intent);
            }
        });
        clSimilarName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DuplicateActivity.class);
                intent.putExtra(KEY.DUP, "name");
                ArrayList<String> id = new ArrayList<>();
                for (int i = 0; i < names.size(); i++) {
                    if (names.get(i).getType() != 0) {
                        id.add(names.get(i).getContactID());
                    }
                }
                intent.putStringArrayListExtra(KEY.LIST_ID, id);
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
