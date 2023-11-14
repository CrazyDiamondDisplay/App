package com.example.appcrazydisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    boolean connected = true;
    ClientApp clientApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.appColor));
        }
        ImageView logo = (ImageView) findViewById(R.id.imageView);
        logo.setImageResource(R.drawable.morioh);
        logo.getLayoutParams().width = 190*2;
        logo.getLayoutParams().height = 247*2;

        EditText id = (EditText) findViewById(R.id.ipText);
        EditText mssg = (EditText) findViewById(R.id.messageText);
        Button connect = (Button) findViewById(R.id.connect);
        Button send = (Button) findViewById(R.id.send);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connected){
                    connected = false;
                    connect.setText("Disconnect");
                    String idServer = id.getText().toString();
                    URI uri = null;
                    try {
                        uri = new URI("ws://" + idServer + ":8888");
                        clientApp = new ClientApp(uri);

                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    connected = true;
                    connect.setText("Connect");
                    clientApp.close();
                }


            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mssg.getText().toString();
                clientApp.send(message);
            }
        });
    }
}