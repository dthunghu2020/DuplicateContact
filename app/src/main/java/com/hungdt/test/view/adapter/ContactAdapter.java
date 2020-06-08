package com.hungdt.test.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.test.R;
import com.hungdt.test.model.Contact;
import com.hungdt.test.view.DetailContactActivity;

import java.io.IOException;
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

        if(contactList.get(position).getImage()!=null){
            holder.imgContact.setImageBitmap(getPhotoFromURI(contactList.get(position).getImage()));
        }else {
            holder.imgContact.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.txtContactName.setText(contactList.get(position).getName());
        Log.e("123123", "onBindViewHolder: "+contactList.size() );

        holder.clItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(layoutInflater.getContext(), DetailContactActivity.class);
                intent.putExtra("name",contactList.get(position).getName());
                intent.putExtra("image",contactList.get(position).getImage());
                intent.putExtra("phone",contactList.get(position).getPhone());
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
    private Bitmap getPhotoFromURI(String photoUri){
        if(photoUri !=null){
            try {
                return MediaStore.Images.Media.getBitmap(layoutInflater.getContext().getContentResolver(), Uri.parse(photoUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
