package com.hungdt.test.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
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
import com.hungdt.test.view.DetailContactActivity;

import java.util.List;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.SelectHolder> {
    private LayoutInflater layoutInflater;
    private List<Contact> contacts;
    private String type;

    public SelectAdapter(Context context, List<Contact> contacts, String type) {
        layoutInflater = LayoutInflater.from(context);
        this.contacts = contacts;
        this.type = type;
    }

    @NonNull
    @Override
    public SelectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.select_adapter,parent,false);
        return new SelectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectHolder holder, final int position) {

        Glide.with(layoutInflater.getContext())
                .load(contacts.get(position).getImage())
                .error(R.drawable.ic_code)
                .into(holder.imgContact);

        if(contacts.get(position).isTicked()){
            holder.cbSelect.setChecked(true);
        }else {
            holder.cbSelect.setChecked(false);
        }

        holder.txtContactName.setText(contacts.get(position).getName());
        holder.cbSelect.setOnClickListener(new View.OnClickListener() {
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
                if(type.equals(KEY.DELETE)){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
                            String.valueOf(contacts.get(position).getIdContact()));
                    intent.setData(uri);
                    layoutInflater.getContext().startActivity(intent);
                }
               if(type.equals(KEY.BACKUP)){
                   Intent intent = new Intent(layoutInflater.getContext(), DetailContactActivity.class);
                   intent.putExtra(KEY.ID, contacts.get(position).getIdContact());
                   Log.e("222", "onClick adpater : "+  contacts.get(position).getIdContact());
                   intent.putExtra(KEY.TYPE, KEY.BACKUP);
                   layoutInflater.getContext().startActivity(intent);
               }
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class SelectHolder extends RecyclerView.ViewHolder {
        private ImageView imgContact;
        private TextView txtContactName;
        private CheckBox cbSelect;
        public SelectHolder(@NonNull View itemView) {
            super(itemView);
            imgContact = itemView.findViewById(R.id.imgContact);
            txtContactName = itemView.findViewById(R.id.txtContactName);
            cbSelect = itemView.findViewById(R.id.cbSelect);
        }
    }
}
