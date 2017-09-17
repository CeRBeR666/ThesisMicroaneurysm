package com.agaoglu.tez;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class hastaKaydet extends Fragment {


    private DatabaseReference hastaDB;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private String hastaID;
    private String mCurrentPhotoPath;

    public hastaKaydet() {
        // Required empty public constructor
    }

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
        final Spinner cinsiyet = (Spinner) view.findViewById(R.id.cinsiyet);
        final Button hastaKaydet = (Button) view.findViewById(R.id.hastKaydet);
        Button fotocek = (Button) view.findViewById(R.id.hastFoto);

        hastaKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hastaismi.getText().toString().length() == 0){
                    hastaismi.setError("Hasta İsmini Girmelisiniz.");
                }
                else{
                    HastaKaydet(hastaismi.getText().toString(),hastadogtar.getText().toString(),hastaadres.getText().toString(),hastatelefon.getText().toString(),cinsiyet.getItemAtPosition(cinsiyet.getSelectedItemPosition()).toString());
                }
            }
        });

        fotocek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hastaismi.getText().toString().length() == 0){
                    hastaismi.setError("Hasta İsmini Girmelisiniz.");
                }
                else{
                    HastaKaydet(hastaismi.getText().toString(),hastadogtar.getText().toString(),hastaadres.getText().toString(),hastatelefon.getText().toString(),cinsiyet.getItemAtPosition(cinsiyet.getSelectedItemPosition()).toString());
                    selectImage();
                }
            }
        });
        return view;
    }

    private void selectImage() {
        final CharSequence[] items = { "Fotoğraf Çek", "Hafızadan Seç",
                "İptal" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Göz resmi ekle");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Util.checkPermission(getActivity());

                if (items[item].equals("Fotoğraf Çek")) {
                    userChoosenTask ="Fotoğraf Çek";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Hafızadan Seç")) {
                    userChoosenTask ="Hafızadan Seç";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("İptal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void HastaKaydet(String isim, String dogtar, final String adres, String telefon, String cinsiyet){
        final hasta hasta = new hasta();
        hasta.setIsim(isim);
        hasta.setDogtar(dogtar);
        hasta.setAdres(adres);
        hasta.setTelefon(telefon);
        if (!cinsiyet.toString().equals("Seçiniz")){
            hasta.setCinsiyet(cinsiyet);
        }else{
            hasta.setCinsiyet("");
        }

        //Bir primary key alıyoruz
        hastaID = hastaDB.push().getKey();
        //Primary key le birlikte hasta class ında tuttuğum datayı internete basıyoruz
        hastaDB.child(hastaID).setValue(hasta);

        //Data doğru basıldı mı kontrol ediyoruz
        //Dikkat edilmesi gereken husus child parametresi vermezsen tüm databasi getiriyor

        hastaDB.child(hastaID).addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getActivity().getPackageManager())!=null){
            File photofile = null;
            try {
                photofile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(photofile != null){
                Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(),"com.agaoglu.tez.fileprovider",photofile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        Intent resmi_isle = new Intent(getActivity(),resimIsle.class);
        resmi_isle.putExtra("tip","kamera");
        resmi_isle.putExtra("resim_yolu",mCurrentPhotoPath);
        Log.e("yenipath",mCurrentPhotoPath);
        resmi_isle.putExtra("hastaID",hastaID);
        startActivity(resmi_isle);
        return image;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent resmi_isle = new Intent(getActivity(),resimIsle.class);
            resmi_isle.putExtra("tip","galeri");
            resmi_isle.putExtra("resim_yolu",data.getData().toString());
            resmi_isle.putExtra("hastaID",hastaID);
            startActivity(resmi_isle);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Util.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Fotoğraf Çek"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Hafızadan Seç"))
                        galleryIntent();
                } else {
                    //code for deny
                    //// TODO: 19.03.2017 kullanıcı resim almaya izin vermezse yapılması gerekeni buraya yaz
                }
                break;
        }
    }
}