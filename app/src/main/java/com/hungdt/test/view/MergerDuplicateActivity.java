package com.hungdt.test.view;

import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.test.ContactConfig;
import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Contact;
import com.hungdt.test.model.Email;
import com.hungdt.test.model.Phone;
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.DuplicateContactAdapter;
import com.unity3d.ads.UnityAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.hungdt.test.view.MainActivity.contactList;
import static com.hungdt.test.view.MainActivity.showInterstitial;

public class MergerDuplicateActivity extends AppCompatActivity {
    private String type;
    private List<String> idContact = new ArrayList<>();
    private List<Phone> listPhones = new ArrayList<>();
    private List<Email> listEmails = new ArrayList<>();
    private List<Contact> contacts = new ArrayList<>();
    private Contact contactMerger;
    private ImageView imgContact, imgBack;
    private TextView txtContactName, txtMerger;
    private RecyclerView rcvDupContact;
    private ConstraintLayout clContactMerger;
    private DuplicateContactAdapter dupContactAdapter;
    private LinearLayout llButtonMerger;
    int number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Random rd = new Random();
        number = rd.nextInt();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merger_duplicate);

        txtMerger = findViewById(R.id.txtMerger);
        rcvDupContact = findViewById(R.id.rcvDupContact);
        imgContact = findViewById(R.id.imgContact);
        imgBack = findViewById(R.id.imgBack);
        txtContactName = findViewById(R.id.txtContactName);
        clContactMerger = findViewById(R.id.clContactMerger);
        llButtonMerger = findViewById(R.id.llButtonMerger);

        Ads.initNativeGgFb((LinearLayout) findViewById(R.id.lnNative), this, true);
        Intent intent = getIntent();
        type = intent.getStringExtra(KEY.DUP);
        txtMerger.setText("SAVE");
        assert type != null;
        if (type.equals("contact") || type.equals("name") || type.equals("email") || type.equals("phone")) {
            idContact.addAll(Objects.requireNonNull(intent.getStringArrayListExtra(KEY.LIST_ID)));
            Log.e("123123", "onCreate: " + idContact);

            for (int i = 0; i < idContact.size(); i++) {
                contacts.add(DBHelper.getInstance(this).getDuplicateContact(String.valueOf(idContact.get(i))));
                txtContactName.setText(contacts.get(0).getName());
            }
            Collections.sort(contacts);
            for (int i = 0; i < contacts.size(); i++) {
                listPhones.addAll(contacts.get(i).getPhones());
                listEmails.addAll(contacts.get(i).getEmails());
            }

            ArrayList<String> listPhoneNumber = new ArrayList<>();
            for (int i = 0; i < listPhones.size(); i++) {

                if (!listPhoneNumber.contains(listPhones.get(i).getPhone())) {
                    listPhoneNumber.add(listPhones.get(i).getPhone());
                }
            }
            ArrayList<String> listEmailName = new ArrayList<>();
            for (int i = 0; i < listEmails.size(); i++) {

                if (!listEmailName.contains(listEmails.get(i).getEmail())) {
                    listEmailName.add(listEmails.get(i).getEmail());
                }
            }

            switch (type) {
                case "contact":
                    contactMerger = new Contact("0", String.valueOf(number), contacts.get(0).getName(), "image", String.valueOf(number), KEY.FALSE, KEY.TRUE, KEY.FALSE, KEY.TRUE, KEY.FALSE, listPhones, Collections.singletonList(contacts.get(0).getAccounts().get(0)), listEmails);
                    break;
                case "name":
                    contactMerger = new Contact("0", String.valueOf(number), contacts.get(0).getName(), "image", KEY.FALSE, String.valueOf(number), KEY.FALSE, KEY.TRUE, KEY.TRUE, KEY.FALSE, listPhones, Collections.singletonList(contacts.get(0).getAccounts().get(0)), listEmails);
                    break;
                case "phone":
                case "email":
                    contactMerger = new Contact("0", String.valueOf(number), contacts.get(0).getName(), "image", KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.TRUE, KEY.FALSE, listPhones, Collections.singletonList(contacts.get(0).getAccounts().get(0)), listEmails);
                    break;
            }

        } else {
            contactMerger = (Contact) intent.getSerializableExtra(KEY.CONTACT);
            txtContactName.setText(contactMerger.getName());
            if (contactMerger.getmContact().equals(KEY.TRUE)) {
                for (int i = 0; i < contactList.size(); i++) {
                    if (!contactMerger.getIdContact().equals(contactList.get(i).getIdContact())) {
                        if (contactList.get(i).getmContact().equals(KEY.TRUE) && contactList.get(i).gettContact().equals(contactMerger.gettContact())) {
                            contacts.add(contactList.get(i));
                        }
                    }
                }
            }
            if (contactMerger.getmName().equals(KEY.TRUE)) {
                for (int i = 0; i < contactList.size(); i++) {
                    if (!contactMerger.getIdContact().equals(contactList.get(i).getIdContact())) {
                        if (contactList.get(i).getmName().equals(KEY.TRUE) && contactList.get(i).gettName().equals(contactMerger.gettName())) {
                            contacts.add(contactList.get(i));
                        }
                    }

                }
            }
           /* if (contactMerger.getmPhone().equals(KEY.TRUE)) {
                for (int i = 0; i < contactList.size(); i++) {
                    if (!contactMerger.getIdContact().equals(contactList.get(i).getIdContact())) {
                        if (contactList.get(i).getmPhone().equals(KEY.TRUE) && contactList.get(i).gettPhone().equals(contactMerger.gettPhone())) {
                            contacts.add(contactList.get(i));
                        }
                    }
                }
            }

            if (contactMerger.getmEmail().equals(KEY.TRUE)) {
                for (int i = 0; i < contactList.size(); i++) {
                    if (!contactMerger.getIdContact().equals(contactList.get(i).getIdContact())) {
                        if (contactList.get(i).getmEmail().equals(KEY.TRUE) && contactList.get(i).gettEmail().equals(contactMerger.gettEmail())) {
                            contacts.add(contactList.get(i));
                        }
                    }
                }
            }*/
        }

        imgContact.setImageResource(R.drawable.ic_code);

        dupContactAdapter = new DuplicateContactAdapter(this, contacts);
        rcvDupContact.setLayoutManager(new LinearLayoutManager(this));
        rcvDupContact.setAdapter(dupContactAdapter);

        clContactMerger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MergerDuplicateActivity.this, DetailContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY.CONTACT, contactMerger);
                intent1.putExtras(bundle);
                intent1.putExtra(KEY.TYPE, KEY.MERGER);
                startActivity(intent1);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        llButtonMerger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("contact") || type.equals("name") || type.equals("email") || type.equals("phone")) {
                    //check
                    String mP = KEY.FALSE;
                    String mE = KEY.FALSE;
                    //update listDub
                    switch (type) {
                        case "contact":
                            for (int i = 0; i < contacts.size(); i++) {
                                DBHelper.getInstance(MergerDuplicateActivity.this).updateContactMerger(contacts.get(i).getIdContact(), String.valueOf(number), contacts.get(i).gettName(), KEY.FALSE, KEY.FALSE, KEY.TRUE, contacts.get(i).getmName(), KEY.FALSE, KEY.FALSE);
                            }
                            for (int i = 0; i < contactList.size(); i++) {
                                if (idContact.contains(contactList.get(i).getIdContact())) {
                                    contactList.get(i).settContact(String.valueOf(number));
                                    contactList.get(i).setmContact(KEY.TRUE);
                                    /*for (int j = 0; j < contactList.get(i).getPhones().size(); j++) {
                                        contactList.get(i).getPhones().get(j).setMerger(KEY.TRUE);
                                    }
                                    for (int j = 0; j < contactList.get(i).getEmails().size(); j++) {
                                        contactList.get(i).getEmails().get(j).setMerger(KEY.TRUE);
                                    }*/
                                }
                            }

                            break;
                        case "name":
                            for (int i = 0; i < contacts.size(); i++) {
                                DBHelper.getInstance(MergerDuplicateActivity.this).updateContactMerger(contacts.get(i).getIdContact(), contacts.get(i).gettContact(), String.valueOf(number), KEY.FALSE, KEY.FALSE, contacts.get(i).getmContact(), KEY.TRUE, KEY.FALSE, KEY.FALSE);
                            }
                            for (int i = 0; i < contactList.size(); i++) {
                                if (idContact.contains(contactList.get(i).getIdContact())) {
                                    contactList.get(i).settName(String.valueOf(number));
                                    contactList.get(i).setmName(KEY.TRUE);
                                }
                            }
                            break;
                        case "phone":
                            mP = KEY.TRUE;
                            List<String> phones = new ArrayList<>();
                            String phoneNumber = "";
                            for (int i = 0; i < contacts.size(); i++) {
                                for (int j = 0; j < contacts.get(i).getPhones().size(); j++) {
                                    if (!phones.contains(contacts.get(i).getPhones().get(j).getPhone())) {
                                        phones.add(contacts.get(i).getPhones().get(j).getPhone());
                                    } else {
                                        if (phoneNumber.equals("")) {
                                            phoneNumber = contacts.get(i).getPhones().get(j).getPhone();
                                        }
                                    }
                                }
                            }
                            DBHelper.getInstance(MergerDuplicateActivity.this).updatePhone(phoneNumber, String.valueOf(number));
                            for (int i = 0; i < contactList.size(); i++) {
                                for(int j = 0 ; j< contactList.get(i).getPhones().size();j++){
                                    if(contactList.get(i).getPhones().get(j).getPhone().equals(phoneNumber)){
                                        contactList.get(i).getPhones().get(j).setTypeMerger(String.valueOf(number));
                                        contactList.get(i).getPhones().get(j).setmPhone(KEY.TRUE);
                                    }
                                }
                            }
                            break;
                        case "email":
                            mE = KEY.TRUE;
                            List<String> emails = new ArrayList<>();
                            String mail = "";
                            for (int i = 0; i < contacts.size(); i++) {
                                for (int j = 0; j < contacts.get(i).getEmails().size(); j++) {
                                    if (!emails.contains(contacts.get(i).getEmails().get(j).getEmail())) {
                                        emails.add(contacts.get(i).getEmails().get(j).getEmail());
                                    } else {
                                        if (mail.equals("")) {
                                            mail = contacts.get(i).getEmails().get(j).getEmail();
                                        }
                                    }
                                }
                            }
                            DBHelper.getInstance(MergerDuplicateActivity.this).updateEmail(mail, String.valueOf(number));
                            for (int i = 0; i < contactList.size(); i++) {
                                for(int j = 0 ; j< contactList.get(i).getEmails().size();j++){
                                    if(contactList.get(i).getEmails().get(j).getEmail().equals(mail)){
                                        contactList.get(i).getEmails().get(j).setTypeMerger(String.valueOf(number));
                                        contactList.get(i).getEmails().get(j).setmEmail(KEY.TRUE);
                                    }
                                }
                            }
                            break;
                    }

                    //Add new to db
                    DBHelper.getInstance(MergerDuplicateActivity.this).addContact(contactMerger.getIdContact(), contactMerger.getName(), contactMerger.getImage(), contactMerger.gettContact(), contactMerger.gettName(), contactMerger.getmContact(), contactMerger.getmName(),  KEY.TRUE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE);
                    String idTable = DBHelper.getInstance(MergerDuplicateActivity.this).getLastID();
                    String idContact = DBHelper.getInstance(MergerDuplicateActivity.this).getLastContactID(idTable);

                    if (listPhones.size() > 0) {

                        for (int i = 0; i < listPhones.size(); i++) {
                            if(type.equals("phone")){
                                DBHelper.getInstance(MergerDuplicateActivity.this).addPhone(idContact,listPhones.get(i).getPhone(),String.valueOf(number),KEY.TRUE );
                            }else {
                                DBHelper.getInstance(MergerDuplicateActivity.this).addPhone(idContact,listPhones.get(i).getPhone(),KEY.FALSE,KEY.FALSE);
                            }

                        }
                    }
                    DBHelper.getInstance(MergerDuplicateActivity.this).addAccount(idContact, contacts.get(0).getAccounts().get(0).getAccountName(), contacts.get(0).getAccounts().get(0).getAccountType());
                    if (listEmails.size() > 0) {
                        for (int i = 0; i < listEmails.size(); i++) {
                            if(type.equals("email")){
                                DBHelper.getInstance(MergerDuplicateActivity.this).addEmail(idContact,listEmails.get(i).getEmail(),String.valueOf(number),KEY.TRUE );
                            }else {
                                DBHelper.getInstance(MergerDuplicateActivity.this).addEmail(idContact,listEmails.get(i).getEmail(),KEY.FALSE,KEY.FALSE);
                            }
                        }
                    }

                    //add to list
                    contactList.add(new Contact(idTable, idContact, contactMerger.getName(), contactMerger.getImage(), contactMerger.gettContact(), contactMerger.gettName(), contactMerger.getmContact(), contactMerger.getmName(), KEY.TRUE, KEY.FALSE, listPhones, contactMerger.getAccounts(), listEmails));

                    sendBroadcast(new Intent(ContactFragment.ACTION_UPDATE_LIST_CONTACT));
                    sendBroadcast(new Intent(DuplicateActivity.ACTION_FINISH_ACTIVITY));
                    sendBroadcast(new Intent(ManageFragment.ACTION_RELOAD_FRAGMENT_MANAGE));
                    sendBroadcast(new Intent(MergedFragment.ACTION_RELOAD_FRAGMENT_MERGED));
                    Toast.makeText(MergerDuplicateActivity.this, "Save to Merger!", Toast.LENGTH_SHORT).show();
                    if (ContactConfig.getInstance().getConfig().getBoolean("config_on")) {
                        if (MainActivity.ggInterstitialAd != null && MainActivity.ggInterstitialAd.isLoaded())
                            MainActivity.ggInterstitialAd.show();
                        else if (UnityAds.isInitialized() && UnityAds.isReady(getString(R.string.INTER_UNI)))
                            UnityAds.show(MergerDuplicateActivity.this, getString(R.string.INTER_UNI));
                    }
                    finish();
                } else {
                    openMergerDialog();
                }

            }
        });
    }

    private void openMergerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_qs_yes_no);

        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        TextView txtTitleToolBar = dialog.findViewById(R.id.txtTitleToolBar);
        TextView txtBody = dialog.findViewById(R.id.txtBody);

        txtTitleToolBar.setText("Confirmation");
        txtBody.setText("Selected contacts will be saved to phone book and corresponding duplicates contacts will be removed.");

        btnYes.setText("OK");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //xóa danh bạ cũ và add danh bạ mới.
                for (int i = 0; i < contacts.size(); i++) {
                    if (!contacts.get(i).getFather().equals(KEY.TRUE)) {
                        //Delete phone book
                        deleteContact(i);
                        //Delete from list
                        contactList.remove(contacts.get(i));
                        //Delete db
                        DBHelper.getInstance(MergerDuplicateActivity.this).updateDisableContact(contacts.get(i).getIdContact(), KEY.FALSE);
                    }
                }
                DBHelper.getInstance(MergerDuplicateActivity.this).updateDisableContact(contactMerger.getIdContact(), KEY.TRUE);
                /*for (int i = 0; i < contactList.size(); i++) {
                    if (contactList.get(i).getIdContact().equals(contactMerger.getIdContact())) {
                        contactList.get(i).setFather(KEY.FALSE);
                        contactList.get(i).settContact(KEY.FALSE);
                        contactList.get(i).settName(KEY.FALSE);
                        contactList.get(i).settPhone(KEY.FALSE);
                        contactList.get(i).settEmail(KEY.FALSE);
                        contactList.get(i).setmContact(KEY.FALSE);
                        contactList.get(i).setmName(KEY.FALSE);
                        contactList.get(i).setmPhone(KEY.FALSE);
                        contactList.get(i).settEmail(KEY.FALSE);
                    }
                }*/
                addNewContactPhoneBook();
                sendBroadcast(new Intent(ContactFragment.ACTION_UPDATE_LIST_CONTACT));
                sendBroadcast(new Intent(ManageFragment.ACTION_RELOAD_FRAGMENT_MANAGE));
                sendBroadcast(new Intent(MergedFragment.ACTION_RELOAD_FRAGMENT_MERGED));
                Toast.makeText(MergerDuplicateActivity.this, "Merger Success!!!\nYou can check on your phone book.", Toast.LENGTH_SHORT).show();
                if (ContactConfig.getInstance().getConfig().getBoolean("config_on")) {
                    if (MainActivity.ggInterstitialAd != null && MainActivity.ggInterstitialAd.isLoaded()) {
                        MainActivity.ggInterstitialAd.show();
                    } else if (UnityAds.isInitialized() && UnityAds.isReady(getString(R.string.INTER_UNI))) {
                        UnityAds.show(MergerDuplicateActivity.this, getString(R.string.INTER_UNI));
                    }
                }
                dialog.dismiss();
                finish();
            }
        });
        btnNo.setText("CANCEL");
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void deleteContact(int i) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        String[] args = new String[]{contacts.get(i).getIdContact()};
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args).build());
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private void addNewContactPhoneBook() {
        ArrayList<ContentProviderOperation> cntProOper = new ArrayList<>();
        int contactIndex = cntProOper.size();

        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)//Step1
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        cntProOper.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)//Step2
                .withValueBackReference(android.provider.ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(android.provider.ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactMerger.getName()) // Name of the contact
                .build());

        List<String> phones = new ArrayList<>();
        for (int i = 0; i < contactMerger.getPhones().size(); i++) {
            phones.add(contactMerger.getPhones().get(i).getPhone());
        }
        for (String s : phones) {
            cntProOper.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)//Step 3
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                    .withValue(android.provider.ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, s)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc
        }
        List<String> emails = new ArrayList<>();
        for (int i = 0; i < contactMerger.getEmails().size(); i++) {
            emails.add(contactMerger.getEmails().get(i).getEmail());
        }
        for (String s : emails) {
            cntProOper.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                    .withValue(android.provider.ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME, s)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM).build()); //Type like HOME, MOBILE etc
        }

        ContentProviderResult[] s = new ContentProviderResult[0];
        try {
            s = getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper);
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
