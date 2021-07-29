package com.example.fasaltech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fasaltech.constant.Api;
import com.example.fasaltech.crop_data.CropDataActivity;
import com.example.fasaltech.login.LoginActivity;
import com.example.fasaltech.watermelon.WatermelonQuestionsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RequestQueue queue ;
    EditText editText;
    Button loginbutton;
    Button registerButton;
    String phone_number="";
    EditText passwordEditText;
    String password;
    final String url ="http://ec2-52-66-244-191.ap-south-1.compute.amazonaws.com/auth/token/login/";
    String token;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("com.example.fasaltech",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        //myEdit.putInt("page_no",1);
        myEdit.commit();

        queue= Volley.newRequestQueue(this);
        editText=findViewById(R.id.editText);
        loginbutton=findViewById(R.id.button);
        registerButton=findViewById(R.id.registerButton);
        passwordEditText=findViewById(R.id.passwordloginEditText);
        isUserLoggedOrNot();
        //sendToNextPage(); //Using this for now... Need to delete it to start from otp verification page

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password=passwordEditText.getText().toString().trim();
                phone_number=editText.getText().toString().trim();
                final JSONObject object2=new JSONObject();
                try {
                    object2.put("phone_number",phone_number);
                    object2.put("password",password);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                        Request.Method.POST,
                        Api.loginUrl,
                        object2,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    token=response.getString("auth_token");
                                    Intent intent=new Intent(MainActivity.this, FarmerDetailActivity.class);
                                    intent.putExtra("token",token);
                                    SharedPreferences sharedPreferences = getSharedPreferences("com.example.fasaltech",MODE_PRIVATE);
                                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                    myEdit.putString("token",token);
                                    myEdit.commit();
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Error","error");
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Charset", "UTF-8");
                        return params;
                    }
                };
                queue.add(jsonObjectRequest);
            }
        });

        //Register Button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_number=  editText.getText().toString();
                Intent intent=new Intent(MainActivity.this, AuthenticationActivity.class);
                intent.putExtra("phone_number",phone_number);
                startActivity(intent);
            }
        });
    }
    public void sendToNextPage(){
        Intent intent=new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("token","74db454e1cf94292d815cd771ebd878df0c7c46e");
        startActivity(intent);
    }
    public void isUserLoggedOrNot(){
        sharedPreferences=getSharedPreferences("com.example.fasaltech",MODE_PRIVATE);
        int number=sharedPreferences.getInt("page_no",100);
        String token =sharedPreferences.getString("token","");
        if(token != null){
            if(number==4){
                Intent intent=new Intent(MainActivity.this,FarmerDetailActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
            }
            else if(number==5){
                Intent intent=new Intent(MainActivity.this,FieldDetailsActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
            }
            else if(number==6){
                Intent intent=new Intent(MainActivity.this,CropDataActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
            }
        }
    }
}
