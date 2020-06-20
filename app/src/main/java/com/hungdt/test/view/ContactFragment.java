package com.hungdt.test.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Account;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.Helper;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.utils.MySetting;
import com.hungdt.test.view.adapter.ContactAdapter;
import com.unity3d.ads.UnityAds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ContactFragment extends Fragment {

    private List<Contact> contactList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private RecyclerView rcvContactView;
    private String idContact;
    private String name;
    private String image;
    private LinearLayout llButtonMerger;
    private List<Account> accounts = new ArrayList<>();
    private List<String> phones = new ArrayList<>();
    private List<String> emails = new ArrayList<>();

    private Calendar calendar;

    public ContactFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendar = Calendar.getInstance();
        Log.i("TAG", "onViewCreated: " + calendar.getTimeInMillis());

        rcvContactView = view.findViewById(R.id.rcvListContact);
        llButtonMerger = view.findViewById(R.id.llButtonMerger);

        contactList = DBHelper.getInstance(getActivity()).getAllContact();
        if (contactList.size() == 0) {
            readAccountContacts();
            contactList = DBHelper.getInstance(getActivity()).getAllContact();
        }
        llButtonMerger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReloadContactDialog();
            }
        });

        Collections.sort(contactList);
        contactAdapter = new ContactAdapter(view.getContext(), contactList, KEY.CONTACT);
        rcvContactView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rcvContactView.setAdapter(contactAdapter);

        /*IntentFilter intentFilter = new IntentFilter(MainActivity.ACTION_UPDATE);
        getLayoutInflater().getContext().registerReceiver(bcUpdateContact,intentFilter);*/

    }


    private void openReloadContactDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_qs_yes_no);

        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        TextView txtTitleToolBar = dialog.findViewById(R.id.txtTitleToolBar);
        TextView txtBody = dialog.findViewById(R.id.txtBody);

        txtBody.setText("All Contact will be reload!");
        txtTitleToolBar.setText("Reload contact");

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

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    ProgressDialog progressDialog;
    Dialog morePlaceDialog;

    private RewardedVideoAd videoAds;

    private boolean rewardedVideoCompleted = false;

    private void loadVideoAds() {
        if (Helper.isConnectedInternet(getActivity())) {
            videoAds = MobileAds.getRewardedVideoAdInstance(getActivity());
            videoAds.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewarded(RewardItem reward) {
                    MySetting.setMaxLength(getActivity(), MySetting.getMaxLength(getActivity()) + 3);
                    rewardedVideoCompleted = true;
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    if (rewardedVideoCompleted) {
                        DBHelper.getInstance(getActivity()).deleteAllContact();
                        startActivity(new Intent(getActivity(), WaitingActivity.class));
                        readAccountContacts();
                        contactList = DBHelper.getInstance(getActivity()).getAllContact();
                        contactAdapter.notifyDataSetChanged();
                        getActivity().sendBroadcast(new Intent(ManageFragment.ACTION_RELOAD_FRAGMENT_MANAGE));
                    }
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int errorCode) {
                    try {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getActivity(), "Loading video failed, please try again later", Toast.LENGTH_SHORT).show();
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
            if (ConsentInformation.getInstance(getActivity()).getConsentStatus().toString().equals(ConsentStatus.PERSONALIZED) ||
                    !ConsentInformation.getInstance(getActivity()).isRequestLocationInEeaOrUnknown()) {
                adRequest = new AdRequest.Builder().build();
            } else {
                adRequest = new AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter.class, Ads.getNonPersonalizedAdsBundle())
                        .build();
            }
            videoAds.loadAd(getString(R.string.VIDEO_G), adRequest);

            try {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIcon(R.drawable.app_icon);
                progressDialog.setMessage("Please wait, the Ad is loaded...");
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
                            videoAds.destroy(getContext());
                        }
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Loading video failed, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 15000);
        } else {
            Toast.makeText(getActivity(), "Please check your internet connection!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void readAccountContacts() {

        String[] projections = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI,
        };
        Cursor cursorContact = getLayoutInflater().getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projections, null, null, null);
        while (cursorContact != null && cursorContact.moveToNext()) {

            idContact = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts._ID));
            name = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            image = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
            String[] projection2 = {
                    ContactsContract.RawContacts._ID,
                    ContactsContract.RawContacts.CONTACT_ID,
                    ContactsContract.RawContacts.ACCOUNT_NAME,
                    ContactsContract.RawContacts.ACCOUNT_TYPE
            };
            //val stringZalo =
            String selection = ContactsContract.RawContacts.CONTACT_ID + " = " + idContact;
            //val selection = "${ContactsContract.RawContacts._ID} = $idContact";
            Cursor cursorAccount = getLayoutInflater().getContext().getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection2, selection, null, null, null);
            while (cursorAccount != null && cursorAccount.moveToNext()) {
                String idRawContract = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts._ID));
                String account = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
                String accountType = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
                //readDataXXX(idContact)
                accounts.add(new Account(account, accountType));
                String[] projectionx = {
                        ContactsContract.Data.RAW_CONTACT_ID,
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.Data.DATA1,
                        ContactsContract.Data.DATA2,
                };
                String selectionx = ContactsContract.Data.RAW_CONTACT_ID + " = " + idRawContract;

                Cursor cursorData = getLayoutInflater().getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, projectionx, selectionx, null, null);
                while (cursorData != null && cursorData.moveToNext()) {
                    if (accountType.contains("xiaomi") || accountType.contains("google") || accountType.contains("pcsc") ||
                            accountType.contains("phone") || accountType.contains("Phone") || accountType.contains("PHONE") || accountType.contains("Sim") || accountType.contains("sim") || accountType.contains("SIM")) {
                        String data1 = cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.DATA1));
                        String mineType = cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.MIMETYPE));
                        if (mineType.equals("vnd.android.cursor.item/phone_v2")) {
                            phones.add(data1);
                        } else if (mineType.equals("vnd.android.cursor.item/email_v2")) {
                            emails.add(data1);
                        }
                    }

                }
                assert cursorData != null;
                cursorData.close();
            }
            assert cursorAccount != null;
            cursorAccount.close();
            boolean noPhone = false;
            boolean noName = true;
            boolean noEmail = false;
            if (phones.isEmpty()) {
                noPhone = true;
                Log.i("TAG", "readAccountContacts: " + name);
            }
            if (!name.equals("")) {
                noName = false;
            }
            if (emails.isEmpty()) {
                noEmail = true;
            }

            contactList.add(new Contact("0", idContact, name, image, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, phones, accounts, emails));
            if (image != null) {
                DBHelper.getInstance(getActivity()).addContact(idContact, name, image, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, String.valueOf(noName), String.valueOf(noPhone), String.valueOf(noEmail));
            } else {
                DBHelper.getInstance(getActivity()).addContact(idContact, name, "image", KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, String.valueOf(noName), String.valueOf(noPhone), String.valueOf(noEmail));
            }

            String id = DBHelper.getInstance(getActivity()).getLastID();
            String idContact = DBHelper.getInstance(getActivity()).getLastContactID(id);
            if (!noPhone) {
                for (int i = 0; i < phones.size(); i++) {
                    DBHelper.getInstance(getActivity()).addPhone(id, idContact, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, phones.get(i));
                }
            }
            if (!accounts.isEmpty()) {
                for (int i = 0; i < accounts.size(); i++) {
                    DBHelper.getInstance(getActivity()).addAccount(id, accounts.get(i).getAccountName(), accounts.get(i).getAccountType());
                }
            }
            if (!noEmail) {
                for (int i = 0; i < emails.size(); i++) {
                    DBHelper.getInstance(getActivity()).addEmail(id, idContact, KEY.FALSE, KEY.FALSE, KEY.FALSE, KEY.FALSE, emails.get(i));
                }
            }
            image = null;
            phones.clear();
            accounts.clear();
            emails.clear();


        }
        assert cursorContact != null;
        cursorContact.close();
    }

    /*private BroadcastReceiver bcUpdateContact = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            *//*String name = intent.getStringExtra(KEY);
            txt.setText(name);*//*
        }
    };*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        //getLayoutInflater().getContext().unregisterReceiver(bcUpdateContact);
    }
}
