package com.agaoglu.tez;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;


import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.github.chrisbanes.photoview.PhotoView;
import com.shawnlin.numberpicker.NumberPicker;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;


public class analiz extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String selectedImagePath;
    private Mat sampledImage;
    protected View view;
    private CrystalRangeSeekbar cannyrangeSeekbar;
    private NumberPicker numberPicker;
    private CrystalSeekbar hougcircleSeekbar;
    private float lastX;
    private Button guncelle;
    private String resim_yolu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analiz);
        setTitle("Görüntü Analizi");

        Intent i = getIntent();
        resim_yolu = i.getStringExtra("resim_yolu");


        ViewPager viewPager = (HackyViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new AnalizAdapter(this));


        //Bug Fix alttaki fonksiyon tamamiyle saçmalık 3_2_0 versiyonunda opencv nin çalışması için eklenmiş bir kod
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
        //Bug Fix


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cannyrangeSeekbar = (CrystalRangeSeekbar) navigationView.getHeaderView(0).findViewById(R.id.cannyseekbar);
        final TextView tvmin = (TextView) navigationView.getHeaderView(0).findViewById(R.id.cannymintxt);
        final TextView tvmax = (TextView) navigationView.getHeaderView(0).findViewById(R.id.cannymaxtxt);

        cannyrangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvmin.setText(minValue.toString());
                tvmax.setText(maxValue.toString());
            }
        });

        hougcircleSeekbar = (CrystalSeekbar) navigationView.getHeaderView(0).findViewById(R.id.houghcircleseekbar);
        final TextView ctv = (TextView) navigationView.getHeaderView(0).findViewById(R.id.houghcirclemintxt);
        hougcircleSeekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                ctv.setText(value.toString());
            }
        });

        guncelle = (Button) navigationView.getHeaderView(0).findViewById(R.id.guncelle);
        guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Şimdi değerleri getirelim.
                int canymin = Integer.valueOf(tvmin.getText().toString());
                int canymax = Integer.valueOf(tvmax.getText().toString());
                NumberPicker numberPicker1 = (NumberPicker) navigationView.getHeaderView(0).findViewById(R.id.houglinetreshnumber);
                int houglinethres = numberPicker1.getValue();
                NumberPicker numberPicker2 = (NumberPicker) navigationView.getHeaderView(0).findViewById(R.id.houglinedotnumber);
                int houglinedot = numberPicker2.getValue();
                NumberPicker numberPicker3 = (NumberPicker) navigationView.getHeaderView(0).findViewById(R.id.houglinespacenumber);
                int houglinespace = numberPicker3.getValue();
                int hougcirclethres = Integer.valueOf(ctv.getText().toString());

                resmiisle(canymin,canymax,houglinethres,houglinedot,houglinespace,hougcirclethres,resim_yolu);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.analiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        /*
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS:{
                    Log.i("OpencvLoaded ?","Opencv Yüklendi");
                    resmiisle(0,0,0,0,0,0,resim_yolu);
                }break;
                default:
                {
                    super.onManagerConnected(status);
                }break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0,this,mLoaderCallback);
    }

    public void resmiisle(int canymin, int canymax, int houglinethres, int houglinedot, int houglinespace, int hougcirclethres, String path){
        canymin = canymin != 0 ? canymin : 18;
        canymax = canymax != 0 ? canymax : 32;
        houglinethres = houglinethres != 0 ? houglinethres : 100;
        houglinedot = houglinedot != 0 ? houglinedot : 100;
        houglinespace = houglinespace != 0 ? houglinespace : 50;
        hougcirclethres = hougcirclethres != 0 ? hougcirclethres : 60;

        Uri selectedImageUri = Uri.parse(path);
        selectedImagePath = Util.getPath(analiz.this,selectedImageUri);
        loadImage(selectedImagePath);

        addImage(sampledImage);

        Mat gray = new Mat();
        Imgproc.cvtColor(sampledImage,gray,Imgproc.COLOR_BGR2GRAY);
        //Imgproc.blur(gray,gray,new Size(2,2));
        Imgproc.medianBlur(gray,gray,5);


        addImage(gray);

        Mat edges = new Mat();

        Imgproc.Canny(gray,edges,canymin,canymax);

        addImage(edges);

        Mat lines = new Mat();
        Imgproc.HoughLinesP(edges, lines, 1, Math.PI / 180 , houglinethres, houglinedot ,houglinespace);

        for (int i =0; i < lines.cols(); i++){
            double[] val = lines.get(0,i);
            Imgproc.line(edges, new org.opencv.core.Point(val[0],val[1]), new org.opencv.core.Point(val[2],val[3]), new Scalar(0,0,255), 2);
        }

        Mat nolines = new Mat();
        Core.subtract(gray,edges,nolines);
        addImage(nolines);

        Mat circles = new Mat();
        int minRadius = 1;
        int maxRadius = 10;
        Imgproc.HoughCircles(nolines,circles, Imgproc.HOUGH_GRADIENT, 1 , minRadius, hougcirclethres , 10 , minRadius, maxRadius);
        Log.d("deneme",circles.toString());

        for (int j =0; j < circles.cols(); j++){
            double circle[] = circles.get(0,j);
            org.opencv.core.Point pt = new org.opencv.core.Point(Math.round(circle[0]),Math.round(circle[1]));
            int radius = (int) Math.round(circle[2]);

            Imgproc.circle(sampledImage, pt, radius, new Scalar(0,255,0), 1);
            Imgproc.circle(sampledImage, pt, 3, new Scalar(0,0,255), 1);
        }
        addImage(sampledImage);



    }

    private void loadImage(String path) {
        Mat originalImage = Imgcodecs.imread(path);
        Mat rgbImage = new Mat();

        Imgproc.cvtColor(originalImage,rgbImage,Imgproc.COLOR_BGR2RGB);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        sampledImage = new Mat();
        Size ratio = new Size(width,height);

        Imgproc.resize(rgbImage,sampledImage,ratio,0,0,Imgproc.INTER_AREA);
    }

    private void addImage(Mat image) {
        Bitmap bitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(image, bitmap);
        new AnalizAdapter(this).addItem(bitmap);
    }

    static class AnalizAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        private ArrayList<Bitmap> sonuclar = new ArrayList<Bitmap>();


        public AnalizAdapter(Context context) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);

        }

        public void addItem(final Bitmap resim){
            sonuclar.add(resim);
            Log.e("sonuclar",sonuclar.toString());
            notifyDataSetChanged();
        }


        private static final int[] sDrawables = { R.drawable.patient_eye, R.drawable.retina_ready_icon, R.drawable.thumb};


        @Override
        public int getCount() {
            //return sonuclar.size();
            return sDrawables.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setImageResource(sDrawables[position]);
            Log.e("pozisyon",String.valueOf(position));
//            Log.e("sonuc",sonuclar.get(0).toString());
            //photoView.setImageBitmap(sonuclar.get(position));
            container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
