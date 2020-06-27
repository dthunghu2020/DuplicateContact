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
import com.hungdt.test.model.Duplicate;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.MergerDuplicateActivity;

import java.util.ArrayList;
import java.util.List;

import static com.hungdt.test.view.MainActivity.contactList;

public class DuplicateAdapter extends RecyclerView.Adapter<DuplicateAdapter.DuplicateHolder> {
    LayoutInflater layoutInflater;
    List<Duplicate> duplicates;
    List<Integer> types;
    String type;


    public DuplicateAdapter(Context context, List<Duplicate> duplicates, List<Integer> types, String type) {
        layoutInflater = LayoutInflater.from(context);
        this.duplicates = duplicates;
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



        boolean setName = false;
        switch (type) {
            case "email":
                for (int i = 0; i < duplicates.size(); i++) {
                    if(duplicates.get(i).getTypeMer()==types.get(position)){
                        for(int j = 0;j<contactList.size();j++){
                            if(duplicates.get(i).getContactID().equals(contactList.get(j).getIdContact())){
                                holder.txtContactDup.append(contactList.get(j).getName());
                                if(!setName){
                                    holder.txtContactMerger.setText("Email: " + duplicates.get(i).getName());
                                    setName= true;
                                }
                            }
                        }
                    }
                }
                break;
            case "phone":
                for (int i = 0; i < duplicates.size(); i++) {
                    if(duplicates.get(i).getTypeMer()==types.get(position)){
                        for(int j = 0;j<contactList.size();j++){
                            if(duplicates.get(i).getContactID().equals(contactList.get(j).getIdContact())){
                                holder.txtContactDup.append(contactList.get(j).getName());
                                if(!setName){
                                    holder.txtContactMerger.setText("Phone: " + duplicates.get(i).getName());
                                    setName= true;
                                }
                            }
                        }
                    }
                }
               /* for (int i = 0; i < contacts.size(); i++) {
                    if (contacts.get(i).getPhones().size() == 1) {
                        phone = contacts.get(i).getPhones().get(0).getPhone();
                    }
                }
                holder.txtContactMerger.setText("Phone: " + phone);*/
                break;
            case "contact":
            case "name":
                for (int i = 0; i < duplicates.size(); i++) {
                    if(duplicates.get(i).getTypeMer()==types.get(position)){
                        for(int j = 0;j<contactList.size();j++){
                            if(duplicates.get(i).getContactID().equals(contactList.get(j).getIdContact())){
                                holder.txtContactDup.append(contactList.get(j).getName());
                                if(!setName){
                                    holder.txtContactMerger.setText("Name: " + duplicates.get(i).getName());
                                    setName= true;
                                }
                            }
                        }
                    }
                }
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(layoutInflater.getContext(), MergerDuplicateActivity.class);
                intent.putExtra(KEY.DUP, type);
                ArrayList<String> id = new ArrayList<>();
                for (int i = 0; i < contacts.size(); i++) {
                    if (contacts.get(i).getType() == types.get(position)) {
                        id.add(contacts.get(i).getIdContact());
                    }
                }
                intent.putStringArrayListExtra(KEY.LIST_ID, id);
                layoutInflater.getContext().startActivity(intent);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    class DuplicateHolder extends RecyclerView.ViewHolder {
        private TextView txtContactDup, txtContactMerger;


        public DuplicateHolder(@NonNull View itemView) {
            super(itemView);
            txtContactDup = itemView.findViewById(R.id.txtContactDup);
            txtContactMerger = itemView.findViewById(R.id.txtContactMerger);

        }
    }
}
