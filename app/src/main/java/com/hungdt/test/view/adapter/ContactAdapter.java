package com.hungdt.test.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hungdt.test.R;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.DetailContactActivity;


import java.io.Serializable;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    List<Contact> contactList ;
    LayoutInflater layoutInflater;

    public ContactAdapter(Context context, List<Contact> contactList) {
        this.contactList = contactList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.contact_adapter, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, final int position) {


        Glide.with(layoutInflater.getContext())
                .load(contactList.get(position).getImage())
                .error(R.drawable.ic_code)
                .into(holder.imgContact);

        holder.txtContactName.setText(contactList.get(position).getName());

        holder.clItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(layoutInflater.getContext(), DetailContactActivity.class);
                intent.putExtra(KEY.ID,contactList.get(position).getId());
                layoutInflater.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        private ImageView imgContact;
        private TextView txtContactName;
        private ConstraintLayout clItem;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            imgContact = itemView.findViewById(R.id.imgContact);
            txtContactName = itemView.findViewById(R.id.txtContactName);
            clItem = itemView.findViewById(R.id.clItem);
        }
    }
}
