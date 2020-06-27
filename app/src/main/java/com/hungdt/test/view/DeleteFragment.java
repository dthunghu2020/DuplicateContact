package com.hungdt.test.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
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
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.KEY;
import com.unity3d.ads.UnityAds;

import java.util.ArrayList;
import java.util.List;

import static com.hungdt.test.view.MainActivity.showInterstitial;

public class DeleteFragment extends Fragment {
    private ConstraintLayout clNoName,clNoPhone,clNoEmail;
    private TextView txtNoName,txtNoPhone,txtNoEmail;
    public static final String ACTION_UPDATE_DELETE_FRAGMENT= "Update Delete Fragment";
    public DeleteFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delete,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        Ads.initBanner(((LinearLayout) view.findViewById(R.id.llBanner)), getActivity(), true);

        clNoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteActivity.class);
                intent.putExtra(KEY.DELETE,"noName");
                startActivity(intent);
                showInter();
            }
        });
        clNoPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteActivity.class);
                intent.putExtra(KEY.DELETE,"noPhone");
                startActivity(intent);
                showInter();
            }
        });
        clNoEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteActivity.class);
                intent.putExtra(KEY.DELETE,"noEmail");
                startActivity(intent);
                showInter();
            }
        });

        IntentFilter intentFilter = new IntentFilter(ACTION_UPDATE_DELETE_FRAGMENT);
        getLayoutInflater().getContext().registerReceiver(broadCastUpdate,intentFilter);
    }
    private BroadcastReceiver broadCastUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            txtNoName.setText(DBHelper.getInstance(getActivity()).getNumberContactNoName());
            txtNoPhone.setText(DBHelper.getInstance(getActivity()).getNumberContactNoPhone());
            txtNoEmail.setText(DBHelper.getInstance(getActivity()).getNumberContactNoEmail());
        }
    };

    private void initView(View view) {
        clNoName = view.findViewById(R.id.clNoName);
        clNoPhone = view.findViewById(R.id.clNoPhone);
        clNoEmail = view.findViewById(R.id.clNoEmail);
        txtNoName = view.findViewById(R.id.txtNoName);
        txtNoPhone = view.findViewById(R.id.txtNoPhone);
        txtNoEmail = view.findViewById(R.id.txtNoEmail);
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
        getActivity().unregisterReceiver(broadCastUpdate);
    }
}
