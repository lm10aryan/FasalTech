package com.example.fasaltech.seed_data;

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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.fasaltech.ProductDetailsActivity;
import com.example.fasaltech.R;
import com.example.fasaltech.VolleySingleton;
import com.example.fasaltech.crop_data.CropDataModel;
import com.example.fasaltech.crop_data.MyCropAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeedDataActivity extends AppCompatActivity implements SeedClickListener {
    RecyclerView recyclerView;
    MySeedAdapter mySeedAdapter;
    VolleySingleton volleySingleton;
    String token;
    final String field_data_url = "http://ec2-13-233-44-214.ap-south-1.compute.amazonaws.com:8000/intro-data/3/1/";
    ArrayList<SeedDataModel> seedarraylist = new ArrayList<>();
    int crop_id_chosen;
    int soil;
    int seed_type=0;
    String crop_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed_data);
        Intent intent = getIntent();
        crop_id_chosen=intent.getIntExtra("product_name",0);
        soil=intent.getIntExtra("soil",0);
        crop_name=intent.getStringExtra("crop_name");
        token=intent.getStringExtra("token");
        volleySingleton = VolleySingleton.getInstance(this);
        recyclerView = findViewById(R.id.seedRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        mySeedAdapter = new MySeedAdapter(seedarraylist, this);
        recyclerView.setAdapter(mySeedAdapter);
        getSeedInfo();
        Button button=findViewById(R.id.buttonToProductInfoPage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seed_type!=0){
                    Intent intent1=new Intent(SeedDataActivity.this, ProductDetailsActivity.class);
                    intent1.putExtra("product_name",crop_id_chosen);
                    intent1.putExtra("soil",soil);
                    intent1.putExtra("seed_type",seed_type);
                    intent1.putExtra("crop_name",crop_name);
                    intent1.putExtra("token",token);
                    startActivity(intent1);
                }
            }
        });

    }

    public void getSeedInfo() {
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                field_data_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i("Response", response.toString());
                            //JSONObject jsonObject=response.getJSONObject(0);
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                SeedDataModel model = new SeedDataModel();
                                model.setHeader(jsonObject.getString("seed_name"));
                                model.setDesc("Lalala");
                                model.setId(jsonObject.getInt("seed_id"));
                                seedarraylist.add(model);
                            }

                            mySeedAdapter.notifyDataSetChanged();

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
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Token " + token);
                return headers;
            }
        };
        volleySingleton.addToRequestQueue(arrayRequest);

    }

    @Override
    public void onClick(SeedDataModel seedDataModel) {
        Log.i("Clicked this", seedDataModel.getHeader());
        seed_type=seedDataModel.getId();
    }
}