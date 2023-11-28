package com.example.appcrazydisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class imgSel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_sel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.appColor));
        }

        ImageView img1 = (ImageView) findViewById(R.id.imageView2);
        img1.setImageResource(R.drawable.crazy_dyamond);
        img1.getLayoutParams().width = 190 * 2;
        img1.getLayoutParams().height = 247 * 2;
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.crazy_dyamond);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream .toByteArray();

                                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                String nuevaCadena = encodedImage.replace("\n", "").replace("\r", "");
                                Log.i("WWWWWWW", "{\"type\": \"img\", \"img\":\" "+nuevaCadena+"\"}");

                                MainActivity.clientApp.send("{\"type\": \"img\", \"image\": \""+nuevaCadena+"\" }");
                            }
                        });
                    }
                });
            }
        });

        ImageView img2 = (ImageView) findViewById(R.id.imageView3);
        img2.setImageResource(R.drawable.si);
        img2.getLayoutParams().width = 190 * 2;
        img2.getLayoutParams().height = 247 * 2;

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.si);

                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                byte[] byteArray = outputStream.toByteArray();
                                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                                //Log.i("WWWWWWW", "{\"type\": \"img\", \"img\":\" "+encodedImage+"\"}");

                                MainActivity.clientApp.send("{\"type\": \"img\", \"image\": \""+encodedImage+"\" }");
                            }
                        });
                    }
                });
            }
        });

        ImageView img3 = (ImageView) findViewById(R.id.imageView4);
        img3.setImageResource(R.drawable.morioh);
        img3.getLayoutParams().width = 190 * 2;
        img3.getLayoutParams().height = 247 * 2;

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.morioh);

                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                                byte[] byteArray = outputStream.toByteArray();
                                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                MainActivity.clientApp.send("{\"type\": \"img\", \"image\": \""+encodedImage+"\" }");
                            }
                        });
                    }
                });
            }
        });
    }
}