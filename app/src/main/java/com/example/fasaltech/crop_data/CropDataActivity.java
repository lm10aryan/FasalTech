package com.example.fasaltech.crop_data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.fasaltech.R;
import com.example.fasaltech.VolleySingleton;
import com.example.fasaltech.model.SoilDataModel;
import com.example.fasaltech.soil_data.ClickListener;
import com.example.fasaltech.soil_data.MySoilAdapter;
import com.example.fasaltech.soil_data.SoilDataActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CropDataActivity extends AppCompatActivity implements CropClickListener{
    RecyclerView recyclerView;
    MyCropAdapter myCropAdapter;
    VolleySingleton volleySingleton;
    String token;
    int crop_id_chosen=0;
    final String field_data_url ="http://ec2-13-233-44-214.ap-south-1.compute.amazonaws.com:8000/intro-data/2/1/";
    ArrayList<CropDataModel> croparraylist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_data);
        Intent intent=getIntent();
        token=intent.getStringExtra("token");
        volleySingleton= VolleySingleton.getInstance(this);
        recyclerView=findViewById(R.id.recyclerView1);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        myCropAdapter=new MyCropAdapter(croparraylist,this);
        recyclerView.setAdapter(myCropAdapter);
        addTokenInfo();
        getCropInfo();
        Button button=findViewById(R.id.buttonToSoilPage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(crop_id_chosen!=0){
                    Intent intent1=new Intent(CropDataActivity.this, SoilDataActivity.class);
                    intent1.putExtra("product_name",crop_id_chosen);
                    startActivity(intent1);
                }
            }
        });

    }
    public void getCropInfo(){
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

                                CropDataModel model=new CropDataModel();
                                model.setHeader(jsonObject.getString("crop_name"));
                                model.setDesc("Lalala");
                                model.setId(jsonObject.getInt("crop_id"));
                                croparraylist.add(model);
                            }

                            myCropAdapter.notifyDataSetChanged();

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
    public void onClick(CropDataModel cropDataModel) {
        Log.i("Clicked this",cropDataModel.getHeader());
        crop_id_chosen=cropDataModel.getId();
        Log.i("Clicked on",String.valueOf(crop_id_chosen));
    }
    private void addTokenInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.fasaltech",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("page_no",6);
        Log.i("we are in","crop data table");
        myEdit.commit();
    }
}