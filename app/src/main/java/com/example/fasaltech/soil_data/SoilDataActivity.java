package com.example.fasaltech.soil_data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.fasaltech.ProductDetailsActivity;
import com.example.fasaltech.R;
import com.example.fasaltech.VolleySingleton;
import com.example.fasaltech.crop_data.CropDataActivity;
import com.example.fasaltech.model.SoilDataModel;
import com.example.fasaltech.seed_data.SeedDataActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoilDataActivity extends AppCompatActivity implements ClickListener {
    RecyclerView recyclerView;
    MySoilAdapter mySoilAdapter;
    VolleySingleton volleySingleton;
    String token;
    final String field_data_url ="http://ec2-3-109-139-249.ap-south-1.compute.amazonaws.com:8000/intro-data/1/1/";
    ArrayList<SoilDataModel> soilarrayList=new ArrayList<>();
    int crop_id_chosen;
    int soil;
    String crop_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_data);
        Intent intent=getIntent();
        crop_id_chosen=intent.getIntExtra("product_name",0);
        crop_name=intent.getStringExtra("crop_name");
        token=intent.getStringExtra("token");
        volleySingleton= VolleySingleton.getInstance(this);
        recyclerView=findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.setLayoutManager();
        mySoilAdapter=new MySoilAdapter(soilarrayList,this);
        recyclerView.setAdapter(mySoilAdapter);
        getSoilInfo();
        Button button=findViewById(R.id.buttonToSeedPage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(soil!=0){
                    if(crop_id_chosen==1){
                        Intent intent1=new Intent(SoilDataActivity.this, SeedDataActivity.class);
                        intent1.putExtra("product_name",crop_id_chosen);
                        intent1.putExtra("soil",soil);
                        intent1.putExtra("crop_name",crop_name);
                        intent1.putExtra("token",token);
                        startActivity(intent1);
                    }
                    else {
                        Intent intent1=new Intent(SoilDataActivity.this, ProductDetailsActivity.class);
                        intent1.putExtra("product_name",crop_id_chosen);
                        intent1.putExtra("soil",soil);
                        intent1.putExtra("seed_type",2);
                        intent1.putExtra("crop_name",crop_name);
                        intent1.putExtra("token",token);
                        startActivity(intent1);
                    }

                }
            }
        });

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

                                SoilDataModel model=new SoilDataModel();
                                model.setHeader(jsonObject.getString("soil_type"));
                                model.setDesc("Computer Language");
                                model.setId(jsonObject.getInt("soil_id"));
                                soilarrayList.add(model);
                            }

                            mySoilAdapter.notifyDataSetChanged();

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
        volleySingleton.addToRequestQueue(arrayRequest);

    }

    @Override
    public void onClick(SoilDataModel soilDataModel) {
        Log.i("Clicked this",soilDataModel.getHeader());
        soil=soilDataModel.getId();
    }

}