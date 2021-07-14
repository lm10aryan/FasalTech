package com.example.fasaltech;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue queue;
    private static Context mContext;


    private VolleySingleton(Context context){
        queue= Volley.newRequestQueue(context.getApplicationContext());
        mContext=context;

    }
    public static synchronized VolleySingleton getInstance(Context context){
        if(mInstance==null){
            mInstance=new VolleySingleton(context);

        }
        return mInstance;
    }
    public RequestQueue getQueue(){
        return queue;
    }
    public<T> void addToRequestQueue(Request<T> request){
        // Add the specified request to the request queue
        getQueue().add(request);
    }

}
