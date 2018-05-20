package com.crowdfire.wardrobe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class Wardrobe extends AppCompatActivity {

    RecyclerView topView,bottomView;
    com.github.clans.fab.FloatingActionButton cameraButton,gallaryButton,topCameraButton,topGallaryButton;
    FloatingActionButton middleButton,likebutton;
    Context ctx;
    ImageView img;
    private final int requestCode = 20;
    public  static final int RequestPermissionCode  = 1;
    String cambutton = "false";
    private int PICK_IMAGE_REQUEST = 1;
    ArrayList<String> gImage = new ArrayList<String>();

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
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , RequestPermissionCode);
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

        middleButton = (FloatingActionButton)findViewById(R.id.middle_add);

        likebutton = (FloatingActionButton)findViewById(R.id.like_button);

        //capture the size of the devices screen
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        int height = displaymetrics.heightPixels;
        topView.getLayoutParams().height = (height-250)/2;


        middleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new loadImages().execute();
            }
        });

        likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCameraImage();
                setImage(v);
            }
        });

        gallaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                setImage(v);
            }
        });

        topCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCameraImage();
                setImage(v);
            }
        });

        topGallaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                setImage(v);
            }
        });
    }

    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void pickCameraImage(){
        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoCaptureIntent, requestCode);
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void setImage(View view){
        switch(view.getId()){
            case R.id.top_floating_camera:
                cambutton = "top";
                break;
            case R.id.bottom_floating_camera:
                cambutton = "bottom";
                break;
            case R.id.top_floating_file_system:
                cambutton = "top_gallary";
                break;
            case R.id.bottom_floating_file_system:
                cambutton = "bottom_gallary";
                break;
                // even more buttons here
        }

    }

    public void saveBitmap(Bitmap bitmap) {
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
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
        if(cambutton.equals("top_gallary")){
            Uri uri = data.getData();
            String path = null;

            try {
                path = uri.toString();
                gImage.add(path);
                System.out.println("selected images"+path);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        topView.setHasFixedSize(true);
                        topView.setLayoutManager(new LinearLayoutManager(getApplicationContext()
                                ,LinearLayoutManager.HORIZONTAL,false));
                        ImageLoaderAdapter adapter =
                                new ImageLoaderAdapter(getApplicationContext(),gImage);
                        topView.addItemDecoration
                                (new DividerItemDecoration(topView.getContext()
                                        , DividerItemDecoration.HORIZONTAL));
                        topView.setAdapter(adapter);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(cambutton == "bottom_gallary"){
            Uri uri = data.getData();

            try {
                String path = uri.toString();
                gImage.add(path);
                System.out.println("selected images"+path);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bottomView.setHasFixedSize(true);
                        bottomView.setLayoutManager(new LinearLayoutManager(getApplicationContext()
                                ,LinearLayoutManager.HORIZONTAL,false));
                        ImageLoaderAdapter adapter =
                                new ImageLoaderAdapter(getApplicationContext(),gImage);
                        bottomView.addItemDecoration
                                (new DividerItemDecoration(bottomView.getContext()
                                        , DividerItemDecoration.HORIZONTAL));
                        bottomView.setAdapter(adapter);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
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
