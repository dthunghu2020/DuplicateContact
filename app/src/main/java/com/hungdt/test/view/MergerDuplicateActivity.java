package com.hungdt.test.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.net.ssl.ManagerFactoryParameters;

import static com.hungdt.test.view.MainActivity.contactList;

public class MergerDuplicateActivity extends AppCompatActivity {
    public static final String ACTION_UPDATE_NAME_CONTACT_MERGER = "renameContactMerger";
    private String type;
    private List<String> idContact = new ArrayList<>();
    private List<Phone> listPhones = new ArrayList<>();
    private List<Phone> listPhoneMer = new ArrayList<>();
    private List<Email> listEmails = new ArrayList<>();
    private List<Email> listEmailMer = new ArrayList<>();
    private List<Contact> contacts = new ArrayList<>();
    private Contact contactMerger;
    private ImageView imgContact, imgBack;
    private TextView txtContactName, txtMerger, txtTitle;
    private RecyclerView rcvDupContact;
    private ConstraintLayout clContactMerger;
    private DuplicateContactAdapter dupContactAdapter;
    private LinearLayout llButtonMerger;
    private String valueDuplicate = "";
    int number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merger_duplicate);

        txtTitle = findViewById(R.id.txtTitle);
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
        txtMerger.setText("Merger Contacts");

        Random rd = new Random();
        number = rd.nextInt();

        IntentFilter intentFilter = new IntentFilter(ACTION_UPDATE_NAME_CONTACT_MERGER);
        registerReceiver(renameContactMerger, intentFilter);


        //txtTitle.setVisibility(View.GONE);
        idContact.addAll(intent.getStringArrayListExtra(KEY.LIST_ID));
        //Lấy list contact giống
        for (int i = 0; i < contactList.size(); i++) {
            if (idContact.contains(contactList.get(i).getIdContact())) {
                contacts.add(contactList.get(i));
            }
        }
        Collections.sort(contacts);
        txtContactName.setText(contacts.get(0).getName());
        for (int i = 0; i < contacts.size(); i++) {
            listPhones.addAll(contacts.get(i).getPhones());
            listEmails.addAll(contacts.get(i).getEmails());
        }
        //Lấy list phone
        ArrayList<String> listPhoneNumber = new ArrayList<>();
        for (int i = 0; i < listPhones.size(); i++) {
            if (!listPhoneNumber.contains(listPhones.get(i).getPhone())) {
                listPhoneNumber.add(listPhones.get(i).getPhone());
                listPhoneMer.add(listPhones.get(i));
            }
        }
        Log.e("hvv1312", "onCreate: " + listPhoneNumber);
        //Lấy list email
        ArrayList<String> listEmailName = new ArrayList<>();
        for (int i = 0; i < listEmails.size(); i++) {

            if (!listEmailName.contains(listEmails.get(i).getEmail())) {
                listEmailName.add(listEmails.get(i).getEmail());
                listEmailMer.add(listEmails.get(i));
            }
        }
        Log.e("hvv1312", "onCreate: " + listEmailName);
        //tạo contact mới
        switch (type) {
            case "contact":
            case "name":
                List<String> contactName = new ArrayList<>();
                for (int i = 0; i < contacts.size(); i++) {
                    if (!contactName.contains(contacts.get(i).getName())) {
                        contactName.add(contacts.get(i).getName());
                    } else {
                        if (valueDuplicate.equals("")) {
                            valueDuplicate = contacts.get(i).getName();
                        }
                    }
                }
                break;
            case "phone":
                List<String> phones = new ArrayList<>();
                for (int i = 0; i < contacts.size(); i++) {
                    for (int j = 0; j < contacts.get(i).getPhones().size(); j++) {
                        if (!phones.contains(contacts.get(i).getPhones().get(j).getPhone())) {
                            phones.add(contacts.get(i).getPhones().get(j).getPhone());
                        } else {
                            if (valueDuplicate.equals("")) {
                                valueDuplicate = contacts.get(i).getPhones().get(j).getPhone();
                            }
                        }
                    }
                }
                break;
            case "email":
                List<String> emails = new ArrayList<>();
                for (int i = 0; i < contacts.size(); i++) {
                    for (int j = 0; j < contacts.get(i).getEmails().size(); j++) {
                        if (!emails.contains(contacts.get(i).getEmails().get(j).getEmail())) {
                            emails.add(contacts.get(i).getEmails().get(j).getEmail());
                        } else {
                            if (valueDuplicate.equals("")) {
                                valueDuplicate = contacts.get(i).getEmails().get(j).getEmail();
                            }
                        }
                    }
                }
                break;
        }
        contactMerger = new Contact("0", String.valueOf(number), contacts.get(0).getName(), "image", KEY.FALSE, listPhoneMer, Collections.singletonList(contacts.get(0).getAccounts().get(0)), listEmailMer);


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
                openMergerDialog();
            }
        });
    }

    private BroadcastReceiver renameContactMerger = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getStringExtra(KEY.RENAME);
            contactMerger.setName(name);
            txtContactName.setText(name);
        }
    };

    private void openMergerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_qs_yes_no);

        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        TextView txtTitleToolBar = dialog.findViewById(R.id.txtTitleToolBar);
        TextView txtBody = dialog.findViewById(R.id.txtBody);

        txtTitleToolBar.setText("Warning!!!");
        txtTitleToolBar.setTextColor(getResources().getColor(R.color.red));
        txtBody.setText("New contacts will save to phone book\nAll duplicates contacts will remove!\n Do you want to continues?");

        btnYes.setText("YES");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < contacts.size(); i++) {
                    //Delete phone book
                    deleteContact(i);
                    //Delete from list
                    contactList.remove(contacts.get(i));
                    //Delete db
                    DBHelper.getInstance(MergerDuplicateActivity.this).updateDisableContact(contacts.get(i).getIdContact());
                }
                // Add new contact to db
                //add to list
                contactList.add(new Contact("idTable", String.valueOf(number), contactMerger.getName(), contactMerger.getImage(), KEY.FALSE, listPhoneMer, contactMerger.getAccounts(), listEmailMer));
                Collections.sort(contactList);
                //Add new to db
                DBHelper.getInstance(MergerDuplicateActivity.this).addContact(contactMerger.getIdContact(), contactMerger.getName(), contactMerger.getImage(), KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE);
                String idTable = DBHelper.getInstance(MergerDuplicateActivity.this).getLastID();
                String idContact = DBHelper.getInstance(MergerDuplicateActivity.this).getLastContactID(idTable);
                if (listPhones.size() > 0) {
                    for (int i = 0; i < listPhoneMer.size(); i++) {
                        DBHelper.getInstance(MergerDuplicateActivity.this).addPhone(idContact, listPhoneMer.get(i).getPhone(), KEY.FALSE);
                    }
                }
                if (listEmailMer.size() > 0) {
                    for (int i = 0; i < listEmailMer.size(); i++) {
                        DBHelper.getInstance(MergerDuplicateActivity.this).addEmail(idContact, listEmailMer.get(i).getEmail(), KEY.FALSE);
                    }
                }
                DBHelper.getInstance(MergerDuplicateActivity.this).addAccount(idContact, contacts.get(0).getAccounts().get(0).getAccountName(), contacts.get(0).getAccounts().get(0).getAccountType(), KEY.FALSE);
                addNewContactPhoneBook();
                sendBroadcast(new Intent(ContactFragment.ACTION_UPDATE_LIST_CONTACT));
                sendBroadcast(new Intent(ManageFragment.ACTION_RELOAD_FRAGMENT_MANAGE));
                sendBroadcast(new Intent(DeleteFragment.ACTION_UPDATE_DELETE_FRAGMENT));
                sendBroadcast(new Intent(DuplicateActivity.ACTION_FINISH_ACTIVITY));
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

       dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                    .withValue(android.provider.ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, s)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK).build()); //Type like HOME, MOBILE etc
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
