package com.shahin.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JokesReceiver receiver;
    private MyListAdapter adapter;
    List<String> jokesList = new ArrayList<>();
    List<String> jokesL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // startService(new Intent(this, JokesService.class));
        recyclerView = findViewById(R.id.recycleJokes);

        Intent intent = new Intent(this, ServiceJokes.class);
        startService(intent);

        receiver = new JokesReceiver();
        registerReceiver(receiver, new IntentFilter(Constants.BROADCAST_ACTION));  //<----Register

        jokesL = new ArrayList<>();




    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    class JokesReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Constants.BROADCAST_ACTION)) {
                String jokes = intent.getStringExtra(Constants.BROADCAST_JOKE);
                jokesList = intent.getStringArrayListExtra("Shahin");
                Log.e("MainActivity", jokesList.toString());


                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                adapter = new MyListAdapter(jokesList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));




            }
        }


    }

}