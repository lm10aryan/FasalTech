package com.example.fasaltech.watermelon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fasaltech.MainActivity;
import com.example.fasaltech.R;
import com.example.fasaltech.VolleySingleton;
import com.example.fasaltech.constant.Api;
import com.example.fasaltech.crop_data.CropDataModel;
import com.example.fasaltech.model.ChildOptions;
import com.example.fasaltech.model.ParentQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WatermelonQuestionsActivity extends AppCompatActivity {
    VolleySingleton volleySingleton;
    String token = "74db454e1cf94292d815cd771ebd878df0c7c46e";
    String melonUrl = "http://ec2-52-66-244-191.ap-south-1.compute.amazonaws.com:8000/dumm/";

    RecyclerView rvParent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watermelon_questions);
        volleySingleton = VolleySingleton.getInstance(this);
        rvParent = findViewById(R.id.rvParent);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvParent.setLayoutManager(layoutManager);
        getCropInfo();

    }

    public void getCropInfo() {
        ProgressDialog pd = ProgressDialog.show(this, null, "Please wait", false, false);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Api.watermelonQuestionsUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        // Log.i("Response", response.toString());
                        try {
                            List<ParentQuestion> questionList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("question");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objQuestion = jsonArray.getJSONObject(i);
                                String question = objQuestion.getString("question_text");
                                int qId = objQuestion.getInt("q_id");
                                int mcId = objQuestion.getInt("mc_id");
                                List<ChildOptions> choiceList = new ArrayList<>();
                                JSONArray arrayArr = objQuestion.getJSONArray("choiceAnswers");
                                if (arrayArr.isNull(0)) continue;
                                JSONArray arrayChoice = arrayArr.getJSONArray(0);
                                for (int j = 0; j < arrayChoice.length(); j++) {
                                    JSONObject objChoice = arrayChoice.getJSONObject(j);
                                    choiceList.add(new ChildOptions(objChoice.getInt("choice_id"), objChoice.getString("choice")));
                                }
                                questionList.add(new ParentQuestion(qId, mcId, question, choiceList));

                            }

                            ParentAdapter parentAdapter = new ParentAdapter(questionList);
                            rvParent.setAdapter(parentAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //pd.dismiss();
                        Log.i("TAG", "onErrorResponse: " + error);
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
        volleySingleton.addToRequestQueue(objectRequest);
    }

}