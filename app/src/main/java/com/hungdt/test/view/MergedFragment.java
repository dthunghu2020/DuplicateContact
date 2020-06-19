package com.hungdt.test.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.test.R;
import com.hungdt.test.database.DBHelper;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MergedFragment extends Fragment {
    private RecyclerView rcvMerged;
    private ContactAdapter contactAdapter;
    private List<Contact> contacts = new ArrayList<>();
    public MergedFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_merged,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvMerged=view.findViewById(R.id.rcvMerged);

        contacts.addAll(DBHelper.getInstance(getContext()).getContactMergedF());

        Collections.sort(contacts);
        contactAdapter = new ContactAdapter(view.getContext(), contacts, KEY.MERGER);
        rcvMerged.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rcvMerged.setAdapter(contactAdapter);
    }
}
