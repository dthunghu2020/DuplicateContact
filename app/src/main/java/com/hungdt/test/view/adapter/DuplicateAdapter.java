package com.hungdt.test.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hungdt.test.R;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.MergerDuplicateActivity;

import java.util.ArrayList;
import java.util.List;

public class DuplicateAdapter extends RecyclerView.Adapter<DuplicateAdapter.DuplicateHolder> {
    LayoutInflater layoutInflater;
    List<Contact> contacts;
    List<Integer> types;
    String type;

    public DuplicateAdapter(Context context, List<Contact> contacts, List<Integer> types, String type) {
        layoutInflater = LayoutInflater.from(context);
        this.contacts = contacts;
        this.types = types;
        this.type = type;
    }

    @NonNull
    @Override
    public DuplicateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.duplicate_adpater, parent, false);
        return new DuplicateHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DuplicateHolder holder, final int position) {
        for (int i = 0; i < contacts.size(); i++) {
            Log.e("111111", "onBindViewHolder: " + contacts.get(i).getIdContact() + contacts.get(i).getName());
        }
        Glide.with(layoutInflater.getContext())
                .load(contacts.get(position).getImage())
                .error(R.drawable.ic_code)
                .into(holder.imgContact);

        String name = null;
        String phone = null;
        String email = null;
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getType() == types.get(position)) {
                if (name == null) {
                    name = contacts.get(i).getName();
                    if (contacts.get(i).getPhones().size() == 1) {
                        phone = contacts.get(i).getPhones().get(0).getPhone();
                    }
                    if (contacts.get(i).getEmails().size() == 1) {
                        email = contacts.get(i).getEmails().get(0).getEmail();
                    }
                    holder.txtContactDup.setText(contacts.get(i).getName());
                } else {
                    holder.txtContactDup.append("\n" + contacts.get(i).getName());
                }
            }
        }
        switch (type) {
            case "email":
                for (int i = 0; i < contacts.size(); i++) {
                    if (contacts.get(i).getEmails().size() == 1) {
                        email = contacts.get(i).getEmails().get(0).getEmail();
                    }
                }
                holder.txtContactMerger.setText("Email: " + email);
                break;
            case "phone":
                for (int i = 0; i < contacts.size(); i++) {
                    if (contacts.get(i).getPhones().size() == 1) {
                        phone = contacts.get(i).getPhones().get(0).getPhone();
                    }
                }
                holder.txtContactMerger.setText("Phone: " + phone);
                break;
            case "contact":
            case "name":
                holder.txtContactMerger.setText("Name: " + name);
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(layoutInflater.getContext(), MergerDuplicateActivity.class);
                intent.putExtra(KEY.DUP, type);
                ArrayList<String> id = new ArrayList<>();
                for (int i = 0; i < contacts.size(); i++) {
                    if (contacts.get(i).getType() == types.get(position)) {
                        id.add(contacts.get(i).getIdContact());
                    }
                }
                intent.putStringArrayListExtra(KEY.LIST_ID, id);
                layoutInflater.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    class DuplicateHolder extends RecyclerView.ViewHolder {
        private ImageView imgContact;
        private TextView txtContactDup, txtContactMerger;


        public DuplicateHolder(@NonNull View itemView) {
            super(itemView);
            imgContact = itemView.findViewById(R.id.imgContact);
            txtContactDup = itemView.findViewById(R.id.txtContactDup);
            txtContactMerger = itemView.findViewById(R.id.txtContactMerger);

        }
    }
}
