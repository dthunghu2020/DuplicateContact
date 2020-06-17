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
import com.hungdt.test.utils.KEY;

import java.util.ArrayList;
import java.util.List;

public class DeleteFragment extends Fragment {
    private List<Contact> contacts = new ArrayList<>();
    private LinearLayout llLastContact;
    private ConstraintLayout clNo3,clNo6,clNo12,clNever,clNoName,clNoPhone,clNoEmail;
    private TextView txtNo3,txtNo6,txtNo12,txtNever,txtNoName,txtNoPhone,txtNoEmail;
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

        txtNo3.setText(DBHelper.getInstance(getLayoutInflater().getContext()).getNumberContactNo3());
        txtNo6.setText(DBHelper.getInstance(getLayoutInflater().getContext()).getNumberContactNo6());
        txtNo12.setText(DBHelper.getInstance(getLayoutInflater().getContext()).getNumberContactNo12());
        txtNever.setText(DBHelper.getInstance(getLayoutInflater().getContext()).getNumberContactNever());
        txtNoName.setText(DBHelper.getInstance(getLayoutInflater().getContext()).getNumberContactNoName());
        txtNoPhone.setText(DBHelper.getInstance(getLayoutInflater().getContext()).getNumberContactNoPhone());
        txtNoEmail.setText(DBHelper.getInstance(getLayoutInflater().getContext()).getNumberContactNoEmail());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            llLastContact.setVisibility(View.GONE);
        }

        clNo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getLayoutInflater().getContext(), DeleteActivity.class);
                intent.putExtra(KEY.DELETE,"no3");
                startActivity(intent);
            }
        });
        clNo6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getLayoutInflater().getContext(), DeleteActivity.class);
                intent.putExtra(KEY.DELETE,"no6");
                startActivity(intent);
            }
        });
        clNo12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getLayoutInflater().getContext(), DeleteActivity.class);
                intent.putExtra(KEY.DELETE,"no12");
                startActivity(intent);
            }
        });
        clNever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getLayoutInflater().getContext(), DeleteActivity.class);
                intent.putExtra(KEY.DELETE,"never");
                startActivity(intent);
            }
        });
        clNoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getLayoutInflater().getContext(), DeleteActivity.class);
                intent.putExtra(KEY.DELETE,"noName");
                startActivity(intent);
            }
        });
        clNoPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getLayoutInflater().getContext(), DeleteActivity.class);
                intent.putExtra(KEY.DELETE,"noPhone");
                startActivity(intent);
            }
        });
        clNoEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getLayoutInflater().getContext(), DeleteActivity.class);
                intent.putExtra(KEY.DELETE,"noEmail");
                startActivity(intent);
            }
        });


    }

    private void initView(View view) {
        llLastContact = view.findViewById(R.id.llLastContact);
        clNo3 = view.findViewById(R.id.clNo3);
        clNo6 = view.findViewById(R.id.clNo6);
        clNo12 = view.findViewById(R.id.clNo12);
        clNever = view.findViewById(R.id.clNever);
        clNoName = view.findViewById(R.id.clNoName);
        clNoPhone = view.findViewById(R.id.clNoPhone);
        clNoEmail = view.findViewById(R.id.clNoEmail);
        txtNo3 = view.findViewById(R.id.txtNo3);
        txtNo6 = view.findViewById(R.id.txtNo6);
        txtNo12 = view.findViewById(R.id.txtNo12);
        txtNever = view.findViewById(R.id.txtNever);
        txtNoName = view.findViewById(R.id.txtNoName);
        txtNoPhone = view.findViewById(R.id.txtNoPhone);
        txtNoEmail = view.findViewById(R.id.txtNoEmail);
    }


}
