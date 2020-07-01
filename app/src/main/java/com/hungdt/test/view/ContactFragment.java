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
import android.os.AsyncTask;
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
import static com.hungdt.test.view.MainActivity.contactList;

public class ContactFragment extends Fragment {

    private ContactAdapter contactAdapter;
    private RecyclerView rcvContactView;
    private LinearLayout llButtonMerger;
    private LoadingDialog loadingDialog;
    public static final String ACTION_UPDATE_LIST_CONTACT = "Update Contact";

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
        Ads.initNativeGgFb((LinearLayout) view.findViewById(R.id.lnNative), getActivity(), true);

        rcvContactView = view.findViewById(R.id.rcvListContact);
        llButtonMerger = view.findViewById(R.id.llButtonMerger);

        llButtonMerger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.isConnectedInternet(getActivity())){
                    openReloadContactDialog();
                }else {
                    if(MySetting.getGems(getContext())>0){
                        openReloadContactGemDialog();
                    }else {
                        Toast.makeText(getContext(), "Your GEM is not enough!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Collections.sort(contactList);
        contactAdapter = new ContactAdapter(view.getContext(), contactList, KEY.CONTACT);
        rcvContactView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rcvContactView.setAdapter(contactAdapter);

        IntentFilter intentFilter = new IntentFilter(ACTION_UPDATE_LIST_CONTACT);
        getLayoutInflater().getContext().registerReceiver(broadCastUpdate, intentFilter);

    }

    private void openReloadContactGemDialog() {
        final Dialog dialog = new Dialog(getLayoutInflater().getContext());
        dialog.setContentView(R.layout.dialog_qs_yes_no);

        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        TextView txtTitleToolBar = dialog.findViewById(R.id.txtTitleToolBar);
        TextView txtBody = dialog.findViewById(R.id.txtBody);

        txtTitleToolBar.setText("Reload contact");
        txtBody.setText("Use 5 GEM to reload all contacts");

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySetting.setGems(getContext(),MySetting.getGems(getContext())-5);
                getContext().sendBroadcast(new Intent(MainActivity.ACTION_UPDATE_GEM));
                DBHelper.getInstance(getActivity()).reloadContact();
                new ReloadContact().execute();
                Toast.makeText(getContext(), "Your GEM is "+MySetting.getGems(getContext())+" !!!", Toast.LENGTH_SHORT).show();
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

    private BroadcastReceiver broadCastUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            contactAdapter.notifyDataSetChanged();
        }
    };

    private void openReloadContactDialog() {
        final Dialog dialog = new Dialog(getLayoutInflater().getContext());
        dialog.setContentView(R.layout.dialog_qs_yes_no);

        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        TextView txtTitleToolBar = dialog.findViewById(R.id.txtTitleToolBar);
        TextView txtBody = dialog.findViewById(R.id.txtBody);

        txtTitleToolBar.setText("Reload contact");
        txtBody.setText("Watch a video to reload all contact?");

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

    private boolean rewardedVideoCompleted = false;

    private void loadVideoAds() {
        if (Helper.isConnectedInternet(getActivity())) {
            videoAds = MobileAds.getRewardedVideoAdInstance(getActivity());
            videoAds.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewarded(RewardItem reward) {
                    rewardedVideoCompleted = true;
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    if (rewardedVideoCompleted) {
                        DBHelper.getInstance(getActivity()).reloadContact();
                        new ReloadContact().execute();
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

    private class ReloadContact extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("111", "onPreExecute: ");
            loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.startLoadingDialog();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("111", "doInBackground: ");
            contactList.clear();
            if (DBHelper.getInstance(getContext()).getAllContact().size() == 0) {
                ((MainActivity) getActivity()).readAccountContacts();
                getActivity().sendBroadcast(new Intent(ManageFragment.ACTION_RELOAD_FRAGMENT_MANAGE));
                getActivity().sendBroadcast(new Intent(DeleteFragment.ACTION_UPDATE_DELETE_FRAGMENT));
            } else {
                contactList.addAll(DBHelper.getInstance(getContext()).getAllContact());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("111", "onPostExecute: ");
            super.onPostExecute(aVoid);
            contactAdapter.notifyDataSetChanged();
            loadingDialog.dismissDialog();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getLayoutInflater().getContext().unregisterReceiver(broadCastUpdate);
    }
}
