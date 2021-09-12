package com.example.fasaltech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fasaltech.constant.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity {
    private static final int LOCATION_SETTINGS_REQUEST =4 ;
    String token;
    TextView homePageTextView;
    VolleySingleton volleySingleton;
    String latitude="";
    String longitude="";
    LocationManager locationManager;
    LocationListener locationListener;
    boolean savedLocationInfo=false;
    TextView waterTextView;
    TextView fertTextView;
    TextView microTextView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Intent intent=getIntent();
        token=intent.getStringExtra("token");
        if(token.isEmpty()){
            Log.i("Empty","token");
        }
        volleySingleton= VolleySingleton.getInstance(this);
        waterTextView=findViewById(R.id.waterText);
        fertTextView=findViewById(R.id.fertText);
        microTextView=findViewById(R.id.micronutrientText);
        addTokenInfo(token);
        homePageTextView=findViewById(R.id.homePageTextView);
        homePageTextView.setText("FasalTech Welcomes You!");
        createLocationRequest();
        getLocationPoints();
        getWaterInfo();
        getFertiliserInfo();
        getMicronutrientInfo();
        addMainUserInfo();

    }
    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {


            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        HomePageActivity.this,
                                        LOCATION_SETTINGS_REQUEST);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });
    }
    private void addTokenInfo(String token){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.fasaltech",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("page_no",12);
        myEdit.putString("token",token);
        Log.i("Saved","token at login endpoint");
        myEdit.commit();
    }
    public void getLocationPoints(){
        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude=String.valueOf(location.getLatitude());
                longitude=String.valueOf(location.getLongitude());
                Log.i("Latitude",latitude);
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
    public void getPersonalWeather(View view){
        JsonObjectRequest objectRequest=new JsonObjectRequest(
                Request.Method.GET,
                Api.weather_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Response",response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error",error.toString());
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
    public void addLocation(View view){

        new AlertDialog.Builder(this)
                .setTitle("Add Location")
                .setMessage("Are you standing on your field?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"great job",Toast.LENGTH_SHORT).show();
                        addLocationInfo();

                    }
                })
                .setNegativeButton(android.R.string.no,null)
                .setIcon(R.drawable.ic_launcher_background)
                .show();

    }

    public void getWaterInfo(){
        JsonArrayRequest arrayRequest=new JsonArrayRequest(
                Request.Method.GET,
                Api.get_water_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Response",response.toString());
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonObject=response.getJSONObject(i);
                                String micro_text=jsonObject.getString("message");
                                String metric=jsonObject.getString("metric");
                                String quantity=jsonObject.getString("quantity");

                                waterTextView.setText(micro_text +" "+quantity+" "+metric);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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
        volleySingleton.addToRequestQueue(arrayRequest);
    }
    public void getFertiliserInfo(){
        JsonArrayRequest arrayRequest=new JsonArrayRequest(
                Request.Method.GET,
                Api.get_fertiliser_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.i("Fertiliser",jsonArray.toString());
                        for(int i=0;i<jsonArray.length();i++){
                            try {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                if(jsonObject.get("quantity").equals("")){
                                    Toast.makeText(getApplicationContext(),"no fert",Toast.LENGTH_SHORT).show();
                                    fertTextView.setText(jsonObject.getString("fert_name"));
                                }else{
                                    String fert_text=jsonObject.getString("fert_name");
                                    String metric="";
                                    String quantity=jsonObject.getString("quantity");
                                    if(jsonObject.getString("metric").equals("0")){
                                        metric="grams";
                                    }else{
                                        metric="ml";
                                    }
                                    fertTextView.setText(fert_text +" "+quantity+" "+metric);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("Error",volleyError.toString());
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
        volleySingleton.addToRequestQueue(arrayRequest);
    }
    public void getMicronutrientInfo(){
        JsonArrayRequest arrayRequest=new JsonArrayRequest(
                Request.Method.GET,
                Api.get_micronutrient_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.i("Micronutrient",jsonArray.toString());
                        for(int i=0;i<jsonArray.length();i++){
                            try {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                if(jsonObject.get("quantity").equals("")){
                                    Toast.makeText(getApplicationContext(),"no micronutrient",Toast.LENGTH_SHORT).show();
                                    microTextView.setText(jsonObject.getString("fert_name"));
                                }else{
                                    String micro_text=jsonObject.getString("fert_name");
                                    String metric="";
                                    String quantity=jsonObject.getString("quantity");
                                    if(jsonObject.getString("metric").equals("0")){
                                        metric="grams";
                                    }else{
                                        metric="ml";
                                    }
                                    microTextView.setText(micro_text +" "+quantity+" "+metric);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("Error",volleyError.toString());
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
        volleySingleton.addToRequestQueue(arrayRequest);
    }
    public void addLocationInfo(){
        JSONObject locationDetailsObject=new JSONObject();
        try {
            Double latitude_number=Double.parseDouble(latitude);
            Double longitude_number=Double.parseDouble(longitude);


            locationDetailsObject.put("latitude",String.format("%.2f",latitude_number));
            locationDetailsObject.put("longitude",String.format("%.2f",longitude_number));
            Log.i("Details",locationDetailsObject.toString());
            JsonObjectRequest objectRequest=new JsonObjectRequest(
                    Request.Method.POST,
                    Api.post_location_url,
                    locationDetailsObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(),"Added location Info",Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Log.i("Location Posting error",error.toString());
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
            volleySingleton.addToRequestQueue(objectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addMainUserInfo(){
        JsonObjectRequest objectRequest=new JsonObjectRequest(
                Request.Method.POST,
                Api.post_main_info_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),"Main Info added",Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error at main info",error.getLocalizedMessage());
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




