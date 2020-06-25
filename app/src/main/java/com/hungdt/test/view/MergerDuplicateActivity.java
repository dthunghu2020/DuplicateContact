package com.hungdt.test.view;

import android.app.Activity;
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
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.DuplicateContactAdapter;
import com.unity3d.ads.UnityAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.hungdt.test.view.MainActivity.showInterstitial;

public class MergerDuplicateActivity extends AppCompatActivity {
    private static final int REQUEST_CONTACT_CODE = 100;
    private String type;
    private List<String> idContact = new ArrayList<>();
    private List<String> listPhones = new ArrayList<>();
    private List<String> listEmails = new ArrayList<>();
    private List<Contact> contacts = new ArrayList<>();
    private Contact contactMerger;
    private ImageView imgContact, imgBack;
    private TextView txtContactName;
    private RecyclerView rcvDupContact;
    private ConstraintLayout clContactMerger;
    private DuplicateContactAdapter dupContactAdapter;
    private LinearLayout llButtonMerger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merger_duplicate);

        rcvDupContact = findViewById(R.id.rcvDupContact);
        imgContact = findViewById(R.id.imgContact);
        imgBack = findViewById(R.id.imgBack);
        txtContactName = findViewById(R.id.txtContactName);
        clContactMerger = findViewById(R.id.clContactMerger);
        llButtonMerger = findViewById(R.id.llButtonMerger);

        Ads.initNativeGgFb((LinearLayout) findViewById(R.id.lnNative), this, true);
        Intent intent = getIntent();
        type = intent.getStringExtra(KEY.DUP);
        Log.e("123321", "onCreate: "+type );

        assert type != null;
        if (type.equals("contact") || type.equals("name") || type.equals("email") || type.equals("phone")) {
            idContact.addAll(Objects.requireNonNull(intent.getStringArrayListExtra(KEY.LIST_ID)));
            for (int i = 0; i < idContact.size(); i++) {
                contacts.add(DBHelper.getInstance(this).getDuplicateContact(String.valueOf(idContact.get(i))));
                txtContactName.setText(contacts.get(0).getName());
            }
            Collections.sort(contacts);
            for (int i = 0; i < contacts.size(); i++) {

                /*listPhones.addAll(contacts.get(i).getPhones());
                listEmails.addAll(contacts.get(i).getEmails());*/
            }
            ArrayList<String> listP = new ArrayList<>();
            for (int i = 0; i < listPhones.size(); i++) {
                if (!listP.contains(listPhones.get(i))) {
                    listP.add(listPhones.get(i));
                }
            }
            listPhones.clear();
            listPhones.addAll(listP);
            ArrayList<String> listE = new ArrayList<>();
            for (int i = 0; i < listEmails.size(); i++) {
                if (!listE.contains(listEmails.get(i))) {
                    listE.add(listEmails.get(i));
                }
            }
            listEmails.clear();
            listEmails.addAll(listE);

            Random rd = new Random();
            int number = rd.nextInt();
            Log.e("123123", "onCreate: " + listPhones + listEmails);
            Log.e("123123", "onCreate: " + listP + listE);
           /* switch (type) {
                case "email":
                    contactMerger = new Contact(String.valueOf(number), "0", contacts.get(0).getName(), "image", KEY.TRUE,KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.TRUE, KEY.TRUE, KEY.FALSE, listPhones,contacts.get(0).getAccount(), listEmails);
                case "phone":
                    contactMerger = new Contact(String.valueOf(number), "0", contacts.get(0).getName(), "image", KEY.TRUE,  KEY.FALSE,KEY.FALSE, KEY.TRUE, KEY.FALSE, KEY.TRUE, KEY.FALSE, listPhones, contacts.get(0).getAccount(), listEmails);
                case "name":
                    contactMerger = new Contact(String.valueOf(number), "0", contacts.get(0).getName(), "image", KEY.TRUE, KEY.FALSE, KEY.TRUE, KEY.FALSE, KEY.FALSE, KEY.TRUE, KEY.FALSE, listPhones, contacts.get(0).getAccount(), listEmails);
                case "contact":
                    contactMerger = new Contact(String.valueOf(number), "0", contacts.get(0).getName(), "image", KEY.TRUE, KEY.TRUE, KEY.TRUE, KEY.TRUE, KEY.FALSE, KEY.TRUE, KEY.FALSE, listPhones, contacts.get(0).getAccount(), listEmails);
                    break;
            }*/

        } else {
            contacts.addAll(DBHelper.getInstance(this).getContactMerged(type));

            Log.e("123123123", "onCreate: "+type);
            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).getFather().equals(KEY.TRUE)) {
                    txtContactName.setText(contacts.get(i).getName());
                    contactMerger = contacts.get(i);
                }
            }
            contacts.remove(contactMerger);
            for(int i = 0 ; i< contacts.size() ; i++){
                Log.e("123123123", "onCreate: "+contacts.get(i).getName());
            }
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
                startActivityForResult(intent1, REQUEST_CONTACT_CODE);
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
                    Random rd = new Random();
                    int number = rd.nextInt();
                    //check
                    String mN = KEY.FALSE;
                    String mP = KEY.FALSE;
                    String mE = KEY.FALSE;
                   /* switch (type) {
                        case "phone":
                            mP = KEY.TRUE;
                            for (int i = 0; i < contacts.size(); i++) {
                                //todo
                                DBHelper.getInstance(MergerDuplicateActivity.this).updateContactMerger(contacts.get(i).getIdContact(), contacts.get(i).getmContact(), contacts.get(i).getmName(),KEY.TRUE, contacts.get(i).getmEmail(), type, String.valueOf(number));
                            }
                            break;
                        case "email":
                            mE = KEY.TRUE;
                            for (int i = 0; i < contacts.size(); i++) {
                                DBHelper.getInstance(MergerDuplicateActivity.this).updateContactMerger(contacts.get(i).getIdTable(), contacts.get(i).getIdContact(), contacts.get(i).getmContact(),contacts.get(i).getmName(), contacts.get(i).getmPhone(), KEY.TRUE, String.valueOf(number));
                            }
                            break;
                        case "name":
                            mN = KEY.TRUE;
                            for (int i = 0; i < contacts.size(); i++) {
                                DBHelper.getInstance(MergerDuplicateActivity.this).updateContactMerger(contacts.get(i).getIdTable(), contacts.get(i).getIdContact(),contacts.get(i).getmContact(),  KEY.TRUE, contacts.get(i).getmPhone(), contacts.get(i).getmEmail(), String.valueOf(number));
                            }
                            break;
                        case "contact":
                            for (int i = 0; i < contacts.size(); i++) {
                                DBHelper.getInstance(MergerDuplicateActivity.this).updateContactMerger(contacts.get(i).getIdTable(), contacts.get(i).getIdContact(), KEY.TRUE, KEY.TRUE, KEY.TRUE,KEY.TRUE, String.valueOf(number));
                            }
                            break;
                    }*/
                    //check
                    DBHelper.getInstance(MergerDuplicateActivity.this).addContact(contactMerger.getIdContact(), contactMerger.getName(), contactMerger.getImage(), String.valueOf(number),contactMerger.getmContact(), contactMerger.getmName(), contactMerger.getmPhone(), contactMerger.getmEmail(), KEY.TRUE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE);
                    String id = DBHelper.getInstance(MergerDuplicateActivity.this).getLastID();
                    String idContact = DBHelper.getInstance(MergerDuplicateActivity.this).getLastContactID(id);

                  /*  if (listPhones.size() > 0) {
                        for (int i = 0; i < listPhones.size(); i++) {
                            DBHelper.getInstance(MergerDuplicateActivity.this).addPhone(id, idContact, KEY.TRUE, mN, mP, mE, listPhones.get(i));
                        }
                    }
                    DBHelper.getInstance(MergerDuplicateActivity.this).addAccount(id, contacts.get(0).getAccount().get(0).getAccountName(),  contacts.get(0).getAccount().get(0).getAccountType());
                    if (listEmails.size() > 0) {
                        for (int i = 0; i < listEmails.size(); i++) {
                            DBHelper.getInstance(MergerDuplicateActivity.this).addEmail(id, idContact, KEY.TRUE, mN, mP, mE, listEmails.get(i));
                        }
                    }*/
                    if (ContactConfig.getInstance().getConfig().getBoolean("config_on")) {
                        if (MainActivity.ggInterstitialAd != null && MainActivity.ggInterstitialAd.isLoaded())
                            MainActivity.ggInterstitialAd.show();
                        else if (UnityAds.isInitialized() && UnityAds.isReady(getString(R.string.INTER_UNI)))
                            UnityAds.show(MergerDuplicateActivity.this, getString(R.string.INTER_UNI));
                    }

                    sendBroadcast(new Intent(DuplicateActivity.ACTION_FINISH_ACTIVITY));
                    Toast.makeText(MergerDuplicateActivity.this, "Save to Merger!", Toast.LENGTH_SHORT).show();
                    if (ContactConfig.getInstance().getConfig().getBoolean("config_on")) {
                        if (!showInterstitial()) {
                            if (UnityAds.isInitialized() && UnityAds.isReady(getString(R.string.INTER_UNI)))
                                UnityAds.show(MergerDuplicateActivity.this, getString(R.string.INTER_UNI));
                        }
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

        txtTitleToolBar.setText("Merger Contact");
        txtBody.setText("Contact merger will be save to phone book\nand duplicate contact will be deleted");

        btnYes.setText("YES");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < contacts.size(); i++) {
                    if (!contacts.get(i).getFather().equals(KEY.TRUE)) {
                        deleteContact(i);
                        DBHelper.getInstance(MergerDuplicateActivity.this).updateDisableContact(contacts.get(i).getIdTable());
                    }
                }
                DBHelper.getInstance(MergerDuplicateActivity.this).updateMergedContact(contactMerger.getIdTable());
                addNewContact();
                Toast.makeText(MergerDuplicateActivity.this, "Merger Success!!!", Toast.LENGTH_SHORT).show();
                if (ContactConfig.getInstance().getConfig().getBoolean("config_on")) {
                    if (MainActivity.ggInterstitialAd != null && MainActivity.ggInterstitialAd.isLoaded())
                        MainActivity.ggInterstitialAd.show();
                    else if (UnityAds.isInitialized() && UnityAds.isReady(getString(R.string.INTER_UNI)))
                        UnityAds.show(MergerDuplicateActivity.this, getString(R.string.INTER_UNI));
                }
                sendBroadcast(new Intent(MergedFragment.ACTION_RELOAD_FRAGMENT_MERGED));
                dialog.dismiss();

                finish();
            }
        });
        btnNo.setText("BACK");
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

    private void addNewContact() {
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

        /*for (String s : contactMerger.getPhone()) {
            cntProOper.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)//Step 3
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                    .withValue(android.provider.ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, s)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc
        }
        for (String s : contactMerger.getEmail()) {
            cntProOper.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                    .withValue(android.provider.ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME, s)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM).build()); //Type like HOME, MOBILE etc
        }*/

        ContentProviderResult[] s = new ContentProviderResult[0];
        try {
            s = getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper);
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONTACT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                contactMerger.setName(data.getStringExtra(KEY.RESULT));
                txtContactName.setText(data.getStringExtra(KEY.RESULT));
            }
        }
    }
}
