package com.hungdt.test.view.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.test.R;
import com.hungdt.test.model.Phone;

import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.PhoneHolder> {
    List<Phone> phones;
    LayoutInflater layoutInflater;

    public PhoneAdapter(Context context, List<Phone> phones) {
        this.phones = phones;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PhoneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.phone_adapter, parent, false);
        return new PhoneHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneHolder holder, final int position) {
        holder.txtPhone.setText(phones.get(position).getPhone());
        holder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_CALL);
                intentCall.setData(Uri.parse("tel:" + phones.get(position).getPhone()));
                if (ActivityCompat.checkSelfPermission(layoutInflater.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    layoutInflater.getContext().startActivity(intentCall);
                } else {
                    Toast.makeText(layoutInflater.getContext(), "Please Grant Permission!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.imgSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intentSMS = new Intent(Intent.ACTION_SENDTO);
                intentSMS.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentSMS.setData(Uri.parse("smsto:" + phones.get(position).getPhone())); // This ensures only SMS apps respond
                layoutInflater.getContext().startActivity(intentSMS);
            }
        });
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    class PhoneHolder extends RecyclerView.ViewHolder{
        private TextView txtPhone;
        private ImageView imgCall,imgSMS;
        public PhoneHolder(@NonNull View itemView) {
            super(itemView);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            imgCall = itemView.findViewById(R.id.imgCall);
            imgSMS = itemView.findViewById(R.id.imgSMS);
        }
    }
}
