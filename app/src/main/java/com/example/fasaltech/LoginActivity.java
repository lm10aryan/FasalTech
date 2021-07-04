package com.example.fasaltech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    RequestQueue queue ;
    String phone_number="";
    String password="";

    final String url ="http://ec2-52-66-244-191.ap-south-1.compute.amazonaws.com/auth/token/login/";
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
                password=passwordEditText.getText().toString();

                final JSONObject object2=new JSONObject();
                try {
                    object2.put("phone_number",phone_number);
                    object2.put("password",password);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        object2,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    token=response.getString("auth_token");
                                    Log.i("Login", token);
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
    }

}
