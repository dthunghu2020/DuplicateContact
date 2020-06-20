package com.hungdt.test.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Duplicate;
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.KEY;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManageFragment extends Fragment {
    private List<Duplicate> names = new ArrayList<>();
    private List<Duplicate> phones = new ArrayList<>();
    private List<Duplicate> emails = new ArrayList<>();
    private List<Duplicate> contacts = new ArrayList<>();
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
        Ads.initBanner(((LinearLayout) view.findViewById(R.id.llBanner)), getActivity(), true);

        IntentFilter intentFilter = new IntentFilter(ACTION_RELOAD_FRAGMENT_MANAGE);
       getActivity().registerReceiver(reloadFragmentManage,intentFilter);

        phones.addAll(DBHelper.getInstance(getContext()).getDupPhone());
        emails.addAll(DBHelper.getInstance(getContext()).getDupEmail());
        names.addAll(DBHelper.getInstance(getContext()).getDupName());

        loadDubName();
        loadDubPhone();
        loadDubEmail();
        loadDubContact();

        setTextFragment();

        clDupContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DuplicateActivity.class);
                intent.putExtra(KEY.DUP, "contact");
                ArrayList<String> id = new ArrayList<>();
                for (int i = 0; i < contacts.size(); i++) {
                    if (contacts.get(i).getType() != 0) {
                        id.add(contacts.get(i).getContactID());
                    }
                }

                intent.putStringArrayListExtra(KEY.LIST_ID, id);
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
                    }
                }
                Log.e("111111", "onClick: " + id);

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

    private void setTextFragment() {
        txtDupContact.setText(String.valueOf(dubContact));
        txtDupPhone.setText(String.valueOf(dubPhone));
        txtDupEmail.setText(String.valueOf(dubEmail));
        txtSimilarName.setText(String.valueOf(similarName));
    }

    private void loadDubContact() {
        List<Duplicate> contactList = new ArrayList<>(contacts);
        for (int i = 0; i < contacts.size(); i++) {
            if (!contacts.get(i).getMerger().equals(KEY.FALSE)) {
                break;
            }
            for (int j = 0; j < contactList.size(); j++) {
                if (i != j) {
                    if (contacts.get(i).getName().equalsIgnoreCase(contactList.get(j).getName()) &&
                            contacts.get(i).getPhone().equals(contacts.get(j).getPhone()) &&
                            contacts.get(i).getEmail().equals(contacts.get(j).getEmail()) &&
                            Integer.parseInt(names.get(i).getContactID()) != Integer.parseInt(contactList.get(j).getContactID())) {
                        if (contacts.get(j).getType() == 0) {
                            contacts.get(i).setType(type);
                            type++;
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
        final ArrayList<Integer> idCheckContact = new ArrayList<>();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getType() != 0 && !idCheckContact.contains(contacts.get(i).getType())) {
                idCheckContact.add(contacts.get(i).getType());
                dubContact++;
            }
        }
    }

    private void loadDubName() {
        List<Duplicate> nameList = new ArrayList<>(names);
        for (int i = 0; i < names.size(); i++) {
            if (!names.get(i).getMerger().equals(KEY.FALSE)) {
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
    }

    private void loadDubPhone() {
        List<Duplicate> phoneList = new ArrayList<>(phones);
        for (int i = 0; i < phones.size(); i++) {
            if (!phones.get(i).getMerger().equals(KEY.FALSE)) {
                break;
            }
            for (int j = 0; j < phoneList.size(); j++) {
                if (i != j&&phones.get(j).getmName().equals(KEY.FALSE)) {
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
    }

    private void loadDubEmail() {
        contacts.addAll(DBHelper.getInstance(getContext()).getDupContact());
        List<Duplicate> emailList = new ArrayList<>(emails);
        for (int i = 0; i < emails.size(); i++) {
            if (!emails.get(i).getMerger().equals(KEY.FALSE)) {
                break;
            }
            for (int j = 0; j < emailList.size(); j++) {
                if (i != j&&emails.get(j).getmEmail().equals(KEY.FALSE)) {
                    if (emails.get(i).getName().equalsIgnoreCase(emailList.get(j).getName()) && Integer.parseInt(emails.get(i).getContactID()) != Integer.parseInt(emailList.get(j).getContactID())) {
                        if (emails.get(j).getType() == 0) {
                            emails.get(i).setType(type);
                            Log.e("123321", "onViewCreated: "+ emails.get(i).getName()+emailList.get(i).getName() );
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
    }

    public static final String ACTION_RELOAD_FRAGMENT_MANAGE = "updateFragmentManage";
    private BroadcastReceiver reloadFragmentManage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadDubName();
            loadDubEmail();
            loadDubPhone();
            loadDubContact();
            setTextFragment();
        }
    };

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(reloadFragmentManage!=null){
            getActivity().unregisterReceiver(reloadFragmentManage);
        }
    }
}
