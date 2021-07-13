package com.example.fasaltech;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class FieldDetailsActivity extends AppCompatActivity {
    EditText total_acresEditText;
    TextView fieldTextView;
    Button buttonToProductPage;
    String total_acres;
    RequestQueue queue;
    final String field_data_url ="http://ec2-52-66-244-191.ap-south-1.compute.amazonaws.com/add-field/";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_details);
        Intent intent=getIntent();
        queue= Volley.newRequestQueue(this);
        total_acresEditText=findViewById(R.id.total_acresEditText);
        fieldTextView=findViewById(R.id.fieldTextView);
        buttonToProductPage=findViewById(R.id.buttonToProductPage);
        fieldTextView.setText("How much acres do you own?");
        buttonToProductPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_acres=total_acresEditText.getText().toString().trim();
                JSONObject fieldDetailsObject=new JSONObject();
                try {
                    fieldDetailsObject.put("total_acres",total_acres);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest objectRequest=new JsonObjectRequest(
                        Request.Method.GET,
                        field_data_url,
                        fieldDetailsObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        //headers.put("Authorization", "Token "+token);
                        return headers;
                    }
                };
                queue.add(objectRequest);


            }
        });

    }
}