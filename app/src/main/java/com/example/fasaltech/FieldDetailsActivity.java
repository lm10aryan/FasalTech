package com.example.fasaltech;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.fasaltech.constant.Api;
import com.example.fasaltech.crop_data.CropDataActivity;

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
    String token;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_details);
        Intent intent=getIntent();
        token=intent.getStringExtra("token");
        queue= Volley.newRequestQueue(this);
        total_acresEditText=findViewById(R.id.total_acresEditText);
        fieldTextView=findViewById(R.id.fieldTextView);
        buttonToProductPage=findViewById(R.id.buttonToProductPage);
        fieldTextView.setText("How much acres do you own?");
        addTokenInfo();

        buttonToProductPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFunctionValidation()){
                    total_acres=total_acresEditText.getText().toString().trim();
                    JSONObject fieldDetailsObject=new JSONObject();
                    try {
                        fieldDetailsObject.put("total_acres",total_acres);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest objectRequest=new JsonObjectRequest(
                            Request.Method.POST,
                            Api.field_data_url,
                            fieldDetailsObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Intent intent1=new Intent(FieldDetailsActivity.this, CropDataActivity.class);
                                    intent1.putExtra("token",token);
                                    startActivity(intent1);
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
                            headers.put("Authorization", "Token "+token);
                            return headers;
                        }
                    };
                    queue.add(objectRequest);
                }
            }
        });
    }
    private void addTokenInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.fasaltech",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("page_no",5);
        Log.i("we are in","field temp");
        myEdit.commit();
    }
    public boolean checkFunctionValidation(){
        if(total_acresEditText.getText().toString().isEmpty()){
            total_acresEditText.setError("Dont leave this empty");
            return false;
        }
        return true;
    }

}