package com.example.fasaltech.login;

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
import com.example.fasaltech.FarmerDetailActivity;
import com.example.fasaltech.R;
import com.example.fasaltech.constant.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    RequestQueue queue ;
    String phone_number="";
    String password="";
    String re_password="";
    String token="";
    EditText passwordEditText;
    EditText repasswordEditText;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button=findViewById(R.id.button);
        queue= Volley.newRequestQueue(this);
        Intent intent=getIntent();

        phone_number=intent.getStringExtra("phone_number");
        passwordEditText=findViewById(R.id.passwordEditText);
        repasswordEditText=findViewById(R.id.repasswordEditText);
        password=passwordEditText.getText().toString();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UserCompleteLogin();
                password=passwordEditText.getText().toString().trim();
                re_password=repasswordEditText.getText().toString().trim();
                if(password.matches(re_password)){
                    final JSONObject object2=new JSONObject();
                    try {
                        object2.put("phone_number",phone_number);
                        object2.put("password",password);
                        object2.put("re_password",re_password);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                            Request.Method.POST,
                            Api.registerUrl,
                            object2,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    UserCompleteLogin();
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
            }
        });
    }

    public void UserCompleteLogin(){
        password=passwordEditText.getText().toString();
        final JSONObject object3=new JSONObject();
        try {
            object3.put("phone_number",phone_number);
            object3.put("password",password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.POST,
                Api.loginUrl,
                object3,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent1=new Intent(LoginActivity.this, FarmerDetailActivity.class);
                        try {
                            token = response.getString("auth_token");
                            intent1.putExtra("token",token);
                            //addTokenInfo(token);
                            Log.i("log",token);
                            startActivity(intent1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Error",error.getLocalizedMessage());
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
    private void addTokenInfo(String token){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.fasaltech",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("page_no",3);
        myEdit.putString("token",token);
        Log.i("Saved","token at login endpoint");
        myEdit.commit();
    }

}
