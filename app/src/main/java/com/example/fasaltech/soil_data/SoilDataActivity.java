package com.example.fasaltech.soil_data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fasaltech.R;
import com.example.fasaltech.model.SoilDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoilDataActivity extends AppCompatActivity implements ClickListener {
    RecyclerView recyclerView;
    MySoilAdapter mySoilAdapter;
    RequestQueue queue;
    String token="74db454e1cf94292d815cd771ebd878df0c7c46e";
    final String field_data_url ="http://ec2-52-66-244-191.ap-south-1.compute.amazonaws.com:8000/soil-image/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_data);
        Intent intent=getIntent();
        queue= Volley.newRequestQueue(getApplicationContext());
        recyclerView=findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.setLayoutManager();
        mySoilAdapter=new MySoilAdapter(getSoilData(),this);
        recyclerView.setAdapter(mySoilAdapter);
        getSoilInfo();


    }
    public ArrayList<SoilDataModel> getSoilData(){
        ArrayList<SoilDataModel> holder=new ArrayList<>();

        SoilDataModel model=new SoilDataModel();
        model.setHeader("Python");
        model.setDesc("Computer Language");
        model.setId(1);
        //model.setImg(R.drawable.ic_launcher_foreground);
        holder.add(model);

        SoilDataModel model1=new SoilDataModel();
        model1.setHeader("Ruby");
        model1.setDesc("Programming Language");
        model1.setId(4);
        //model1.setImg(R.drawable.ic_launcher_background);
        holder.add(model1);

        SoilDataModel model3=new SoilDataModel();
        model3.setHeader("Java");
        model3.setDesc("Object Oriented Language");
        model3.setId(5);
       // model3.setImg(R.drawable.ic_launcher_background);
        holder.add(model3);
        return holder;
    }
    public void getSoilInfo(){
        JsonArrayRequest arrayRequest=new JsonArrayRequest(
                Request.Method.GET,
                field_data_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i("Response",response.toString());
                            //JSONObject jsonObject=response.getJSONObject(0);
                            for (int i=0;i<response.length();i++){
                                JSONObject jsonObject=response.getJSONObject(i);
                                String soilUrl = jsonObject.getString("soil_picture");
                            }

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
        queue.add(arrayRequest);

    }

    @Override
    public void onClick(SoilDataModel soilDataModel) {
        Log.i("Clicked this",soilDataModel.getHeader());
    }
}