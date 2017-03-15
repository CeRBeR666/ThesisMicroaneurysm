package com.agaoglu.tez;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
                    kayitlar.add(hasta);
                    Log.e("test",kayitlar.toString());
                    recyclerView.scrollToPosition(kayitlar.size() - 1);
                    adapter.notifyItemInserted(kayitlar.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

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




}
