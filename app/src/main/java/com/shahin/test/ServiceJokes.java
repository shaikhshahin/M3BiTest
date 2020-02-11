package com.shahin.test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ServiceJokes extends Service {
    Context context;
    RequestQueue queue;
    List<String> jokesList ;
    String MainJokes;


    public ServiceJokes() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

            AsyncTaskExample jokes = new AsyncTaskExample();
            jokes.execute();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private class AsyncTaskExample extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... strings) {

            String urlPath = Constants.URL;



            for (int i = 0; i <=20; i++) {
                jokesList = new ArrayList<>();

                RequestQueue queue = Volley.newRequestQueue(ServiceJokes.this); // this = context
                final int finalI = i;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, urlPath, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject json = response.getJSONObject(Constants.RESPONSE_VALUE);

                                    String jokes = json.getString(Constants.RESPONSE_JOKE);

                                    MainJokes=jokes;

                                    jokesList.add(MainJokes);


                                        sendDataToActivity(MainJokes);


                                    Log.e("List", jokesList.toString());
                                    Log.e("ListSize", String.valueOf(jokesList.size()));



                                    Log.e("Response", jokes);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error

                            }
                        });

                queue.add(jsonObjectRequest);


            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }



    private void sendDataToActivity(String jokes) {
        Intent sendLevel = new Intent();
        sendLevel.setAction(Constants.BROADCAST_ACTION);
        sendLevel.putExtra(Constants.BROADCAST_JOKE, jokes);
        sendLevel.putStringArrayListExtra("Shahin", (ArrayList<String>) jokesList);
        sendBroadcast(sendLevel);

    }
}
