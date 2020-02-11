package com.shahin.test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class JokesService extends Service {
    Context context;
    public JokesService() {
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
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
    private class AsyncTaskExample extends AsyncTask<String, Integer, String> {



        @Override
        protected String doInBackground(String... strings) {

            String urlPath = "http://api.icndb.com/jokes/random";
            String chunks = null;


            try {
                java.net.URL url = new URL(urlPath);
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    JsonObject json = new JsonObject();
                    //json= json.getAsJsonObject("value");
                    String jokes = String.valueOf(json.get("joke"));
                    Log.e("Joke", jokes);


                    while ((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                        Log.e("SHAHIN", dta.toString());
                        sendDataToActivity(dta.toString());

                    }
                } else {
                    //Handle else
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return chunks;
        }
    }

    private void sendDataToActivity( String jokes)
    {
        Intent sendLevel = new Intent();
        sendLevel.setAction("GET_JOKES");
        sendLevel.putExtra( "Jokes", jokes);
        sendBroadcast(sendLevel);

    }



}
