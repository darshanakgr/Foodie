package edu.cse.foodie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RestaurantProfile extends AppCompatActivity {

    private static final String TAG = "RestaurantProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);

        Intent intent = getIntent();
        Restaurant restaurant = (Restaurant) intent.getSerializableExtra("profile");

        final TextView profileNoiseLevel = findViewById(R.id.profileNoiseLevel);
        final TextView profileLightLevel = findViewById(R.id.profileLightLevel);
//        final ProgressBar noiseLevelProgressBar = findViewById(R.id.noiseLevelProgressBar);
//        final ProgressBar lightLevelProgressBar = findViewById(R.id.lightLevelProgressBar);
        TextView profileNameText = findViewById(R.id.profileName);
        TextView profileAddressText = findViewById(R.id.profileAddress);
        ImageView profileImage = findViewById(R.id.profileImage);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        profileNameText.setText(restaurant.getName());
        profileAddressText.setText(restaurant.getAddress());
        ratingBar.setRating((float) restaurant.getRating());

        Picasso.get().load(restaurant.getUrl()).into(profileImage);

        try {
            new RequestHandler(getApplicationContext()).getNoiseLevel(restaurant.getId(), new Response.Listener<JSONObject>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String soundString = response.getString("sound");
                        String lightString = response.getString("light");
                        if (soundString.equals("null")) {
                            profileNoiseLevel.setText("NA");
                        } else {
                            profileNoiseLevel.setText(String.format("%.0fdB", Double.parseDouble(soundString)));
                        }
                        if (lightString.equals("null")) {
                            profileLightLevel.setText("NA");
                        } else {
                            double v = Double.parseDouble(lightString) * 100;
                            profileLightLevel.setText(String.format("%.0f%%", v));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RestaurantProfile.this, "Could not connect to the server", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
