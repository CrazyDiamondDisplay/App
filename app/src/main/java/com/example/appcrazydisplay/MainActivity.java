package com.example.appcrazydisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {
    boolean connected = true;
    ArrayList<Message> data;
    static ClientApp clientApp;
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

        loginAlert();

        data = updateMessages();

        EditText id = (EditText) findViewById(R.id.ipText);
        EditText mssg = (EditText) findViewById(R.id.messageText);
        Button connect = (Button) findViewById(R.id.connect);
        Button send = (Button) findViewById(R.id.send);
        Button list = (Button) findViewById(R.id.buttonList);
        send.setEnabled(false);
        list.setEnabled(false);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connected){
                    send.setEnabled(true);
                    list.setEnabled(true);
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
                    list.setEnabled(false);
                }
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mssg.getText().toString();
                final AtomicBoolean exist = new AtomicBoolean(false);
                if(!data.isEmpty()){
                    data.forEach(message1 -> {
                        if (message1.text.equals(message)){
                            message1.date = new Date();
                            exist.set(true);
                        }
                    });
                }
                if(!exist.get()) saveMessage(message, data);

                clientApp.send(message);
            }
        });
    }

    public void saveMessage(String mssg, ArrayList<Message> data) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = openFileOutput("mssgHistory.mssg", MODE_APPEND);
            oos = new ObjectOutputStream(fos);
            Message m = new Message(mssg);
            data.add(m);
            oos.writeObject(m);
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

    public void loginAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_signin, null);
        builder.setView(dialogView);

        EditText usernameEditText = dialogView.findViewById(R.id.name);
        EditText passwordEditText = dialogView.findViewById(R.id.pass);

        builder.setTitle("Sign In")
                .setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String username = usernameEditText.getText().toString();
                        String password = passwordEditText.getText().toString();

                        if(!username.equals("admin") || !password.equals("admin")){
                            Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                            loginAlert();
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

class Message implements Serializable{
    String text;
    Date date;
    Message(String text){
        this.text = text;
        this.date = new Date();
    }

    public String toString() {
        return text;
    }

}