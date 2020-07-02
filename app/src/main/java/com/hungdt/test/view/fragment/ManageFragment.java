package com.hungdt.test.view.fragment;

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

import com.hungdt.test.ContactConfig;
import com.hungdt.test.R;
import com.hungdt.test.model.Duplicate;
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.DuplicateActivity;
import com.hungdt.test.view.RestoreActivity;
import com.unity3d.ads.UnityAds;

import java.util.ArrayList;
import java.util.List;

import static com.hungdt.test.view.MainActivity.contactList;
import static com.hungdt.test.view.MainActivity.showInterstitial;

public class ManageFragment extends Fragment {
    public static final String ACTION_RELOAD_FRAGMENT_MANAGE = "updateFragmentManage";
    private List<Duplicate> names = new ArrayList<>();
    private List<Duplicate> phones = new ArrayList<>();
    private List<Duplicate> emails = new ArrayList<>();
    private List<Duplicate> contacts = new ArrayList<>();
    private ArrayList<String> idContacts = new ArrayList<>();
    private ArrayList<String> idNames = new ArrayList<>();
    private ArrayList<String> idPhones = new ArrayList<>();
    private ArrayList<String> idEmails = new ArrayList<>();
    private ConstraintLayout clDupContact, clDupPhone, clDupEmail, clSimilarName, clBackup;
    private TextView txtDupContact, txtDupPhone, txtDupEmail, txtSimilarName;
    private int dubContact = 0;
    private int dubPhone = 0;
    private int dubEmail = 0;
    private int dubName = 0;

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
        getActivity().registerReceiver(reloadFragmentManage, intentFilter);

        clDupContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DuplicateActivity.class);
                intent.putExtra(KEY.DUP, "contact");
                intent.putStringArrayListExtra(KEY.LIST_ID, idContacts);
                startActivity(intent);
                showInter();
            }
        });
        clSimilarName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DuplicateActivity.class);
                intent.putExtra(KEY.DUP, "name");
                intent.putStringArrayListExtra(KEY.LIST_ID, idNames);
                startActivity(intent);
                showInter();
            }
        });
        clDupPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DuplicateActivity.class);
                intent.putExtra(KEY.DUP, "phone");
                intent.putStringArrayListExtra(KEY.LIST_ID, idPhones);
                Log.e("123", "onClick: " + idPhones);
                startActivity(intent);
                showInter();
            }
        });
        clDupEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DuplicateActivity.class);
                intent.putExtra(KEY.DUP, "email");
                intent.putStringArrayListExtra(KEY.LIST_ID, idEmails);
                startActivity(intent);
                showInter();
            }
        });

        clBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RestoreActivity.class);
                startActivity(intent);
                showInter();
            }
        });
    }

    private BroadcastReceiver reloadFragmentManage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dubContact = 0;
            dubPhone = 0;
            dubEmail = 0;
            dubName = 0;
            contacts.clear();
            phones.clear();
            names.clear();
            emails.clear();
            idContacts.clear();
            idNames.clear();
            idPhones.clear();
            idEmails.clear();
            for (int i = 0; i < contactList.size(); i++) {
                contacts.add(new Duplicate(contactList.get(i).getIdTable(),
                        contactList.get(i).getIdContact(),
                        contactList.get(i).getName(),
                        KEY.FALSE, "",
                        contactList.get(i).getPhones(), contactList.get(i).getEmails()));
                //add phone
                for (int j = 0; j < contactList.get(i).getPhones().size(); j++) {
                    phones.add(new Duplicate(contactList.get(i).getPhones().get(j).getIdTable(),
                            contactList.get(i).getPhones().get(j).getIdContact(),
                            contactList.get(i).getPhones().get(j).getPhone(),
                            KEY.FALSE, "", null, null));
                }

                //addEmail
                for (int j = 0; j < contactList.get(i).getEmails().size(); j++) {
                    emails.add(new Duplicate(contactList.get(i).getEmails().get(j).getIdTable(),
                            contactList.get(i).getEmails().get(j).getIdContact(),
                            contactList.get(i).getEmails().get(j).getEmail(),
                            KEY.FALSE, "", null, null));
                }

                //addName
                names.add(new Duplicate(contactList.get(i).getIdTable(),
                        contactList.get(i).getIdContact(),
                        contactList.get(i).getName(),
                        KEY.FALSE, "", null, null));
            }
            loadDubContact();
            loadDubName();
            loadDubPhone();
            loadDubEmail();
            setTextFragment();
        }
    };
    private void setTextFragment() {
        txtDupContact.setText(String.valueOf(dubContact));
        txtDupPhone.setText(String.valueOf(dubPhone));
        txtDupEmail.setText(String.valueOf(dubEmail));
        txtSimilarName.setText(String.valueOf(dubName));
    }

    private void loadDubEmail() {
        List<Duplicate> emailList = new ArrayList<>(emails);
        for (int i = 0; i < emails.size(); i++) {
            if (!emails.get(i).getMerged().equals(KEY.FALSE)) {
                break;
            }
            for (int j = 0; j < emailList.size(); j++) {
                if (i != j && !emails.get(i).getContactID().equals(emailList.get(j).getContactID())) {
                    if (emails.get(i).getName().equals(emailList.get(j).getName())) {
                        if (!emails.get(j).getTypeMer().equals("")) {
                            emails.get(i).setTypeMer(emails.get(j).getTypeMer());
                            break;
                        }
                        if (emails.get(j).getTypeMer().equals("")) {
                            emails.get(i).setTypeMer(emails.get(i).getName());
                            break;
                        }
                    }
                }
            }
        }
        ArrayList<String> idCheckEmail = new ArrayList<>();
        ArrayList<String> idCheckIdE = new ArrayList<>();
        for (int i = 0; i < emails.size(); i++) {
            if (!emails.get(i).getTypeMer().equals("")) {
                if (!idCheckIdE.contains(emails.get(i).getContactID())) {
                    idCheckIdE.add(emails.get(i).getContactID());
                    idEmails.add(emails.get(i).getContactID());
                }
                if (!idCheckEmail.contains(emails.get(i).getTypeMer())) {
                    idCheckEmail.add(emails.get(i).getTypeMer());
                    dubEmail++;
                }
            }
        }
    }

    private void loadDubPhone() {
        List<Duplicate> phoneList = new ArrayList<>(phones);
        for (int i = 0; i < phones.size(); i++) {
            if (!phones.get(i).getMerged().equals(KEY.FALSE)) {
                break;
            }
            for (int j = 0; j < phoneList.size(); j++) {
                if (i != j && !phones.get(i).getContactID().equals(phoneList.get(j).getContactID())) {
                    if (phones.get(i).getName().equals(phoneList.get(j).getName())) {
                        if (!phones.get(j).getTypeMer().equals("")) {
                            phones.get(i).setTypeMer(phones.get(j).getTypeMer());
                            break;
                        }
                        if (phones.get(j).getTypeMer().equals("")) {
                            phones.get(i).setTypeMer(phones.get(i).getName());
                            break;
                        }
                    }
                }
            }
        }
        ArrayList<String> idCheckPhone = new ArrayList<>();
        ArrayList<String> idCheckIdP = new ArrayList<>();
        for (int i = 0; i < phones.size(); i++) {
            if (!phones.get(i).getTypeMer().equals("")) {
                if (!idCheckIdP.contains(phones.get(i).getContactID())) {
                    idCheckIdP.add(phones.get(i).getContactID());
                    idPhones.add(phones.get(i).getContactID());
                }
                if (!idCheckPhone.contains(phones.get(i).getTypeMer())) {
                    idCheckPhone.add(phones.get(i).getTypeMer());
                    dubPhone++;
                }
            }
        }
    }

    private void loadDubName() {
        List<Duplicate> nameList = new ArrayList<>(names);
        for (int i = 0; i < names.size(); i++) {
            if (!names.get(i).getMerged().equals(KEY.FALSE)) {
                break;
            }
            for (int j = 0; j < nameList.size(); j++) {
                if (i != j && !names.get(i).getContactID().equals(nameList.get(j).getContactID())) {
                    if (names.get(i).getName().equalsIgnoreCase(nameList.get(j).getName())) {
                        if (!names.get(j).getTypeMer().equals("")) {
                            names.get(i).setTypeMer(names.get(j).getTypeMer());
                            break;
                        }
                        if (names.get(j).getTypeMer().equals("")) {
                            names.get(i).setTypeMer(names.get(i).getName());
                            break;
                        }
                    }
                }
            }
        }
        ArrayList<String> idCheckName = new ArrayList<>();
        ArrayList<String> idCheckIdN = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            if (!names.get(i).getTypeMer().equals("")) {
                if (!idCheckIdN.contains(names.get(i).getContactID())) {
                    idCheckIdN.add(names.get(i).getContactID());
                    idNames.add(names.get(i).getContactID());
                }
                if (!idCheckName.contains(names.get(i).getTypeMer())) {
                    idCheckName.add(names.get(i).getTypeMer());
                    dubName++;
                }
            }
        }
    }

    private void loadDubContact() {
        List<Duplicate> contactList = new ArrayList<>(contacts);
        for (int i = 0; i < contacts.size(); i++) {
            if (!contacts.get(i).getMerged().equals(KEY.FALSE)) {
                break;
            }
            for (int j = 0; j < contactList.size(); j++) {
                if (i != j && !contacts.get(i).getContactID().equals(contactList.get(j).getContactID())) {
                    if (contacts.get(i).getName().equalsIgnoreCase(contactList.get(j).getName()) &&
                            contacts.get(i).getPhone().equals(contactList.get(j).getPhone()) &&
                            contacts.get(i).getEmail().equals(contactList.get(j).getEmail())) {
                        if (!contacts.get(j).getTypeMer().equals("")) {
                            contacts.get(i).setTypeMer(contacts.get(j).getTypeMer());
                            break;
                        }
                        if (contacts.get(j).getTypeMer().equals("")) {
                            contacts.get(i).setTypeMer(contacts.get(i).getContactID());
                            break;
                        }
                    }
                }
            }
        }
        ArrayList<String> idCheckContact = new ArrayList<>();
        ArrayList<String> idCheckIdC = new ArrayList<>();
        for (int i = 0; i < contacts.size(); i++) {
            if (!contacts.get(i).getTypeMer().equals("")) {
                if (!idCheckIdC.contains(contacts.get(i).getContactID())) {
                    idCheckIdC.add(contacts.get(i).getContactID());
                    idContacts.add(contacts.get(i).getContactID());
                }
                if (!idCheckContact.contains(contacts.get(i).getTypeMer())) {
                    idCheckContact.add(contacts.get(i).getTypeMer());
                    dubContact++;
                }
            }
        }
    }
    private void initView(View view) {
        clDupContact = view.findViewById(R.id.clDupContact);
        clDupPhone = view.findViewById(R.id.clDupPhone);
        clDupEmail = view.findViewById(R.id.clDupEmail);
        clBackup = view.findViewById(R.id.clBackup);
        clSimilarName = view.findViewById(R.id.clSimilarName);
        txtDupContact = view.findViewById(R.id.txtDupContact);
        txtDupPhone = view.findViewById(R.id.txtDupPhone);
        txtDupEmail = view.findViewById(R.id.txtDupEmail);
        txtSimilarName = view.findViewById(R.id.txtSimilarName);
    }
    private void showInter() {
        if (ContactConfig.getInstance().getConfig().getBoolean("config_on")) {
            if (!showInterstitial()) {
                if (UnityAds.isInitialized() && UnityAds.isReady(getString(R.string.INTER_UNI)))
                    UnityAds.show(getActivity(), getString(R.string.INTER_UNI));
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (reloadFragmentManage != null) {
            getActivity().unregisterReceiver(reloadFragmentManage);
        }
    }
}
