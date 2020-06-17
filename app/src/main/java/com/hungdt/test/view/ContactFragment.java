package com.hungdt.test.view;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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
import com.hungdt.test.model.Account;
import com.hungdt.test.model.Contact;
import com.hungdt.test.utils.KEY;
import com.hungdt.test.view.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ContactFragment extends Fragment {

    private List<Contact> contactList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private RecyclerView rcvContactView;
    private String idContact = null;
    private String name;
    private String image = null;
    private String lastCT = null;
    private List<Account> accounts = new ArrayList<>();
    private List<String> phones = new ArrayList<>();
    private List<String> emails = new ArrayList<>();

    private Calendar calendar;

    public ContactFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendar = Calendar.getInstance();
        Log.i("TAG", "onViewCreated: " + calendar.getTimeInMillis());

    /*
    * // Câu lệnh tìm kiếm
        @Query("SELECT * FROM phone WHERE Ten like :timKiem")
        List<Phone> getListTimKiem(String timKiem);
        *
        * imgTimKiem.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    String srearch = String.valueOf(edtTimKiem.getText()).trim().replaceAll("\\s+", " ");
                    String timKiem = "%" + srearch + "%";
                    listphones = AppDatabasePhone.getInstance().getIDatabase().getPhoneDao().getListTimKiem(timKiem);
                    phoneAdaper.notifyDataSetChanged();

                    checkDataListPhone(listphones.size());
                    loadDataTrangChinh(view);

                    txtTraVeTimKiem.setVisibility(View.VISIBLE);
                    txtTieuDe.setVisibility(View.INVISIBLE);

                    txtTraVeTimKiem.setText("Kết quả tìm kiếm '" + edtTimKiem.getText() + "' là : ");
                }
            });
        * */


        rcvContactView = view.findViewById(R.id.rcvListContact);

        contactList = DBHelper.getInstance(view.getContext()).getAllContact();
        if (contactList.size() == 0) {
            readAccountContacts();
            contactList = DBHelper.getInstance(view.getContext()).getAllContact();
        }

        Collections.sort(contactList);
        contactAdapter = new ContactAdapter(view.getContext(), contactList);
        rcvContactView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rcvContactView.setAdapter(contactAdapter);

    }

    private void readAccountContacts() {

        String[] projections = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.LAST_TIME_CONTACTED
        };
        Cursor cursorContact = getLayoutInflater().getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projections, null, null, null);
        while (cursorContact != null && cursorContact.moveToNext()) {

            idContact = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts._ID));
            name = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            image = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
            lastCT = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.LAST_TIME_CONTACTED));
            String[] projection2 = {
                    ContactsContract.RawContacts._ID,
                    ContactsContract.RawContacts.CONTACT_ID,
                    ContactsContract.RawContacts.ACCOUNT_NAME,
                    ContactsContract.RawContacts.ACCOUNT_TYPE
            };
            //val stringZalo =
            String selection = ContactsContract.RawContacts.CONTACT_ID + " = " + idContact;
            //val selection = "${ContactsContract.RawContacts._ID} = $idContact";
            Cursor cursorAccount = getLayoutInflater().getContext().getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection2, selection, null, null, null);
            while (cursorAccount != null && cursorAccount.moveToNext()) {
                String idRawContract = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts._ID));
                String account = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
                String accountType = cursorAccount.getString(cursorAccount.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
                //readDataXXX(idContact)
                accounts.add(new Account(account, accountType));
                String[] projectionx = {
                        ContactsContract.Data.RAW_CONTACT_ID,
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.Data.DATA1,
                        ContactsContract.Data.DATA2,
                };
                String selectionx = ContactsContract.Data.RAW_CONTACT_ID + " = " + idRawContract;
                Cursor cursorData = getLayoutInflater().getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, projectionx, selectionx, null, null);
                while (cursorData != null && cursorData.moveToNext()) {
                    if (accountType.equals("vnd.sec.contact.phone") || accountType.equals("vnd.sec.contact.sim") || accountType.equals("vnd.sec.contact.sim2")) {
                        String data1 = cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.DATA1));
                        String mineType = cursorData.getString(cursorData.getColumnIndex(ContactsContract.Data.MIMETYPE));
                        if (mineType.equals("vnd.android.cursor.item/phone_v2")) {
                            phones.add(data1);
                        } else if (mineType.equals("vnd.android.cursor.item/email_v2")) {
                            emails.add(data1);
                        }
                    }

                }
                assert cursorData != null;
                cursorData.close();

            }
            assert cursorAccount != null;
            cursorAccount.close();
            boolean noPhone = false;
            boolean noName = true;
            boolean noEmail = false;
            if (phones.isEmpty()) {
                noPhone = true;
                Log.i("TAG", "readAccountContacts: "+name);
            }
            if (!name.equals("")) {
                noName =false;
            }
            if (emails.isEmpty()) {
                noEmail = true;
            }

            contactList.add(new Contact("0", idContact, name, image, lastCT, KEY.FALSE, phones, accounts, emails));
            if (image != null) {
                DBHelper.getInstance(getLayoutInflater().getContext()).addContact(idContact, name, image, lastCT, KEY.FALSE,KEY.TRUE,KEY.TRUE,KEY.TRUE,KEY.TRUE,String.valueOf(noName),String.valueOf(noPhone),String.valueOf(noEmail));
            } else {
                DBHelper.getInstance(getLayoutInflater().getContext()).addContact(idContact, name, "image", lastCT,KEY.FALSE,KEY.TRUE,KEY.TRUE,KEY.TRUE,KEY.TRUE,String.valueOf(noName),String.valueOf(noPhone),String.valueOf(noEmail));
            }
            String id = DBHelper.getInstance(getLayoutInflater().getContext()).getLastContactID();
            if (!noPhone) {
                for (int i = 0; i < phones.size(); i++) {
                    DBHelper.getInstance(getLayoutInflater().getContext()).addPhone(id, phones.get(i));
                }
            }
            if (!accounts.isEmpty()) {
                for (int i = 0; i < accounts.size(); i++) {
                    DBHelper.getInstance(getLayoutInflater().getContext()).addAccount(id, accounts.get(i).getAccountName(), accounts.get(i).getAccountType());
                }
            }
            if (!noEmail) {
                for (int i = 0; i < emails.size(); i++) {
                    DBHelper.getInstance(getLayoutInflater().getContext()).addEmail(id, emails.get(i));
                }
            }
            image = null;
            phones.clear();
            accounts.clear();
            emails.clear();


        }
        assert cursorContact != null;
        cursorContact.close();
    }


}
