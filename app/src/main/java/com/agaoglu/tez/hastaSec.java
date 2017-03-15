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



    public hastaSec() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hasta_sec, container, false);


        List<hasta> kayitlar = tum_katilar();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.hastaseclist);
        hastasecadapter adapter = new hastasecadapter(kayitlar, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private List<hasta> tum_katilar() {
        List<hasta> data = new ArrayList<>();
        data.add(new hasta("Testinggg","","",""));
        data.add(new hasta("Enını amı","","",""));
        data.add(new hasta("Ebenin amı","","",""));
        return data;
    }
}
