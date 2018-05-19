package com.crowdfire.wardrobe;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Iterator;

public class ImageData
{
    ArrayList<String> listOfAllImages;
    ArrayList<String> latestImage;
    public ArrayList topView(Context ctx){
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = ctx.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

         int ss=cursor.getCount();
         System.out.println("cloumn::"+ss);
//        for(int j=0;j<cursor.getCount();j++){
//            absolutePathOfImage = cursor.getString(column_index_data);
//
//            listOfAllImages.add(absolutePathOfImage);
//        }

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }

        cursor.close();
        for(int i=0;i<listOfAllImages.size();i++){
            System.out.println("Images are::"+listOfAllImages.get(i));
        }


        return listOfAllImages;
    }

    public ArrayList capturedImage(Context ctx){

        String image = "0";
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        listOfAllImages = new ArrayList<String>();
        latestImage =new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = ctx.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        int ss=cursor.getCount();
        System.out.println("cloumn::"+ss);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }


        latestImage.add(listOfAllImages.get(listOfAllImages.size()-1));
        image = listOfAllImages.get(listOfAllImages.size()-1);

        System.out.println("Latest image::"+image);

        cursor.close();

        return latestImage;
    }
}
