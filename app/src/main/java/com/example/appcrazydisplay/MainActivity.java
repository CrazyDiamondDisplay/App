package com.example.appcrazydisplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    boolean connected = true;
    ArrayList<Message> data;
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

        data = updateMessages();
        Log.i("AYUDA", String.valueOf(data.size()));

        EditText id = (EditText) findViewById(R.id.ipText);
        EditText mssg = (EditText) findViewById(R.id.messageText);
        Button connect = (Button) findViewById(R.id.connect);
        Button send = (Button) findViewById(R.id.send);
        Button list = (Button) findViewById(R.id.buttonList);
        send.setEnabled(false);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connected){
                    send.setEnabled(true);
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
                    send.setEnabled(false);
                }
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mssg.getText().toString();
                saveMessage(message);
                clientApp.send(message);
            }
        });
    }

    public void saveMessage(String data) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = openFileOutput("mssgHistory.mssg", MODE_APPEND);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(new Message(data));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<Message> updateMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = openFileInput("mssgHistory.mssg");
            while (true) {
                ois = new ObjectInputStream(fis);
                Message message = (Message) ois.readObject();
                messages.add(message);
            }
        } catch (EOFException ignored) {
            // EOFException se lanzará cuando no haya más objetos que leer.
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("ARRARRRAR", String.valueOf(messages.size()));
        return messages;
    }
}

class Message implements Serializable{
    String text;
    Date date;
    Message(String text){
        this.text = text;
        Date date = new Date();
    }

    public String toString() {
        return text;
    }
}