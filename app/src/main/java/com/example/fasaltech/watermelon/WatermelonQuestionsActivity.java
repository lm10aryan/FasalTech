package com.example.fasaltech.watermelon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WatermelonQuestionsActivity extends AppCompatActivity implements WatermelonMainClickListener {
    VolleySingleton volleySingleton;
    String token;
    String melonUrl = "http://ec2-52-66-244-191.ap-south-1.compute.amazonaws.com:8000/dumm/";
    RecyclerView rvParent;
    int subq_id;
    int subc_id;
    List<Integer>parentAskedList=new LinkedList<>();
    List<Integer>childAskedList=new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watermelon_questions);
        Intent intent=getIntent();
        addTokenInfo();
        token=intent.getStringExtra("token");
        volleySingleton = VolleySingleton.getInstance(this);
        rvParent = findViewById(R.id.rvParent);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvParent.setLayoutManager(layoutManager);
        getCropInfo();
        Button button=findViewById(R.id.submitWatermelonAnswers);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
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
                        Log.i("Response", response.toString());
                        try {
                            List<ParentQuestion> questionList = new ArrayList<>();
                            List<ParentQuestion> subQuestionList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("question");
                            Log.i("Response", response.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objQuestion = jsonArray.getJSONObject(i);
                                String question = objQuestion.getString("question_text");
                                int qId = objQuestion.getInt("q_id");
                                int mcId = objQuestion.getInt("mc_id");
                                if (qId == 6) continue;
                                if (objQuestion.isNull("sub_id")) {
                                    subq_id = 0;
                                    subc_id = 0;

                                } else {
                                    subq_id = objQuestion.getInt("sub_id");
                                    subc_id = objQuestion.getInt("sub_choice");
                                }
                                List<ChildOptions> choiceList = new ArrayList<>();
                                JSONArray arrayArr = objQuestion.getJSONArray("choiceAnswers");
                                //Log.i("value",arrayArr.toString());
                                if (arrayArr.isNull(0)) {
                                    choiceList.add(new ChildOptions(100, "Haha"));
                                    //Log.i("found it","Done");
                                    //questionList.add(new ParentQuestion(qId, mcId, question, choiceList, subq_id, subc_id, false));
                                    subQuestionList.add(new ParentQuestion(qId, mcId, question, choiceList, subq_id, subc_id));
                                    continue;
                                }
                                JSONArray arrayChoice = arrayArr.getJSONArray(0);
                                for (int j = 0; j < arrayChoice.length(); j++) {
                                    JSONObject objChoice = arrayChoice.getJSONObject(j);
                                    choiceList.add(new ChildOptions(objChoice.getInt("choice_id"), objChoice.getString("choice")));
                                }

                                questionList.add(new ParentQuestion(qId, mcId, question, choiceList, subq_id, subc_id));
                            }
                            // Log.i("Count",String.valueOf(questionList.size()));
                            ParentAdapter parentAdapter = new ParentAdapter(questionList, subQuestionList, (WatermelonQuestionsActivity.this::onClick));
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

    @Override
    public void onClick(ParentQuestion parentQuestion) {
        //Log.i("Question clicked user", parentQuestion.getQuestionText());
        //check if question has been answered already
        boolean flag=true;
        for(int j=0;j<parentAskedList.size();j++){
            if(parentQuestion.getQId()==parentAskedList.get(j)){
                for(int k=0;k<parentQuestion.getChildOptions().size();k++){
                    if(parentQuestion.getChildOptions().get(k).isSelected()){
                        Log.i("Same question","Solved");
                        childAskedList.set(j,parentQuestion.getChildOptions().get(k).getChoiceId());
                        flag=false;
                    }
                }
            }
            else {

            }
        }
        //add question id and choice id if the question has not been answered yet
        if(flag){
            parentAskedList.add(parentQuestion.getQId());
            for(int i=0;i<parentQuestion.getChildOptions().size();i++){
                if(parentQuestion.getChildOptions().get(i).isSelected()){
                    childAskedList.add(parentQuestion.getChildOptions().get(i).getChoiceId());
                    //Log.i("Option clicked by user",parentQuestion.getChildOptions().get(i).getChoice());
                }
            }
        }

    }
    private void addTokenInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.fasaltech",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("page_no",10);
        Log.i("we are in","watermelon question table");
        myEdit.commit();
    }

    public void sendRequest(){
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<parentAskedList.size();i++){
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("question_id",parentAskedList.get(i));
                jsonObject.put("choice_id",childAskedList.get(i)) ;
                jsonArray.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JsonArrayRequest arrayRequest=new JsonArrayRequest(
                Request.Method.POST,
                Api.post_answers_url,
                jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Success","Kudos");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Erorr",error.toString());
                    }
                }){
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
}