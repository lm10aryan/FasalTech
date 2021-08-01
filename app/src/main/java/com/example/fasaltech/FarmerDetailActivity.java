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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FarmerDetailActivity extends AppCompatActivity {
    Button buttonToNextPage;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextStreet;
    EditText editTextVillage;
    EditText editTextDistrict;
    EditText editTextState;
    EditText editTextPinCode;
    String token;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_detail);
        Intent intent=getIntent();
        token=intent.getStringExtra("token");
        addTokenInfo();
        // SharedPreferences sharedPreferences=getSharedPreferences("com.example.fasaltech",MODE_PRIVATE);
        //token=sharedPreferences.getString("token","");

        queue= Volley.newRequestQueue(this);
        buttonToNextPage=findViewById(R.id.buttonToNextPage);
        editTextFirstName=findViewById(R.id.editTextFIrstName);
        editTextLastName=findViewById(R.id.editTextLastName);
        editTextStreet=findViewById(R.id.editTextStreet);
        editTextVillage=findViewById(R.id.editTextVillage);
        editTextDistrict=findViewById(R.id.editTextDIstrict);
        editTextState=findViewById(R.id.editTextState);
        editTextPinCode=findViewById(R.id.editTextPinCode);
        buttonToNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject farmerDetailsObject=new JSONObject();
                try {
                    farmerDetailsObject.put("f_name",editTextFirstName.getText().toString().trim());
                    farmerDetailsObject.put("l_name",editTextLastName.getText().toString().trim());
                    farmerDetailsObject.put("street",editTextStreet.getText().toString().trim());
                    farmerDetailsObject.put("village",editTextVillage.getText().toString().trim());
                    farmerDetailsObject.put("district",editTextDistrict.getText().toString().trim());
                    farmerDetailsObject.put("state",editTextState.getText().toString().trim());
                    farmerDetailsObject.put("pin_code",editTextPinCode.getText().toString().trim());
                    //Sending GET Request to upload farmer personal details data
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest objectRequest=new JsonObjectRequest(
                        Request.Method.POST,
                        Api.farmer_data_url,
                        farmerDetailsObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Intent intent1=new Intent(FarmerDetailActivity.this,FieldDetailsActivity.class);
                                intent1.putExtra("token",token);
                                startActivity(intent1);
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
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Token "+token);
                        return headers;
                    }
                };
                queue.add(objectRequest);
            }
        });
    }
    private void addTokenInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.fasaltech",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("page_no",4);
        myEdit.commit();
    }


}