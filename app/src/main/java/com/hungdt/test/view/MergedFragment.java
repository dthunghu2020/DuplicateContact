package com.hungdt.test.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.Ads;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MergedFragment extends Fragment {
    private RecyclerView rcvMerged;
    private ContactAdapter contactAdapter;
    private List<Contact> contacts = new ArrayList<>();
    public static final String ACTION_RELOAD_FRAGMENT_MERGED="reloadMergedFragment";

    public MergedFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_merged, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvMerged = view.findViewById(R.id.rcvMerged);
        Ads.initBanner(((LinearLayout) view.findViewById(R.id.llBanner)), getActivity(), true);
        /*contacts.addAll(DBHelper.getInstance(getActivity()).getContactMergedF());
        IntentFilter intentFilter = new IntentFilter(ACTION_RELOAD_FRAGMENT_MERGED);

        getActivity().registerReceiver(reloadFragmentMerged,intentFilter);
        Collections.sort(contacts);
        contactAdapter = new ContactAdapter(view.getContext(), contacts, KEY.MERGER);
        rcvMerged.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rcvMerged.setAdapter(contactAdapter);*/
    }

    private BroadcastReceiver reloadFragmentMerged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            contacts.addAll(DBHelper.getInstance(getActivity()).getContactMergedF());
            Collections.sort(contacts);
            contactAdapter.notifyDataSetChanged();
        }
    };
}
