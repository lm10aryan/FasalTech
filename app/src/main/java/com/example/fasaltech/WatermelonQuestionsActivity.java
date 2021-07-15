package com.example.fasaltech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fasaltech.constant.Api;
import com.example.fasaltech.crop_data.CropDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WatermelonQuestionsActivity extends AppCompatActivity {
    VolleySingleton volleySingleton;
    String token="74db454e1cf94292d815cd771ebd878df0c7c46e";
    String melonUrl="http://ec2-52-66-244-191.ap-south-1.compute.amazonaws.com:8000/dumm/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watermelon_questions);
        Intent intent=getIntent();
        volleySingleton= VolleySingleton.getInstance(this);
        getCropInfo();

    }
    public void getCropInfo(){

        JsonObjectRequest objectRequest=new JsonObjectRequest(
                Request.Method.GET,
                Api.watermelonQuestionsUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Response",response.toString());
                        try {
                            JSONArray jsonArray=response.getJSONArray("question");
                            //Log.i("Response",jsonArray.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
        volleySingleton.addToRequestQueue(objectRequest);
    }

}