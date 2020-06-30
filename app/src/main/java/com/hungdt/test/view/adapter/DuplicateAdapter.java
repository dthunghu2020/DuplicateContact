package com.hungdt.test.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.test.R;
import com.hungdt.test.model.ContactBum;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.MergerDuplicateActivity;

import java.util.ArrayList;
import java.util.List;

import static com.hungdt.test.view.MainActivity.contactList;

public class DuplicateAdapter extends RecyclerView.Adapter<DuplicateAdapter.DuplicateHolder> {
    LayoutInflater layoutInflater;
    List<ContactBum> contactBums;
    List<String> typeList;
    String type;


    public DuplicateAdapter(Context context, List<ContactBum> contactBums, List<String> typeList, String type) {
        layoutInflater = LayoutInflater.from(context);
        this.contactBums = contactBums;
        this.typeList = typeList;
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
        Log.e("123", "onBindViewHolder: "+typeList );
        List<String > names = new ArrayList<>();
        switch (type) {
            case "contact":
                for (int i = 0; i < contactBums.size(); i++) {
                    if (contactBums.get(i).getBum().equals(typeList.get(position))&&!names.contains(contactBums.get(i).getNameContact())) {
                        names.add(contactBums.get(i).getNameContact());
                        if (holder.txtContactDup.getText().equals("")) {
                            holder.txtContactDup.setText(contactBums.get(i).getNameContact());
                        } else {
                            holder.txtContactDup.append("\n" + contactBums.get(i).getNameContact());
                        }
                        holder.txtContactMerger.setText("Contact: " + contactBums.get(i).getName());
                    }
                }
                break;
            case "name":
                for (int i = 0; i < contactBums.size(); i++) {
                    if (contactBums.get(i).getBum().equals(typeList.get(position))&&!names.contains(contactBums.get(i).getNameContact())) {
                        names.add(contactBums.get(i).getNameContact());
                        if (holder.txtContactDup.getText().equals("")) {
                            holder.txtContactDup.setText(contactBums.get(i).getNameContact());
                        } else {
                            holder.txtContactDup.append("\n" + contactBums.get(i).getNameContact());
                        }
                        holder.txtContactMerger.setText("Name: " + contactBums.get(i).getName());
                    }
                }
                break;
            case "email":
                for (int i = 0; i < contactBums.size(); i++) {
                    if (contactBums.get(i).getBum().equals(typeList.get(position))&&!names.contains(contactBums.get(i).getNameContact())) {
                        names.add(contactBums.get(i).getNameContact());
                        if (holder.txtContactDup.getText().equals("")) {
                            holder.txtContactDup.setText(contactBums.get(i).getNameContact());
                        } else {
                            holder.txtContactDup.append("\n" + contactBums.get(i).getNameContact());
                        }
                        holder.txtContactMerger.setText("Email: " + contactBums.get(i).getName());
                    }
                }
                break;
            case "phone":
                for (int i = 0; i < contactBums.size(); i++) {
                    if (contactBums.get(i).getBum().equals(typeList.get(position))&&!names.contains(contactBums.get(i).getNameContact())) {
                        names.add(contactBums.get(i).getNameContact());
                        if (holder.txtContactDup.getText().equals("")) {
                            holder.txtContactDup.setText(contactBums.get(i).getNameContact());
                        } else {
                            holder.txtContactDup.append("\n" + contactBums.get(i).getNameContact());
                        }
                        holder.txtContactMerger.setText("Phone: " + contactBums.get(i).getName());
                    }
                }
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(layoutInflater.getContext(), MergerDuplicateActivity.class);
                intent.putExtra(KEY.DUP, type);
                ArrayList<String> id = new ArrayList<>();
                for (int i = 0; i < contactBums.size(); i++) {
                    if (contactBums.get(i).getBum().equals(typeList.get(position)) &&!id.contains(contactBums.get(i).getIdContact())) {
                        id.add(contactBums.get(i).getIdContact());
                    }
                }
                intent.putStringArrayListExtra(KEY.LIST_ID, id);
                layoutInflater.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return typeList.size();
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
