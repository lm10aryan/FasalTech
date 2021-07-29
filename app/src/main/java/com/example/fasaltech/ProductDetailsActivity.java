package com.example.fasaltech;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fasaltech.constant.Api;
import com.example.fasaltech.watermelon.WatermelonQuestionsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {
    int crop_id_chosen;
    int soil;
    int seed_type;
    String crop_name;
    String date_selected;
    VolleySingleton volleySingleton;
    String token;
    JSONObject productDetailsObject;
    //RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Intent intent=getIntent();
        volleySingleton= VolleySingleton.getInstance(this);
       // queue= Volley.newRequestQueue(this);

        crop_id_chosen=intent.getIntExtra("product_name",0);
        soil=intent.getIntExtra("soil",0);
        seed_type=intent.getIntExtra("seed_type",0);
        crop_name=intent.getStringExtra("crop_name");
        token=intent.getStringExtra("token");

        TextView questionProductText=findViewById(R.id.questionProductText);
        String questionText="How many acres are you growing ";
        questionProductText.setText(questionText+crop_name+" ?");
        EditText editTextNumber=findViewById(R.id.editTextNumber);
        Button submitProductDetails=findViewById(R.id.submitProductDetails);

        CalendarView calendarView=findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date_selected=String.valueOf(i)+"-"+String.valueOf(i1)+"-"+String.valueOf(i2);
            }
        });

        submitProductDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String acres_number=editTextNumber.getText().toString().trim();
                if(!acres_number.isEmpty() && !date_selected.isEmpty()){
                    productDetailsObject=new JSONObject();
                    try {
                        productDetailsObject.put("product_name",crop_id_chosen);
                        productDetailsObject.put("soil",soil);
                        productDetailsObject.put("seed_type",seed_type);
                        productDetailsObject.put("date_of_start",date_selected);
                        productDetailsObject.put("acres",Integer.valueOf(acres_number));
                        Log.i("Product Details",productDetailsObject.toString());
                        StringRequest stringRequest=new StringRequest(
                                Api.product_data_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.i("Success","Yes");
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.i("error",error.toString());
                                    }
                                }){
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> stringMap=new HashMap<>();
                                stringMap.put("product_name",String.valueOf(crop_id_chosen));
                                stringMap.put("soil",String.valueOf(soil));
                                stringMap.put("seed_type",String.valueOf(seed_type));
                                stringMap.put("date_of_start",date_selected);
                                stringMap.put("acres",String.valueOf(acres_number));
                                return stringMap;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("Content-Type","application/json");
                                params.put("Authorization", "Token "+token);
                                return params;
                            }
                        };
                        volleySingleton.addToRequestQueue(stringRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void sendProductDetails(JSONObject jsonObject){
        //Log.i("Data",jsonObject.toString());
        JsonObjectRequest objectRequest=new JsonObjectRequest(
                Request.Method.POST,
                Api.product_data_url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent1=new Intent(ProductDetailsActivity.this, WatermelonQuestionsActivity.class);
                        intent1.putExtra("token",token);
                        startActivity(intent1);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Error",error.toString());
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
        volleySingleton.addToRequestQueue(objectRequest);
    }
}


   /* Map<String,String> stringMap=new HashMap<>();
                                stringMap.put("product_name",String.valueOf(crop_id_chosen));
                                        stringMap.put("soil",String.valueOf(soil));
                                        stringMap.put("seed_type",String.valueOf(seed_type));
                                        stringMap.put("date_of_start",date_selected);
                                        stringMap.put("acres",String.valueOf(acres_number));  */