package com.example.appcrazydisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.appColor));

        ListView listView = (ListView) findViewById(R.id.list);
        ArrayList<Message> data = (ArrayList<Message>) getIntent().getSerializableExtra("data");

        /*
        if(data.size() > 1){
            Collections.sort(data, new Comparator<Message>() {
                @Override
                public int compare(Message m1, Message m2) {
                    return m2.date.compareTo(m1.date);
                }
            });
        }
        */

        ArrayAdapter<Message> adapter = new ArrayAdapter<Message>( this, R.layout.list_item, data)
        {
            @Override
            public View getView(int pos, View convertView, ViewGroup container)
            {
                if( convertView==null ) {
                    convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
                }
                ((TextView) convertView.findViewById(R.id.mssg)).setText(getItem(pos).text);
                String text =  ((TextView) convertView.findViewById(R.id.mssg)).getText().toString();
                ((TextView) convertView.findViewById(R.id.mssg)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                        builder.setMessage("Do you want to send it?")  // Establece el mensaje
                                .setTitle("Confirmation")          // Establece el título del cuadro de diálogo
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        MainActivity.clientApp.send("{\"type\": \"mssg\", \"text\":\""+text+"\"}");
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(ListActivity.this, "Not sent", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                return convertView;
            }

        };
        listView.setAdapter(adapter);
    }

}
