package com.remotedesktopcontrol.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.remotedesktopcontrol.R;
import com.remotedesktopcontrol.backend.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Accueil extends Activity{
    private static Client client;
    private Boolean success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        final Button buttonValidate = (Button) findViewById(R.id.buttonValidate);
        final EditText textAreaIP = (EditText) findViewById(R.id.textAreaIP);
        final EditText textAreaPort = (EditText) findViewById(R.id.textAreaPort);

        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (textAreaIP.getText().toString().equals("") || textAreaPort.getText().toString().equals("")) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast errorToast = Toast.makeText(Accueil.this, "Error, all fields are required", Toast.LENGTH_SHORT);
                            errorToast.show();
                        }
                    });
                } else {

                    success = false;
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                client = new Client(textAreaIP.getText().toString(), Integer.parseInt(textAreaPort.getText().toString()));
                                success = true;
                            } catch (IOException e) {
                                success = false;
                            } finally {

                                if (success) {
                                    Intent switchActivityIntent = new Intent(Accueil.this, Remote.class);
                                    startActivity(switchActivityIntent);
                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast errorToast = Toast.makeText(Accueil.this, "Error, check your internet connection and try again!", Toast.LENGTH_LONG);
                                            errorToast.show();
                                        }
                                    });
                                }

                            }
                        }
                    });
                    thread.start();
                }
            }
        });

    }

    public static Client getClient() {
        return Accueil.client;
    }
}

