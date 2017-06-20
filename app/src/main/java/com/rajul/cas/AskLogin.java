package com.rajul.cas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AskLogin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_login);
    }
    public void jumptoAskSignupScreen(View v){
        Intent intent = new Intent(getApplicationContext(), asksignup.class);
        startActivity(intent);
    }
    public void jumptoLoginScreen(View v){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);

    }

}