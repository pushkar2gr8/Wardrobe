package com.crowdfire.wardrobe;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;

public class Wardrobe extends AppCompatActivity {

    RecyclerView topView,bottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe);

        topView = (RecyclerView)findViewById(R.id.top_view);

        //capture the size of the devices screen
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        int height = displaymetrics.heightPixels;
        topView.getLayoutParams().height = (height-250)/2;
    }
}
