package com.hungdt.test.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hungdt.test.R;
import com.hungdt.test.model.Contact;

import java.util.List;

public class DuplicateContactAdapter extends RecyclerView.Adapter<DuplicateContactAdapter.DupContactAdapter> {
    LayoutInflater layoutInflater;
    List<Contact> contacts;

    public DuplicateContactAdapter(Context context, List<Contact> contacts) {
        layoutInflater = LayoutInflater.from(context);
        this.contacts = contacts;
    }


    @NonNull
    @Override
    public DupContactAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.duplicate_contact_adpater, parent, false);
        return new DupContactAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DupContactAdapter holder, final int position) {
        Glide.with(layoutInflater.getContext())
                .load(contacts.get(position).getImage())
                .error(R.drawable.ic_code)
                .into(holder.imgContact);
        holder.txtContactName.setText(contacts.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
                        String.valueOf(contacts.get(position).getIdContact()));
                intent.setData(uri);
                layoutInflater.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class DupContactAdapter extends RecyclerView.ViewHolder {
        private ImageView imgContact;
        private TextView txtContactName;
        public DupContactAdapter(@NonNull View itemView) {
            super(itemView);
            imgContact = itemView.findViewById(R.id.imgContact);
            txtContactName = itemView.findViewById(R.id.txtContactName);
        }
    }
}
