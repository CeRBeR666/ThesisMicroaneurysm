package com.agaoglu.tez;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class hastaSec extends Fragment {
    private DatabaseReference hastaDB;
    private List<hasta> kayitlar;
    private ProgressDialog Dialog;
    static View.OnLongClickListener myLongClickListener;



    public hastaSec() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hasta_sec, container, false);
        getActivity().setTitle("Hasta Seç");
        setHasOptionsMenu(true);

        myLongClickListener = new MyLongClickListener(getContext());


        kayitlar = new ArrayList<>();
        hastaDB = FirebaseDatabase.getInstance().getReference("hastalar");


        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.hastaseclist);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshhasta);
        final hastasecadapter adapter = new hastasecadapter(kayitlar, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Dialog = new ProgressDialog(getActivity());
        Dialog.setMessage("Hastalar Yükleniyor");
        Dialog.show();


        hastaDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    hasta hasta = dataSnapshot.getValue(com.agaoglu.tez.hasta.class);
                    adapter.insert(0,hasta);
                    adapter.notifyDataSetChanged();
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

        hastaDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Dialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.form)," Bir hata oluştu", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }

            private void refreshItems() {
                //// TODO: 18.03.2017 Aşağıya çekince güncelleme yapılacak. Bu kısımda biraz sıkıntı var
                onItemsLoadComplete();
            }
            private void onItemsLoadComplete() {
                swipeRefreshLayout.setRefreshing(false);
            }   
        });




        return view;
    }


    public class MyLongClickListener implements View.OnLongClickListener{
        private final Context context;
        private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
        private String userChoosenTask;
        private String mCurrentPhotoPath;

        private MyLongClickListener(Context context) {
            this.context = context;
        }

        @Override
        public boolean onLongClick(View v) {
            selectImage();
            return false;
        }

        private void selectImage() {
            final CharSequence[] items = { "Fotoğraf Çek", "Hafızadan Seç",
                    "İptal" };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Yeni göz resmi ekle");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    boolean result=Util.checkPermission(context);

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

        private void galleryIntent() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
        }

        public void activityResult(int requestCode, int resultCode, Intent data){
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult(data);
            }
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
            //resmi_isle.putExtra("hastaID",hastaID);
            startActivity(resmi_isle);
            return image;
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
                //resmi_isle.putExtra("hastaID",hastaID);
                startActivity(resmi_isle);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MyLongClickListener longClickListener = new MyLongClickListener(getContext());
        longClickListener.activityResult(requestCode,resultCode,data);
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
