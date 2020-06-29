package com.hungdt.test.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.test.R;
import com.hungdt.test.model.Account;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountHolder> {
    List<Account> accounts;
    LayoutInflater layoutInflater;

    public AccountAdapter(Context context, List<Account> accounts) {
        this.accounts = accounts;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.phone_adapter, parent, false);
        return new AccountHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, final int position) {
        holder.imgSMS.setVisibility(View.GONE);
        holder.txtPhone.setText(accounts.get(position).getAccountName());
        if(accounts.get(position).getAccountType().equals("vnd.sec.contact.phone")||
                accounts.get(position).getAccountType().equals("vnd.sec.contact.sim")||
                        accounts.get(position).getAccountType().equals("vnd.sec.contact.sim2")||
                accounts.get(position).getAccountType().equals("Device")){
            holder.imgCall.setImageResource(R.drawable.ic_id);
        }else if(  accounts.get(position).getAccountType().equals("com.google.android.apps.tachyon")){
            holder.imgCall.setImageResource(R.drawable.duo);
        }else if ( accounts.get(position).getAccountType().equals("com.zing.zalo")){
            holder.imgCall.setImageResource(R.drawable.zalo);
        }else if (accounts.get(position).getAccountType().equals("com.google")){
            holder.imgCall.setImageResource(R.drawable.google);
        }else {
            holder.imgCall.setImageResource(R.drawable.ic_code);
        }
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    class AccountHolder extends RecyclerView.ViewHolder {
        private TextView txtPhone;
        private ImageView imgCall, imgSMS;

        public AccountHolder(@NonNull View itemView) {
            super(itemView);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            imgCall = itemView.findViewById(R.id.imgCall);
            imgSMS = itemView.findViewById(R.id.imgSMS);
        }
    }
}
