package com.example.foodcookbook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ListViewHolder> {
    Context context;
    ArrayList<AdapterModel> all_random_food= new ArrayList<>();
    FavDBHelper favDBHelper;
    int pos;

    public RecycleAdapter(Context applicationContext, ArrayList<AdapterModel> get_all_favfood_array, int i) {
        all_random_food= get_all_favfood_array;
        pos=i;
        context= applicationContext;
    }

    @NonNull
    @Override
    public RecycleAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemslist, null);
        return new RecycleAdapter.ListViewHolder(inflate);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.ListViewHolder holder, int position) {

        if(pos==0){
            holder.mpage_item_fav.setVisibility(View.VISIBLE);
        }
        else if(pos==1){
            holder.mpage_item_fav.setVisibility(View.GONE);
        }

        favDBHelper= new FavDBHelper(context);

        if(favDBHelper.checkSpecficFood(all_random_food.get(position).getIdMeal()) > 0){
            holder.mpage_item_fav.setImageTintList(ColorStateList.valueOf(Color.RED));
        }
        else {
            holder.mpage_item_fav.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D8D8D8")));
        }

        holder.mpage_food_name.setText("Name:    "+all_random_food.get(position).getStrMeal());
        holder.mpage_food_cat_name.setText("Category:    "+all_random_food.get(position).getStrCategory());
        holder.mpage_food_area_name.setText("Area:    "+all_random_food.get(position).getStrArea());
        Glide.with(context)
                .load(all_random_food.get(position).getStrMealThumb())
                .into(holder.mpage_food_img);

        holder.mpage_main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("meal_id", all_random_food.get(position).getIdMeal());
                context.startActivity(intent);
            }
        });

        holder.mpage_item_fav.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(favDBHelper.checkSpecficFood(all_random_food.get(position).getIdMeal()) > 0){
                    if(favDBHelper.removeFavFood(all_random_food.get(position).getIdMeal())){
                        Log.e("Fav_action","Item removed");
                        holder.mpage_item_fav.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D8D8D8")));
                    }
                }
                else {
                    if(favDBHelper.addFavFood(all_random_food.get(position).getIdMeal())){
                        Log.e("Fav_action","Item added");
                        holder.mpage_item_fav.setImageTintList(ColorStateList.valueOf(Color.RED));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return all_random_food.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        View mpage_main_container;
        ImageView mpage_food_img;
        TextView mpage_food_name, mpage_food_cat_name, mpage_food_area_name;
        ImageButton mpage_item_fav;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            mpage_main_container= itemView.findViewById(R.id.mpage_main_container);
            mpage_food_img= itemView.findViewById(R.id.mpage_food_img);
            mpage_food_name= itemView.findViewById(R.id.mpage_food_name);
            mpage_food_cat_name= itemView.findViewById(R.id.mpage_food_cat_name);
            mpage_food_area_name= itemView.findViewById(R.id.mpage_food_area_name);
            mpage_item_fav= itemView.findViewById(R.id.favourite);

        }
    }
}

