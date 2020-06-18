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

import java.util.List;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailHolder> {
    List<String> emails;
    LayoutInflater layoutInflater;

    public EmailAdapter(Context context, List<String> emails) {
        this.emails = emails;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public EmailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.phone_adapter, parent, false);
        return new EmailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailHolder holder, final int position) {
        holder.imgSMS.setVisibility(View.GONE);
        holder.txtPhone.setText(emails.get(position));
        holder.imgCall.setImageResource(R.drawable.ic_gmail);
        holder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+emails.get(position))); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, "addresses");
                intent.putExtra(Intent.EXTRA_SUBJECT, "mail");
                if (intent.resolveActivity(layoutInflater.getContext().getPackageManager()) != null) {
                    layoutInflater.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }

    class EmailHolder extends RecyclerView.ViewHolder {
        private TextView txtPhone;
        private ImageView imgCall,imgSMS;
        public EmailHolder(@NonNull View itemView) {
            super(itemView);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            imgCall = itemView.findViewById(R.id.imgCall);
            imgSMS = itemView.findViewById(R.id.imgSMS);
        }
    }
}
