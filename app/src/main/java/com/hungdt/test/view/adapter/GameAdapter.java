package com.hungdt.test.view.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hungdt.test.R;
import com.hungdt.test.model.GameItem;
import com.hungdt.test.utils.Helper;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.RecyclerViewHolder> {
    private Activity context;
    private ArrayList<GameItem> listGameItem;
    private boolean isSmall;

    public GameAdapter(ArrayList<GameItem> list, Activity context, boolean isSmall) {
        this.listGameItem = list;
        this.context = context;
        this.isSmall = isSmall;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (!isSmall)view = inflater.inflate(R.layout.item_game, parent, false);
        else view = inflater.inflate(R.layout.item_game, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        final GameItem item = listGameItem.get(position);
        try {
            holder.tvTitle.setText(item.getName());
            holder.tvInstall.setText(item.getStateInstall());
            try {
                Glide.with(context)
                        .load(item.getPathImage())
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_moregame_default)
                                .error(R.drawable.ic_moregame_default))
                        .into(holder.imgIcon);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.isLive()) {
                        try{
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(item.getName()+" (ad)");
                            builder.setMessage("Do you want to install this app?");
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Helper.callPlayStore(context, item.getPackageName());
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context, item.getStateInstall(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listGameItem.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private CardView cvItem;
        private ImageView imgIcon;
        private TextView tvTitle;
        private TextView tvInstall;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            cvItem = itemView.findViewById(R.id.cvItemMoreGame);
            imgIcon = itemView.findViewById(R.id.imgItemMoregame);
            tvTitle = itemView.findViewById(R.id.tvTitleItemMoreGame);
            tvInstall = itemView.findViewById(R.id.tvInstallItemMoregame);

        }
    }
}
