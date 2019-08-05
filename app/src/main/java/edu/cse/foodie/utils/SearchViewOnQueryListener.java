package edu.cse.foodie.utils;

import android.support.v7.widget.SearchView;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.cse.foodie.CardListAdapter;
import edu.cse.foodie.model.Restaurant;

public class SearchViewOnQueryListener implements SearchView.OnQueryTextListener {
    private static final String TAG = "OnQueryListener";
    private RequestHandler requestHandler;
    private ArrayList<Restaurant> restaurants;
    private ArrayList<Restaurant> allRestaurants;
    private CardListAdapter cardListAdapter;

    public SearchViewOnQueryListener(RequestHandler requestHandler, ArrayList<Restaurant> restaurants, CardListAdapter cardListAdapter) {
        this.requestHandler = requestHandler;
        this.restaurants = restaurants;
        this.cardListAdapter = cardListAdapter;
        this.allRestaurants = new ArrayList<>();

        requestHandler.getAllRestaurants(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
                        SearchViewOnQueryListener.this.allRestaurants.add(restaurant);
                    }
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
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (this.allRestaurants != null || this.allRestaurants.size() > 0) {
            this.cardListAdapter.notifyItemRangeRemoved(0, restaurants.size());
            this.restaurants.clear();
            if (s.trim().isEmpty()) {
                this.restaurants.addAll(allRestaurants);
                this.cardListAdapter.notifyItemRangeInserted(0, allRestaurants.size());
            } else {
                int count = 0;
                for (int i = 0; i < allRestaurants.size(); i++) {
                    Restaurant restaurant = allRestaurants.get(i);
                    if (restaurant.getName().toLowerCase().contains(s) || restaurant.getAddress().toLowerCase().contains(s)) {
                        this.restaurants.add(restaurant);
                        cardListAdapter.notifyItemInserted(count++);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return true;
    }
}
