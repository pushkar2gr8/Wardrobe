package com.crowdfire.wardrobe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class Wardrobe extends AppCompatActivity {

    RecyclerView topView,bottomView;
    com.github.clans.fab.FloatingActionButton cameraButton,middleButton,gallaryButton,topCameraButton,topGallaryButton;
    Context ctx;
    ImageView img;
    private final int requestCode = 20;
    public  static final int RequestPermissionCode  = 1;
    String cambutton = "false";

    ArrayList<Bitmap> cameraImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe);

        if (ActivityCompat.shouldShowRequestPermissionRationale(Wardrobe.this,
                Manifest.permission.CAMERA))
        {
            Toast.makeText(Wardrobe.this,
                    "CAMERA permission allows us to Access CAMERA app",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(Wardrobe.this,new String[]{
                    Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);
        }

        topView = (RecyclerView)findViewById(R.id.top_view);
        bottomView = (RecyclerView)findViewById(R.id.bottom_view);

        cameraButton = (com.github.clans.fab.FloatingActionButton)
                findViewById(R.id.bottom_floating_camera);

        gallaryButton = (com.github.clans.fab.FloatingActionButton)
                findViewById(R.id.bottom_floating_file_system);

        topCameraButton = (com.github.clans.fab.FloatingActionButton)
                findViewById(R.id.top_floating_camera);

        topGallaryButton = (com.github.clans.fab.FloatingActionButton)
                findViewById(R.id.top_floating_file_system);

        //capture the size of the devices screen
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        int height = displaymetrics.heightPixels;
        topView.getLayoutParams().height = (height-250)/2;

        //new loadImages().execute();

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
                //startActivity(photoCaptureIntent);
                setImage(v);
            }
        });

        gallaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        topCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
//                startActivity(photoCaptureIntent);
                setImage(v);
            }
        });
    }


    public void setImage(View view){
        switch(view.getId()){
            case R.id.top_floating_camera:
                cambutton = "top";
                break;
            case R.id.bottom_floating_camera:
                cambutton = "bottom";
                break;
            // even more buttons here
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(this.requestCode == requestCode && resultCode == RESULT_OK){
            if(cambutton.equals("top")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        topView.setHasFixedSize(true);
                        topView.setLayoutManager(new LinearLayoutManager(getApplicationContext()
                                ,LinearLayoutManager.HORIZONTAL,false));
                        ImageLoaderAdapter adapter = new ImageLoaderAdapter(getApplicationContext(),
                                new ImageData().capturedImage(getApplicationContext()));
                        topView.addItemDecoration
                                (new DividerItemDecoration(topView.getContext()
                                        , DividerItemDecoration.HORIZONTAL));
                        topView.setAdapter(adapter);
                    }
                });
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bottomView.setHasFixedSize(true);
                        bottomView.setLayoutManager(new LinearLayoutManager(getApplicationContext()
                                ,LinearLayoutManager.HORIZONTAL,false));
                        ImageLoaderAdapter adapter = new ImageLoaderAdapter(getApplicationContext(),
                                new ImageData().capturedImage(getApplicationContext()));
                        bottomView.addItemDecoration
                                (new DividerItemDecoration(bottomView.getContext()
                                        , DividerItemDecoration.HORIZONTAL));
                        bottomView.setAdapter(adapter);
                    }
                });
            }
        }
    }

    public class loadImages extends AsyncTask<Void , Void ,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    setTopView();
                    setBottomView();
                }
            });t.start();
            return null;
        }

        public void setTopView(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    topView.setHasFixedSize(true);
                    topView.setLayoutManager(new LinearLayoutManager(getApplicationContext()
                            ,LinearLayoutManager.HORIZONTAL,false));
                    ImageLoaderAdapter adapter = new ImageLoaderAdapter(getApplicationContext(),
                            new ImageData().topView(getApplicationContext()));
                    topView.addItemDecoration
                            (new DividerItemDecoration(topView.getContext()
                                    , DividerItemDecoration.HORIZONTAL));
                    topView.setAdapter(adapter);
                }
            });
        }

        public void setBottomView(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bottomView.setHasFixedSize(true);
                    bottomView.setLayoutManager(new LinearLayoutManager(getApplicationContext()
                            ,LinearLayoutManager.HORIZONTAL,false));
                    ImageLoaderAdapter adapter = new ImageLoaderAdapter(getApplicationContext(),
                            new ImageData().topView(getApplicationContext()));
                    bottomView.addItemDecoration
                            (new DividerItemDecoration(bottomView.getContext()
                                    , DividerItemDecoration.HORIZONTAL));
                    bottomView.setAdapter(adapter);
                }
            });
        }

    }
}
