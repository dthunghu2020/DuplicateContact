package com.hungdt.test.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
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

import java.util.List;

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.DeleteHolder> {
    LayoutInflater layoutInflater;
    List<Contact> contacts;

    public DeleteAdapter(Context context, List<Contact> contacts) {
        layoutInflater = LayoutInflater.from(context);
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public DeleteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.delete_adapter,parent,false);
        return new DeleteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteHolder holder, final int position) {

        Glide.with(layoutInflater.getContext())
                .load(contacts.get(position).getImage())
                .error(R.drawable.ic_id)
                .into(holder.imgContact);

        if(contacts.get(position).isTicked()){
            holder.cbDelete.setChecked(true);
        }else {
            holder.cbDelete.setChecked(false);
        }

        holder.txtContactName.setText(contacts.get(position).getName());
        holder.cbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contacts.get(position).isTicked()){
                    contacts.get(position).setTicked(false);
                }else {
                    contacts.get(position).setTicked(true);
                }
            }
        });
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

    class DeleteHolder extends RecyclerView.ViewHolder {
        private ImageView imgContact;
        private TextView txtContactName;
        private CheckBox cbDelete;
        public DeleteHolder(@NonNull View itemView) {
            super(itemView);
            imgContact = itemView.findViewById(R.id.imgContact);
            txtContactName = itemView.findViewById(R.id.txtContactName);
            cbDelete = itemView.findViewById(R.id.cbDelete);
        }
    }
}
