package com.agaoglu.tez;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class tetkikler extends Fragment {

    private DatabaseReference tetkikDB;
    private DatabaseReference hastaDB;
    private List<tetkik> tetkiklerList;

    public tetkikler() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tetkikler, container, false);
        getActivity().setTitle("Tetkikler");

        tetkiklerList = new ArrayList<>();
        tetkikDB = FirebaseDatabase.getInstance().getReference("tetkikler");
        hastaDB = FirebaseDatabase.getInstance().getReference("hastalar");

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.tetkiklerlist);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshtetkik);
        final tetkikadapter adapter = new tetkikadapter(tetkiklerList, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        tetkikDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    tetkik tetkik = dataSnapshot.getValue(com.agaoglu.tez.tetkik.class);
                    adapter.insert(0,tetkik);
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
