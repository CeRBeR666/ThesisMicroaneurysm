package com.agaoglu.tez;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class hastaSec extends Fragment {
    private DatabaseReference hastaDB;
    private List<hasta> kayitlar;


    public hastaSec() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hasta_sec, container, false);
        getActivity().setTitle("Hasta Seç");
        setHasOptionsMenu(true);

        kayitlar = new ArrayList<>();
        hastaDB = FirebaseDatabase.getInstance().getReference("hastalar");

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.hastaseclist);
        final hastasecadapter adapter = new hastasecadapter(kayitlar, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        hastaDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    hasta hasta = dataSnapshot.getValue(com.agaoglu.tez.hasta.class);
                    adapter.insert(0,hasta);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                hasta hasta = dataSnapshot.getValue(com.agaoglu.tez.hasta.class);
                adapter.remove(hasta);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.action,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        final Fragment hastakaydet = new hastaKaydet();

        switch (item.getItemId()){
            case R.id.optionhastaekle:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content,hastakaydet).commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
