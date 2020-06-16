package com.hungdt.test.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.hungdt.test.R;
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.Helper;
import com.hungdt.test.utils.MySetting;

public class VipFragment extends Fragment implements BillingProcessor.IBillingHandler {
    private BillingProcessor billingProcessor;
    private boolean readyToPurchase = false;
    private Button btnVip;
    TextView txtMoney;

    public VipFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vip,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Helper.setColorStatusBar(getActivity(), R.color.status_bar);
        Ads.initBanner(((LinearLayout) view.findViewById(R.id.llBanner)), getActivity(), true);

        billingProcessor = BillingProcessor.newBillingProcessor(getLayoutInflater().getContext().getApplicationContext(), getString(R.string.BASE64),this); // doesn't bind
        billingProcessor.initialize();

        btnVip = view.findViewById(R.id.btnVip);
        txtMoney = view.findViewById(R.id.txtMoney);

        btnVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyToPurchase) {
                    billingProcessor.subscribe(getActivity(), getString(R.string.ID_SUBSCRIPTION));
                } else {
                    Toast.makeText(getLayoutInflater().getContext().getApplicationContext(), "Unable to initiate purchase", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if(MySetting.isSubscription(getLayoutInflater().getContext())){
            btnVip.setVisibility(View.GONE);
            txtMoney.setVisibility(View.GONE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (billingProcessor != null) {
            billingProcessor.release();
        }
        super.onDestroy();
    }

    @Override
    public void onProductPurchased(@Nullable  String productId, TransactionDetails details) {
        try {
            Toast.makeText(getLayoutInflater().getContext(), "Thanks for your Purchased!", Toast.LENGTH_SHORT).show();
            MySetting.setSubscription(getLayoutInflater().getContext(), true);
            MySetting.putRemoveAds(getLayoutInflater().getContext(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(getContext(), "Unable to process billing", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBillingInitialized() {
        readyToPurchase = true;
        try {
            txtMoney.setText(billingProcessor.getSubscriptionListingDetails(getString(R.string.ID_SUBSCRIPTION)).priceText + "/Month)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
