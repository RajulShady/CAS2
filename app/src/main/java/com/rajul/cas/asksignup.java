package com.rajul.cas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class asksignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asksignup);
    }
    public void signupScreen(View v){
        Intent intent = new Intent(getApplicationContext(), signup.class);
        startActivity(intent);
    }
    public void jumptoAskLoginScreen(View v){
        Intent intent = new Intent(getApplicationContext(), AskLogin.class);
        startActivity(intent);
    }
}
