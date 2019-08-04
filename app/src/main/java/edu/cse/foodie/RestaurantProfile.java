package edu.cse.foodie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class RestaurantProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);

        Intent intent = getIntent();
        Restaurant restaurant = (Restaurant) intent.getSerializableExtra("profile");

        TextView profileNameText = findViewById(R.id.profileName);
        TextView profileAddressText = findViewById(R.id.profileAddress);
        ImageView profileImage = findViewById(R.id.profileImage);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        profileNameText.setText(restaurant.getName());
        profileAddressText.setText(restaurant.getAddress());
        ratingBar.setRating((float) restaurant.getRating());

        Picasso.get().load(restaurant.getUrl()).into(profileImage);

    }


}
