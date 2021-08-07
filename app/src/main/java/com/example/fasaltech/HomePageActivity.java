package com.example.fasaltech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class HomePageActivity extends AppCompatActivity {
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Intent intent=getIntent();
        token=intent.getStringExtra("token");
        addTokenInfo(token);
    }
    private void addTokenInfo(String token){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.fasaltech",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("page_no",12);
        myEdit.putString("token",token);
        Log.i("Saved","token at login endpoint");
        myEdit.commit();
    }
}