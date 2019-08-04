package edu.cse.foodie;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initService();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Utils.getUrl("location/all-restaurant");

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Restaurant> restaurants = new ArrayList<>();
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject restaurantJSON = arr.getJSONObject(i);
                                Restaurant restaurant = new Restaurant();
                                restaurant.setId(restaurantJSON.getString("id"));
                                restaurant.setName(restaurantJSON.getString("name"));
                                restaurant.setAddress(restaurantJSON.getString("address"));
                                restaurant.setUrl(restaurantJSON.getString("image"));
                                restaurant.setRating(restaurantJSON.getDouble("rating"));
                                restaurant.setLatitude(restaurantJSON.getDouble("latitude"));
                                restaurant.setLongitude(restaurantJSON.getDouble("longitude"));
                                restaurants.add(restaurant);
                            }
                            RecyclerView cardListView = findViewById(R.id.restaurantListView);
                            CardListAdapter cardListAdapter = new CardListAdapter(MainActivity.this, restaurants);
                            cardListView.setAdapter(cardListAdapter);
                            cardListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        requestQueue.add(stringRequest);

//        SearchView search = (SearchView) findViewById(R.id.searchText);
//        String data[]={"Emmanuel", "Olayemi", "Henrry", "Mark", "Steve", "Ayomide", "David", "Anthony", "Adekola", "Adenuga"};
//        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, data);
//        searchAutoComplete.setAdapter(dataAdapter);
    }

    private void initService() {
        JobScheduler schedular = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(100, new ComponentName(MainActivity.this, MyJobService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setMinimumLatency(10000);
        JobInfo jobInfo = builder.build();
        schedular.schedule(jobInfo);
        Log.i("BACK_SERVICE", "SCHEDULED");
    }
}
