package com.hungdt.test.view;

import android.content.Intent;
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

import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.KEY;

import java.util.ArrayList;
import java.util.List;

public class DeleteFragment extends Fragment {
    private List<Contact> contacts = new ArrayList<>();
    private ConstraintLayout clNoName,clNoPhone,clNoEmail;
    private TextView txtNoName,txtNoPhone,txtNoEmail;
    private LinearLayout llBanner;
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
        txtNoName.setText(DBHelper.getInstance(getActivity()).getNumberContactNoName());
        txtNoPhone.setText(DBHelper.getInstance(getActivity()).getNumberContactNoPhone());
        txtNoEmail.setText(DBHelper.getInstance(getActivity()).getNumberContactNoEmail());

        clNoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteActivity.class);
                intent.putExtra(KEY.DELETE,"noName");
                startActivity(intent);
            }
        });
        clNoPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteActivity.class);
                intent.putExtra(KEY.DELETE,"noPhone");
                startActivity(intent);
            }
        });
        clNoEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteActivity.class);
                intent.putExtra(KEY.DELETE,"noEmail");
                startActivity(intent);
            }
        });


    }

    private void initView(View view) {
        llBanner = view.findViewById(R.id.llBanner);
        clNoName = view.findViewById(R.id.clNoName);
        clNoPhone = view.findViewById(R.id.clNoPhone);
        clNoEmail = view.findViewById(R.id.clNoEmail);
        txtNoName = view.findViewById(R.id.txtNoName);
        txtNoPhone = view.findViewById(R.id.txtNoPhone);
        txtNoEmail = view.findViewById(R.id.txtNoEmail);
    }


}
