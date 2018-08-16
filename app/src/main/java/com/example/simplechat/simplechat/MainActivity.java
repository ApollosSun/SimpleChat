package com.example.simplechat.simplechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText roomName;
    EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roomName = findViewById(R.id.et_room);
        userName = findViewById(R.id.et_name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();
    }

    public void createRoom (View v){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("room",roomName.getText().toString());
        intent.putExtra("user",userName.getText().toString());
        startActivity(intent);
    }

    public boolean isNetAvailable() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return false;
    }

    public void checkInternet() {

        if (isNetAvailable()){

        } else {

            Snackbar snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    R.string.no_Internet_message,
                    Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkInternet();
                }
            });

            snackbar.show();
        }
    }

}