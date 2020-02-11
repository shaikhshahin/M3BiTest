package com.shahin.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JokesReceiver receiver;
    private List<Model> modelList = new ArrayList<>();
    private MyListAdapter adapter;

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

                Model model = new Model();
                model.setJokes(jokes);
                modelList.add(model);
                adapter = new MyListAdapter(modelList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);

            }
        }


    }

}
