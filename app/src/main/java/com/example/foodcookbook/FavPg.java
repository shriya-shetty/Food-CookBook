package com.example.foodcookbook;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavPg extends AppCompatActivity {
    FavDBHelper favDBHelper;
    ArrayList<Integer> get_all_from_fav= new ArrayList<>();
    RecyclerView fav_rec;
    ArrayList<AdapterModel> get_all_favfood_array = new ArrayList<>();
    RecycleAdapter mainPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favpg);

        favDBHelper= new FavDBHelper(getApplicationContext());
        fav_rec= findViewById(R.id.fav_rec);
        get_all_from_fav= favDBHelper.getAllFavFoods();

        getAllFavItems();
    }

    private void getAllFavItems() {
        for(int i=0;i<get_all_from_fav.size();i++){

            RequestQueue queue= Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.POST, ApiLink.SEARCH_MEAL_DETAILS_BY_ID + get_all_from_fav.get(i), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String fav_outer_val = response.getJSONArray("meals").getString(0);
                                JSONObject favjsonObject = new JSONObject(fav_outer_val);
                                Log.e("Fav_example_item", "Fav Meal Name is : " + favjsonObject.getString("strMeal"));

                                AdapterModel mainPageModel = new AdapterModel(
                                        favjsonObject.getInt("idMeal"),
                                        favjsonObject.getString("strMeal"),
                                        favjsonObject.getString("strCategory"),
                                        favjsonObject.getString("strArea"),
                                        favjsonObject.getString("strMealThumb"));
                                get_all_favfood_array.add(mainPageModel);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            fav_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mainPageAdapter = new RecycleAdapter(getApplicationContext(), get_all_favfood_array, 0);
                            fav_rec.setAdapter(mainPageAdapter);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("FavItems", "Error is : "+error.getMessage());
                        }
                    });

            queue.add(jsonObjectRequest);
        }
    }

}
