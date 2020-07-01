package com.hungdt.test.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.hungdt.test.ContactConfig;
import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.Helper;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.SelectAdapter;
import com.unity3d.ads.UnityAds;

import java.util.ArrayList;
import java.util.List;

import static com.hungdt.test.view.MainActivity.showInterstitial;

public class RestoreActivity extends AppCompatActivity {
    private ImageView imgReload, imgBack;
    private RecyclerView rcvBackup;
    private LinearLayout llEmpty, llButtonRestore;
    private CheckBox cbAll;
    private SelectAdapter backupAdapter;
    private TextView txtBackup;
    private List<Contact> contacts = new ArrayList<>();
    private Boolean getBackup = false;
    private boolean rewardedVideoCompleted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);

        initView();
        txtBackup.setText("View Back Up");

        imgReload.setVisibility(View.GONE);
        cbAll.setVisibility(View.GONE);
        rcvBackup.setVisibility(View.GONE);
        llEmpty.setVisibility(View.GONE);

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
                backupAdapter.notifyDataSetChanged();
            }
        });

        imgReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackupDialog();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        llButtonRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getBackup) {
                    openBackupDialog();
                } else {
                    if (contacts.size() == 0) {
                        Toast.makeText(RestoreActivity.this, "No contact backup!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean dataCheck = false;
                        for (int i = 0; i < contacts.size(); i++) {
                            if (contacts.get(i).isTicked()) {
                                dataCheck = true;
                            }
                        }
                        if (dataCheck) {
                            List<Contact> contactSave = new ArrayList<>();
                            for (int i = 0; i < contacts.size(); i++) {
                                if (contacts.get(i).isTicked()) {
                                    contacts.get(i).setDeleted(KEY.FALSE);
                                    //add list
                                    //contactList.add(contacts.get(i));
                                    //Update DB
                                    DBHelper.getInstance(RestoreActivity.this).deleteBackupContact(contacts.get(i).getIdContact());
                                    //Add new contact to phone book.
                                    addNewContact(contacts.get(i));
                                    contactSave.add(contacts.get(i));
                                }
                            }
                            //Collections.sort(contactList);
                            contacts.removeAll(contactSave);
                            backupAdapter.notifyDataSetChanged();
                            sendBroadcast(new Intent(ContactFragment.ACTION_UPDATE_LIST_CONTACT));
                            sendBroadcast(new Intent(ManageFragment.ACTION_RELOAD_FRAGMENT_MANAGE));
                            sendBroadcast(new Intent(DeleteFragment.ACTION_UPDATE_DELETE_FRAGMENT));
                            Toast.makeText(RestoreActivity.this, "Backup Success!\n You can check on your phone book!!!", Toast.LENGTH_SHORT).show();
                            if (ContactConfig.getInstance().getConfig().getBoolean("config_on")) {
                                if (!showInterstitial()) {
                                    if (UnityAds.isInitialized() && UnityAds.isReady(getString(R.string.INTER_UNI)))
                                        UnityAds.show(RestoreActivity.this, getString(R.string.INTER_UNI));
                                }
                            }
                        } else {
                            Toast.makeText(RestoreActivity.this, "No contact backup!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }


    private void addNewContact(Contact contact) {
        ArrayList<ContentProviderOperation> cntProOper = new ArrayList<>();
        int contactIndex = cntProOper.size();

        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)//Step1
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        cntProOper.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)//Step2
                .withValueBackReference(android.provider.ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(android.provider.ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName()) // Name of the contact
                .build());

        List<String> phones = new ArrayList<>();
        for (int i = 0; i < contact.getPhones().size(); i++) {
            phones.add(contact.getPhones().get(i).getPhone());
        }
        for (String s : phones) {
            cntProOper.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)//Step 3
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                    .withValue(android.provider.ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, s)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc
        }
        List<String> emails = new ArrayList<>();
        for (int i = 0; i < contact.getEmails().size(); i++) {
            emails.add(contact.getEmails().get(i).getEmail());
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
        } catch (OperationApplicationException | RemoteException e) {
            e.printStackTrace();
        }
    }

    private void openBackupDialog() {
        final Dialog dialog = new Dialog(getLayoutInflater().getContext());
        dialog.setContentView(R.layout.dialog_qs_yes_no);

        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        TextView txtTitleToolBar = dialog.findViewById(R.id.txtTitleToolBar);
        TextView txtBody = dialog.findViewById(R.id.txtBody);

        txtTitleToolBar.setText("Get Back Up Contact");
        txtBody.setText("Watch a video to get backup contact");

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadVideoAds();
                dialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    ProgressDialog progressDialog;
    private RewardedVideoAd videoAds;


    private void loadVideoAds() {
        if (Helper.isConnectedInternet(this)) {
            videoAds = MobileAds.getRewardedVideoAdInstance(this);
            videoAds.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewarded(RewardItem reward) {
                    rewardedVideoCompleted = true;
                    getBackup = true;
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    if (rewardedVideoCompleted) {
                        contacts = DBHelper.getInstance(RestoreActivity.this).getAllBackUpContact();
                        for (int i = 0; i < contacts.size(); i++) {
                            Log.e("1", "1 name: " + contacts.get(i).getName());
                            for (int j = 0; j < contacts.get(i).getPhones().size(); j++) {
                                Log.e("1", "2 phones: " + contacts.get(i).getPhones().get(j).getPhone());
                            }
                            for (int j = 0; j < contacts.get(i).getEmails().size(); j++) {
                                Log.e("1", "3 emails: " + contacts.get(i).getEmails().get(j).getEmail());
                            }
                        }
                        backupAdapter = new SelectAdapter(RestoreActivity.this, contacts, KEY.BACKUP);
                        rcvBackup.setLayoutManager(new LinearLayoutManager(RestoreActivity.this));
                        rcvBackup.setAdapter(backupAdapter);
                        imgReload.setVisibility(View.VISIBLE);
                        if (contacts.size() == 0) {
                            llEmpty.setVisibility(View.VISIBLE);
                        } else {
                            cbAll.setVisibility(View.VISIBLE);
                            rcvBackup.setVisibility(View.VISIBLE);
                        }
                        txtBackup.setText("Back Up");
                    }
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int errorCode) {
                    try {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(RestoreActivity.this, "Loading video failed, please try again later", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onRewardedVideoAdLoaded() {
                    if (videoAds != null && videoAds.isLoaded()) videoAds.show();
                    try {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onRewardedVideoAdOpened() {
                }

                @Override
                public void onRewardedVideoStarted() {
                }

                @Override
                public void onRewardedVideoCompleted() {

                }
            });

            AdRequest adRequest;
            if (ConsentInformation.getInstance(RestoreActivity.this).getConsentStatus().toString().equals(ConsentStatus.PERSONALIZED) ||
                    !ConsentInformation.getInstance(RestoreActivity.this).isRequestLocationInEeaOrUnknown()) {
                adRequest = new AdRequest.Builder().build();
            } else {
                adRequest = new AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter.class, Ads.getNonPersonalizedAdsBundle())
                        .build();
            }
            videoAds.loadAd(getString(R.string.VIDEO_G), adRequest);

            try {
                progressDialog = new ProgressDialog(RestoreActivity.this);
                progressDialog.setIcon(R.drawable.app_icon);
                progressDialog.setMessage("Please wait, the Ad is loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (videoAds != null && !videoAds.isLoaded()) {
                            videoAds.destroy(RestoreActivity.this);
                        }
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            Toast.makeText(RestoreActivity.this, "Loading video failed, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 15000);
        } else {
            Toast.makeText(RestoreActivity.this, "Please check your internet connection!!!", Toast.LENGTH_SHORT).show();
        }
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


    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        txtBackup = findViewById(R.id.txtRestore);
        rcvBackup = findViewById(R.id.rcvBackup);
        llEmpty = findViewById(R.id.llEmpty);
        imgReload = findViewById(R.id.imgReload);
        llButtonRestore = findViewById(R.id.llButtonRestore);
        cbAll = findViewById(R.id.cbAll);
    }


}
