package edu.cse.foodie;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private RequestHandler requestHandler;
    private ArrayList<Restaurant> restaurants;
    private CardListAdapter cardListAdapter;
    private final String CHANNEL_ID = "visited_locations";
    private LocationManager mLocationManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.setIpAddress("10.10.0.89");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        NotificationChannel visited_location = new NotificationChannel(CHANNEL_ID, "Visited Location", NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(visited_location);

        initService();

        requestHandler = new RequestHandler(getApplicationContext());
        restaurants = new ArrayList<>();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Log.i(TAG, String.format("%f, %f", mLocation.getLatitude(), mLocation.getLongitude()));

        RecyclerView cardListView = findViewById(R.id.restaurantListView);
        cardListAdapter = new CardListAdapter(MainActivity.this, restaurants);
        cardListView.setAdapter(cardListAdapter);
        cardListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        try {
            locateNearbyRestaurants(mLocation);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void initService() {
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(100, new ComponentName(MainActivity.this, MyJobService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setMinimumLatency(10000);
        JobInfo jobInfo = builder.build();
        scheduler.schedule(jobInfo);
        Log.i("BACK_SERVICE", "SCHEDULED");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchViewOnQueryListener(requestHandler, restaurants, cardListAdapter));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                @SuppressLint("MissingPermission")
                Location mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                try {
                    locateNearbyRestaurants(mLocation);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public void locateNearbyRestaurants(Location mLocation) throws JSONException {
        requestHandler.getNearbyRestaurantsByLocation(mLocation, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cardListAdapter.notifyItemRangeRemoved(0, restaurants.size());
                restaurants.clear();
                try {
                    JSONArray resList = response.getJSONArray("restaurant");
                    for (int i = 0; i < resList.length(); i++) {
                        JSONObject restaurantJSON = resList.getJSONObject(i);
                        Restaurant restaurant = new Restaurant();
                        restaurant.setId(restaurantJSON.getString("_id"));
                        restaurant.setName(restaurantJSON.getString("name"));
                        restaurant.setAddress(restaurantJSON.getString("address"));
                        restaurant.setUrl(restaurantJSON.getString("image"));
                        restaurant.setRating(restaurantJSON.getDouble("rating"));
                        restaurant.setLatitude(restaurantJSON.getDouble("latitude"));
                        restaurant.setLongitude(restaurantJSON.getDouble("longitude"));
                        restaurants.add(restaurant);
                        cardListAdapter.notifyItemInserted(i);

                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Could not connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
