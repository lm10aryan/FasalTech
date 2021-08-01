package com.example.fasaltech;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import org.jetbrains.annotations.NotNull;
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
        Log.i("Token",token);

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
                        JsonObjectRequest objectRequest=new JsonObjectRequest(
                                Request.Method.POST,
                                Api.product_data_url,
                                productDetailsObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        addCropId();
                                        Intent intent1=new Intent(ProductDetailsActivity.this, WatermelonQuestionsActivity.class);
                                        intent1.putExtra("token",token);
                                        intent1.putExtra("crop_id",crop_id_chosen);
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void addCropId(){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.fasaltech",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("crop_id",crop_id_chosen);
        myEdit.commit();
    }

}


/*
 //RequestQueue queue;
    String latitude="";
    String longitude="";
    LocationManager locationManager;
    LocationListener locationListener;
    boolean savedLocationInfo=false;
above onCreate -
 @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

Below all functions-

    public void getLocationPoints(){
        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude=String.valueOf(location.getLatitude());
                longitude=String.valueOf(location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }
    public void sendLocationInfo(){
        if(latitude.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please switch your location services",Toast.LENGTH_SHORT).show();
        }else {
            JSONObject locationDetailsObject=new JSONObject();
            try {
                locationDetailsObject.put("latitude",latitude);
                locationDetailsObject.put("longitude",longitude);
                JsonObjectRequest objectRequest=new JsonObjectRequest(
                        Request.Method.POST,
                        Api.post_location_url,
                        locationDetailsObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("Location Info","Saved");
                                savedLocationInfo=true;
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Error at Location",error.toString());
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
 */