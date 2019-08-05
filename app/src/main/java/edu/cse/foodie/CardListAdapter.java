package edu.cse.foodie;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Restaurant> mList;

    public CardListAdapter(Context mContext, List<Restaurant> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.resturant_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        Picasso.get()
                .load(mList.get(i).getUrl())
                .resize(200, 200)
                .centerCrop()
                .into(myViewHolder.iconImage);
        myViewHolder.nameText.setText(mList.get(i).getName());
        myViewHolder.addressText.setText(mList.get(i).getAddress());
        myViewHolder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RestaurantProfile.class);
                intent.putExtra("profile", mList.get(i));
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iconImage;
        private TextView nameText;
        private TextView addressText;
        private Button moreBtn;

        MyViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImage);
            nameText = itemView.findViewById(R.id.restaurantNameText);
            addressText = itemView.findViewById(R.id.restaurantAddressText);
            moreBtn = itemView.findViewById(R.id.moreBtn);
        }
    }
}
