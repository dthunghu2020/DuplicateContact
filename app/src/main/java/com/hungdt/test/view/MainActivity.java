package com.hungdt.test.view;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.billingclient.api.BillingClientStateListener;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.hungdt.test.ContactConfig;
import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.Helper;
import com.hungdt.test.utils.MySetting;
import com.hungdt.test.view.adapter.ViewPageAdapter;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tabContacts, tabManager, tabMerged, tabDelete, tabVIP;

    private ImageView imgMenu, imgRemoveAds, imgGift;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ViewPageAdapter viewPageAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    final Calendar calendar = Calendar.getInstance();
    private BillingProcessor bp;
    private RewardedVideoAd videoAds;
    private boolean readyToPurchase = false;
    private boolean rewardedVideoCompleted = false;
    private boolean isDailyReward = false;
    public static InterstitialAd ggInterstitialAd;
    public static com.facebook.ads.InterstitialAd fbInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            bp = BillingProcessor.newBillingProcessor(this, getString(R.string.BASE64), this); // doesn't bind
            bp.initialize(); // binds
        } catch (Exception e) {
            e.printStackTrace();
        }

        initView();
        //readAccountContacts();
        initInterstitialAd();
        View hView = navigationView.getHeaderView(0);
        if (ContactConfig.getInstance().getConfig().getBoolean("config_on")) {
            Ads.initNativeGg((LinearLayout) hView.findViewById(R.id.lnNative), this, true, true);
        }

        arrayList = new ArrayList<>();
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.add(new ContactFragment(), "Contact");
        viewPageAdapter.add(new ManageFragment(), "Manager");
        viewPageAdapter.add(new MergedFragment(), "Merged");
        viewPageAdapter.add(new DeleteFragment(), "Delete");
        viewPageAdapter.add(new VipFragment(), "Upgrade");
        viewPager.setAdapter(viewPageAdapter);

        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        imgRemoveAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    removeAds();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imgRemoveAds.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgRemoveAds.setEnabled(true);
                    }
                }, 1250);
            }
        });


        imgGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoAdsDialog();
            }
        });

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) drawerLayout.openDrawer(Gravity.LEFT);
                else drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_remove_add:
                        try {
                            removeAds();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.nav_rate_us:
                        startActivity(new Intent(MainActivity.this, RateAppActivity.class));
                        break;
                    case R.id.nav_share:
                        com.hungdt.test.utils.Helper.shareApp(MainActivity.this);
                        break;
                    case R.id.nav_policy:
                        startActivity(new Intent(MainActivity.this, PolicyActivity.class));
                        if (!showInterstitial()) {
                            if (UnityAds.isInitialized() && UnityAds.isReady(getString(R.string.INTER_UNI)))
                                UnityAds.show(MainActivity.this, getString(R.string.INTER_UNI));
                        }
                        break;
                    case R.id.nav_feedback_dev:
                        com.hungdt.test.utils.Helper.feedback(MainActivity.this);
                        break;
                    case R.id.nav_more_app:
                        startActivity(new Intent(MainActivity.this, MoreAppActivity.class));
                        if (!showInterstitial()) {
                            if (UnityAds.isInitialized() && UnityAds.isReady(getString(R.string.INTER_UNI)))
                                UnityAds.show(MainActivity.this, getString(R.string.INTER_UNI));
                        }
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void initInterstitialAd() {
        initUnityInterstitialAd();
        initFbInterstitialAd(false);
        initGgInterstitialAd();
    }
    private void initGgInterstitialAd() {
        try {
            ggInterstitialAd = new InterstitialAd(this);
            ggInterstitialAd.setAdUnitId(getString(R.string.INTER_G));
            ggInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    try {
                        if (fbInterstitialAd != null) fbInterstitialAd.destroy();
                        initFbInterstitialAd(false);
                        requestNewInterstitial();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    try {
                        if (fbInterstitialAd != null) fbInterstitialAd.destroy();
                        initFbInterstitialAd(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }
            });
            requestNewInterstitial();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestNewInterstitial() {
        try {
            if (!MySetting.isRemoveAds(this)) {
                AdRequest adRequest = null;
                if (ConsentInformation.getInstance(this).getConsentStatus().toString().equals(ConsentStatus.PERSONALIZED) ||
                        !ConsentInformation.getInstance(this).isRequestLocationInEeaOrUnknown()) {
                    adRequest = new AdRequest.Builder().build();
                } else {
                    adRequest = new AdRequest.Builder()
                            .addNetworkExtrasBundle(AdMobAdapter.class, Ads.getNonPersonalizedAdsBundle())
                            .build();
                }
                ggInterstitialAd.loadAd(adRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean showInterstitial() {
        if (ggInterstitialAd != null && ggInterstitialAd.isLoaded()) {
            ggInterstitialAd.show();
            return true;
        } else if (fbInterstitialAd != null && fbInterstitialAd.isAdLoaded()) {
            fbInterstitialAd.show();
            return true;
        } else return false;
    }
    private void initFbInterstitialAd(final boolean isLoad_ID_FB_2) {
        try {
            if (!isLoad_ID_FB_2)
                fbInterstitialAd = new com.facebook.ads.InterstitialAd(this, getString(R.string.INTER_FB));
            else
                fbInterstitialAd = new com.facebook.ads.InterstitialAd(this, getString(R.string.INTER_FB_2));
            fbInterstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    requestNewFBInterstitial();
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    if (!isLoad_ID_FB_2) {
                        if (ggInterstitialAd == null) initGgInterstitialAd();
                        else requestNewInterstitial();
                    }
                }

                @Override
                public void onAdLoaded(Ad ad) {

                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });
            requestNewFBInterstitial();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestNewFBInterstitial() {
        try {
            if (!MySetting.isRemoveAds(this)) {
                fbInterstitialAd.loadAd();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUnityInterstitialAd() {
        try {
            if (!MySetting.isRemoveAds(this)) {
                UnityAds.initialize(this, getString(R.string.GAME_ID), new IUnityAdsListener() {
                    @Override
                    public void onUnityAdsReady(String placementId) {
                    }

                    @Override
                    public void onUnityAdsStart(String placementId) {
                    }

                    @Override
                    public void onUnityAdsFinish(String placementId, UnityAds.FinishState result) {
                    }

                    @Override
                    public void onUnityAdsError(UnityAds.UnityAdsError error, String message) {
                    }
                }, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ProgressDialog progressDialog;
    Dialog morePlaceDialog;

    public void openVideoAdsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.video_ads_dialog, null);
        Button btnYes = view.findViewById(R.id.btnYes);
        Button btnBack = view.findViewById(R.id.btnBack);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtBody = view.findViewById(R.id.txtBody);
        txtBody.setText("Would you like to see video ads\nto get maximum free 10 gems?");
        txtTitle.setText("Daily Reward!");


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (morePlaceDialog != null) morePlaceDialog.dismiss();
                drawerLayout.closeDrawers();
                loadVideoAds();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (morePlaceDialog != null) morePlaceDialog.dismiss();
            }
        });


        builder.setView(view);
        builder.setCancelable(false);
        morePlaceDialog = builder.create();
        Objects.requireNonNull(morePlaceDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        morePlaceDialog.show();
    }

    private void loadVideoAds() {
        if (Helper.isConnectedInternet(this)) {
            videoAds = MobileAds.getRewardedVideoAdInstance(this);
            videoAds.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewarded(RewardItem reward) {
                    MySetting.setMaxLength(MainActivity.this, MySetting.getMaxLength(MainActivity.this) + 3);
                    rewardedVideoCompleted = true;
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    if (rewardedVideoCompleted) {
                        //openRewardSuccessDialog(false);
                    }
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int errorCode) {
                    try {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MainActivity.this, "Loading video failed, please try again later", Toast.LENGTH_SHORT).show();
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
            if (ConsentInformation.getInstance(this).getConsentStatus().toString().equals(ConsentStatus.PERSONALIZED) ||
                    !ConsentInformation.getInstance(this).isRequestLocationInEeaOrUnknown()) {
                adRequest = new AdRequest.Builder().build();
            } else {
                adRequest = new AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter.class, Ads.getNonPersonalizedAdsBundle())
                        .build();
            }
            videoAds.loadAd(getString(R.string.VIDEO_G), adRequest);

            try {
                progressDialog = new ProgressDialog(this);
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
                            videoAds.destroy(MainActivity.this);
                        }
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Loading video failed, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 15000);
        } else {
            Toast.makeText(MainActivity.this, "Please check your internet connection!!!", Toast.LENGTH_SHORT).show();
        }
    }


    private void initView() {
        tabLayout = findViewById(R.id.tabBar);
        tabContacts = findViewById(R.id.tabContacts);
        tabManager = findViewById(R.id.tabManager);
        tabMerged = findViewById(R.id.tabMerged);
        tabDelete = findViewById(R.id.tabDelete);
        tabVIP = findViewById(R.id.tabVIP);
        viewPager = findViewById(R.id.viewPager);
        imgMenu = findViewById(R.id.imgMenu);
        imgRemoveAds = findViewById(R.id.imgRemoveAds);
        imgGift = findViewById(R.id.imgGift);
        drawerLayout = findViewById(R.id.draw_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    private void removeAds() {
        try {
            if (readyToPurchase) {
                bp.subscribe(this, getString(R.string.ID_REMOVE_ADS));
            } else {
                Toast.makeText(this, "Billing not initialized", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            Log.i("ABC", "number is:" + phones.getString(phones.getColumnIndex("data1")));
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Log.i("ABC", "Name is" + name);

                    }
                }
                break;
        }

    }*/


    private void getContact() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        assert cursor != null;
        while (cursor.moveToNext()) {
            String DISPLAY_NAME = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));          //Chức vụ Tên Tên lót họ, chức danh
            String NAME_RAW_CONTACT_ID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID)); //1059
            String PHOTO_ID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));                         //null(3038)
            String PHOTO_FILE_ID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_FILE_ID));               //null(1)
            String PHOTO_URI = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));                       //null(content://com.android.contact/display_photo/1)
            String PHOTO_THUMBNAIL_URI = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));    //null(content://com.android.contact/contacts/1063/photo)
            String IN_DEFAULT_DIRECTORY = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IN_DEFAULT_DIRECTORY));     //1
            String IN_VISIBLE_GROUP = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IN_VISIBLE_GROUP));             //1
            //String IS_USER_PROFILE = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IS_USER_PROFILE));
            String HAS_PHONE_NUMBER = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));             //1
            String LOOKUP_KEY = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));                         //key dài
            String CONTACT_LAST_UPDATED_TIMESTAMP = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP)); //1591189343196
            String NUMBER = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));                         //sdt
            String NORMALIZED_NUMBER = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)); //null
            int indexPhoneType = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE);                                 //70
            int label = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LABEL);                                         //69
            arrayList.add(DISPLAY_NAME + "\n" + NAME_RAW_CONTACT_ID + "\n" + PHOTO_ID + "\n" + PHOTO_FILE_ID + "\n" + PHOTO_URI + "\n" + PHOTO_THUMBNAIL_URI +
                    "\n" + IN_DEFAULT_DIRECTORY + "\n" + IN_VISIBLE_GROUP + "\n" + HAS_PHONE_NUMBER + "\n" + LOOKUP_KEY + "\n" + CONTACT_LAST_UPDATED_TIMESTAMP
                    + "\n" + NUMBER + "\n" + NORMALIZED_NUMBER + "\n");

            Log.e("123123", "getContact: " + indexPhoneType);
            Log.e("123123", "getContact: " + label);
        }

        Log.e("123123", "calender " + calendar.getTimeInMillis());


        ContentResolver resolver = getContentResolver();

        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        Cursor contacts = resolver.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
        while (contacts.moveToNext()) {

            long contactId = contacts.getLong(0);
            String name = contacts.getString(1);

            Log.i("Contacts", "Contact " + contactId + " " + name + " - has the following raw-contacts:");

            String[] projection2 = new String[]{ContactsContract.RawContacts._ID, ContactsContract.RawContacts.ACCOUNT_TYPE, ContactsContract.RawContacts.ACCOUNT_NAME};
            Cursor raws = resolver.query(ContactsContract.RawContacts.CONTENT_URI, null, ContactsContract.RawContacts.CONTACT_ID, null, null);

            while (raws.moveToNext()) {

                long rawId = raws.getLong(0);
                String accountType = raws.getString(1);
                String accountName = raws.getString(2);

                Log.i("Contacts", "\t RawContact " + rawId + " from " + accountType + " / " + accountName);
            }
            raws.close();
        }
        contacts.close();
        //getDefaultAccountNameAndType();
        //getContactFromSIM();
        //getContactFromPhoneUse();

    }

    private void getContactFromPhoneUse() {
        Cursor cursor = getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts._ID, ContactsContract.RawContacts.ACCOUNT_TYPE},
                ContactsContract.RawContacts.ACCOUNT_TYPE + " <> 'com.android.contacts.sim' "
                        + " AND " + ContactsContract.RawContacts.ACCOUNT_TYPE + " <> 'com.google' " //if you don't want to google contacts also
                ,
                null,
                null);
        List<String> listName = new ArrayList<>();
        List<String> listContactId = new ArrayList<>();
        List<String> listMobileNo = new ArrayList<>();
        while (cursor.moveToNext()) {
            listName.add(cursor.getString(cursor.getColumnIndex("name")));
            listContactId.add(cursor.getString(cursor.getColumnIndex("_id")));
            listMobileNo.add(cursor.getString(cursor.getColumnIndex("number")));
        }
        Log.e("Contacts", "getDefaultAccountNameAndType: listName, " + listName);
        Log.e("Contacts", "getDefaultAccountNameAndType: listContactId, " + listContactId);
        Log.e("Contacts", "getDefaultAccountNameAndType: listMobileNo, " + listMobileNo);
    }

    private void getContactFromSIM() {
        Uri simUri = Uri.parse("content://icc/adn");
        Cursor cursorSim = this.getContentResolver().query(simUri, null, null, null, null);
        List<String> listName = new ArrayList<>();
        List<String> listContactId = new ArrayList<>();
        List<String> listMobileNo = new ArrayList<>();
        while (cursorSim.moveToNext()) {
            listName.add(cursorSim.getString(cursorSim.getColumnIndex("name")));
            listContactId.add(cursorSim.getString(cursorSim.getColumnIndex("_id")));
            listMobileNo.add(cursorSim.getString(cursorSim.getColumnIndex("number")));
        }
        Log.e("Contacts", "getDefaultAccountNameAndType: listName, " + listName);
        Log.e("Contacts", "getDefaultAccountNameAndType: listContactId, " + listContactId);
        Log.e("Contacts", "getDefaultAccountNameAndType: listMobileNo, " + listMobileNo);
    }


    public void getDefaultAccountNameAndType() {
        String accountType = "";
        String accountName = "";

        long rawContactId = 0;
        Uri rawContactUri = null;
        ContentProviderResult[] results = null;

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).build());

        try {
            results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ops.clear();
        }

        for (ContentProviderResult result : results) {
            rawContactUri = result.uri;
            rawContactId = ContentUris.parseId(rawContactUri);
        }

        Cursor c = getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI
                , new String[]{ContactsContract.RawContacts.ACCOUNT_TYPE, ContactsContract.RawContacts.ACCOUNT_NAME}
                , ContactsContract.RawContacts._ID + "=?"
                , new String[]{String.valueOf(rawContactId)}
                , null);

        if (c.moveToFirst()) {
            if (!c.isAfterLast()) {
                accountType = c.getString(c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
                accountName = c.getString(c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
            }
        }

        getContentResolver().delete(rawContactUri, null, null);

        c.close();
        c = null;


        Log.e("Contacts", "getDefaultAccountNameAndType: accountType, " + accountType);
        Log.e("Contacts", "getDefaultAccountNameAndType: accountName, " + accountName);
    }

    public void readContacts() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Log.i("ABC", "aaa\n" + id + "\n" + name + "\n");

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.e("ABC", "phone: " + phone);
                    }
                    pCur.close();


                    // get email and type

                    Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        // This would allow you get several email addresses
                        // if the email addresses were stored in an array
                        String email = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        String emailType = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        Log.e("ABC", "email and type - Email " + email + " Email Type : " + emailType);
                    }
                    emailCur.close();

                    // Get note.......
                    String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] noteWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                    Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                    if (noteCur.moveToFirst()) {
                        String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                        Log.e("ABC", "Note " + note);
                    }
                    noteCur.close();

                    //Get Postal Address.... Nhận địa chỉ bưu điện

                    String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] addrWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                    Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, null, null, null);
                    while (addrCur.moveToNext()) {
                        String poBox = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                        String street = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                        String city = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                        String state = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                        String postalCode = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                        String country = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                        String type = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

                    }
                    addrCur.close();

                    // Get Instant Messenger......... Nhận tin nhắn tức thời.

                    String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] imWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
                    Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, imWhere, imWhereParams, null);
                    if (imCur.moveToFirst()) {
                        String imName = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
                        String imType;
                        imType = imCur.getString(
                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
                        Log.e("ABC", "Instant Messenger - DATA: " + imName + " TYPE : " + imType);
                    }
                    imCur.close();

                    // Get Organizations......... Nhận tổ chức

                    String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] orgWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                    Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
                            null, orgWhere, orgWhereParams, null);
                    if (orgCur.moveToFirst()) {
                        String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                        String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                        Log.e("ABC", "Organizations - DATA " + orgName + " TITLE : " + title);
                    }
                    orgCur.close();
                }
            }
        }
    }

    private void readDeviceAccounts() {
        //Account[] accounts = manager.getAccountsByType("com.google");
        /*List<String> username = new LinkedList<String>();

        for (Account account : accounts) {
            username.add(account.name);
        }*/
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            //val contactAccount = ContactAccount()
            //contactAccount.id = tempDeviceAccount.size
            //contactAccount.accountType = account.type
            //contactAccount.accountName = account.name
            //setContactAccountDetails(contactAccount)
            //tempDeviceAccount.add(contactAccount)
            Log.e("HDT123", "account name : " + account.name + " account type : " + account.type);
        }
    }

    private void readDeviceAccount2() {
        String[] projections = {
                ContactsContract.RawContacts.ACCOUNT_NAME,
                ContactsContract.RawContacts.ACCOUNT_TYPE
        };
        List<String> names = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projections, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String accountName = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
            String accountType = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
            if (names.size() == 0) {
                names.add(accountName);
            } else {
                boolean check = true;
                for (int i = 0; i < names.size(); i++) {
                    if (names.get(i).equals(accountName)) {
                        check = false;
                        break;
                    }
                }
                if (check) {
                    names.add(accountName);
                }
            }
            Log.e("HDT123", "account : " + names);
            //Log.e("HDT123", "accountName : " + accountName + "  accountType : " + accountType);
        }
        assert cursor != null;
        cursor.close();

    }

    private void readAccountContacts() {
        String[] projections = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        Cursor cursorContact = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projections, null, null, null);
        while (cursorContact != null && cursorContact.moveToNext()) {
            String idContact = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts._ID));
            String nameContact = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Log.e("HDT123", "//////////////////////////////Contacts._ID: " + idContact + " ////////////DISPLAY_NAME: " + nameContact);
            String[] projection2 = {
                    ContactsContract.RawContacts._ID,
                    ContactsContract.RawContacts.CONTACT_ID,
                    ContactsContract.RawContacts.ACCOUNT_NAME,
                    ContactsContract.RawContacts.ACCOUNT_TYPE
            };
            //val stringZalo =
            String selection = ContactsContract.RawContacts.CONTACT_ID + " = " + idContact;
            //val selection = "${ContactsContract.RawContacts._ID} = $idContact";
            Cursor cursorAccount = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection2, selection, null, null, null);
            while (cursorAccount != null && cursorAccount.moveToNext()) {
                String idRawContract = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts._ID));
                String account = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
                String accountType = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
                //readDataXXX(idContact)
                Log.e("HDT123", "nameContact : " + nameContact + " -> accountName : " + account + "  accountTYpe :" + accountType);
                String[] projectionx = {
                        ContactsContract.Data.RAW_CONTACT_ID,
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.Data.DATA1,
                        ContactsContract.Data.DATA2,
                        ContactsContract.Data.DATA3,
                        ContactsContract.Data.DATA4,
                        ContactsContract.Data.DATA5
                };
                //val argx = arrayOf(ContactsContract.Data.CONTACT_ID,ContactsContract.Data.MIMETYPE,ContactsContract.Data.DATA1);
                //val selectionx = "${ContactsContract.Data.CONTACT_ID} = $idContact"
                String selectionx = ContactsContract.Data.RAW_CONTACT_ID + " = " + idRawContract;
                Cursor cursorData = getContentResolver().query(ContactsContract.Data.CONTENT_URI, projectionx, selectionx, null, null);
                while (cursorData != null && cursorData.moveToNext()) {
                    String a = cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.MIMETYPE));
                    String b = cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.DATA1));
                    String c = cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.DATA2));
                    String d = cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.DATA3));
                    String e = cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.DATA4));
                    String f = cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.DATA5));
                    Log.e("HDT123", "DATA1: " + b + "     DATA2: " + c + "     DATA3: " + d + "     DATA4: " + e + "     DATA5: " + f + "   MIMETYPE" + a);
                }
                assert cursorData != null;
                cursorData.close();

            }
            assert cursorAccount != null;
            cursorAccount.close();
        }
        assert cursorContact != null;
        cursorContact.close();
    }

    private void openExitAppDialog() {
        final BottomSheetDialog exitDialog = new BottomSheetDialog(this);
        exitDialog.setContentView(R.layout.exit_app_dialog);

        if (ContactConfig.getInstance().getConfig().getBoolean("config_on")) {
            Ads.initNativeGgFb((LinearLayout) exitDialog.findViewById(R.id.lnNative), this, false);
        }

        Button btnYes = exitDialog.findViewById(R.id.btnYes);
        Button btnCancel = exitDialog.findViewById(R.id.btnCancel);

        assert btnYes != null;
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                finish();
            }
        });
        assert btnCancel != null;
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });
        exitDialog.show();
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(this, "Thank you for your purchased!", Toast.LENGTH_SHORT).show();
        if (productId.equals(getString(R.string.ID_REMOVE_ADS))) {
            checkRemoveAds();
        } else if (productId.equals(getString(R.string.ID_SUBSCRIPTION))) {
            MySetting.setSubscription(this, true);
            MySetting.putRemoveAds(this, true);
        }
        Toast.makeText(this, "Thanks for your Purchased!", Toast.LENGTH_SHORT).show();
    }

    private void checkRemoveAds() {
        try {
            if (bp.isSubscribed(getString(R.string.ID_REMOVE_ADS))) {
                MySetting.putRemoveAds(this, true);
            } else {
                MySetting.putRemoveAds(this, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(this, "You have declined payment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingInitialized() {
        readyToPurchase = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bp != null) {
            bp.release();
        }
    }


    @Override
    public void onBackPressed() {
        if (ContactConfig.getInstance().getConfig().getBoolean("config_on")) {
            if (!showInterstitial()) {
                if (UnityAds.isInitialized() && UnityAds.isReady(getString(R.string.INTER_UNI)))
                    UnityAds.show(MainActivity.this, getString(R.string.INTER_UNI));
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openExitAppDialog();
            }
        }, 300);
    }

}
