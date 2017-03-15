package com.agaoglu.tez;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class hastaKaydet extends Fragment {


    public hastaKaydet() {
        // Required empty public constructor
    }

    private DatabaseReference hastaDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hasta_kaydet, container, false);
        getActivity().setTitle("Hasta Kaydet");

        //Hasta bilgilerini Firebase e yazalım sonra liste yapıp okuyalım.

        hastaDB = FirebaseDatabase.getInstance().getReference("hastalar");


        final EditText hastaismi = (EditText) view.findViewById(R.id.isimtxt);
        final EditText hastadogtar = (EditText) view.findViewById(R.id.dogtartxt);
        final EditText hastaadres = (EditText) view.findViewById(R.id.adresttxt);
        final EditText hastatelefon = (EditText) view.findViewById(R.id.telefontxt);
        Button hastaKaydet = (Button) view.findViewById(R.id.hastKaydet);

        hastaKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hastaismi.getText().toString().length() == 0){
                    hastaismi.setError("Hasta İsmini Girmelisiniz.");
                }
                else{
                    HastaKaydet(hastaismi.getText().toString(),hastadogtar.getText().toString(),hastaadres.getText().toString(),hastatelefon.getText().toString());
                }
            }
        });
        return view;
    }

    private void HastaKaydet(String isim, String dogtar, final String adres, String telefon){
        final hasta hasta = new hasta();
        hasta.setIsim(isim);
        hasta.setDogtar(dogtar);
        hasta.setAdres(adres);
        hasta.setTelefon(telefon);
        //Bir primary key alıyoruz
        String hastaid = hastaDB.push().getKey();
        //Primary key le birlikte hasta class ında tuttuğum datayı internete basıyoruz
        hastaDB.child(hastaid).setValue(hasta);

        //Data doğru basıldı mı kontrol ediyoruz
        //Dikkat edilmesi gereken husus child parametresi vermezsen tüm databasi getiriyor
        hastaDB.child(hastaid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hasta kayit = dataSnapshot.getValue(hasta.class);
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.form),kayit.isim + " isimli hasta kaydedildi", Snackbar.LENGTH_LONG);
                snackbar.show();
                clearForm((ViewGroup) getActivity().findViewById(R.id.form));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("hata","sebebini bilmediğimiz bir hata oluştu");
                Log.e("hata",databaseError.toString());
            }
        });
    }

    private void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }

}
