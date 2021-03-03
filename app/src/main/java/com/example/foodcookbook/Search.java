package com.example.foodcookbook;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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

public class Search extends AppCompatActivity {
    ImageButton s_search_close;
    EditText s_search_box_input;
    RecyclerView search_items_rec;
    ArrayList<AdapterModel> search_food_array;
    RecycleAdapter mainPageAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        /*s_search_close= findViewById(R.id.s_search_close);
        s_search_box_input= findViewById(R.id.s_search_box_input);
        search_items_rec= findViewById(R.id.search_items_rec);
        search_food_array= new ArrayList<>();

        s_search_box_input.addTextChangedListener(search_textWatcher);
*/

    }

    private final TextWatcher search_textWatcher= new TextWatcher() {
        private Editable s;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @SuppressLint({"ClickableViewAccessibility", "NewApi"})
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String search_text = s_search_box_input.getText().toString();
            search_food_array.clear();

            if (search_text != null && !search_text.isEmpty() && search_text.matches("^[^\\s]+[-a-zA-Z\\s]+([-a-zA-Z^[^ \\n]]+)*$")) {

                Drawable rightSideDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.close);
                assert rightSideDrawable != null;
                rightSideDrawable.setColorFilter(getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_IN);
                s_search_box_input.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, rightSideDrawable, null);
                s_search_box_input.setOnTouchListener((v, event) -> {

                    Drawable rightdrawable = s_search_box_input.getCompoundDrawables()[2];
                    if (rightdrawable != null) {
                        if (event.getRawX() >= (s_search_box_input).getRight() - s_search_box_input.getCompoundDrawables()[2].getBounds().width() - 10) {
                            s_search_box_input.setText("");
                            return true;
                        }
                    }
                    return false;
                });

                searchFoodFromURL(search_text);
            }
            else {
                search_food_array.clear();
                s_search_box_input.setCompoundDrawables(null, null, null, null);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void searchFoodFromURL(String search_text) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiLink.SEARCH_MEAL_BY_NAME+search_text, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        /*if(search_food_array.size() > 0){
                            search_food_array.clear();
                        }*/

                        try {
                            for(int j=0;j<response.getJSONArray("meals").length();j++){
                                String outer_val = response.getJSONArray("meals").getString(j);
                                JSONObject jsonObject = new JSONObject(outer_val);
                                Log.e("search_item", "Searched Meal Name is : " + jsonObject.getString("strMeal"));

                                AdapterModel mainPageModel = new AdapterModel(
                                        jsonObject.getInt("idMeal"),
                                        jsonObject.getString("strMeal"),
                                        jsonObject.getString("strCategory"),
                                        jsonObject.getString("strArea"),
                                        jsonObject.getString("strMealThumb"));

                                if(search_food_array.size() > 0){
                                    for(int i=0;i<search_food_array.size();i++){
                                        if(!search_food_array.get(i).getStrMeal().equals(jsonObject.getString("strMeal"))){
                                            if(i==search_food_array.size()-1){
                                                search_food_array.add(mainPageModel);
                                            }
                                        }
                                    }
                                }
                                else {
                                    search_food_array.add(mainPageModel);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        search_items_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mainPageAdapter = new RecycleAdapter(getApplicationContext(), search_food_array, 1);
                        search_items_rec.setAdapter(mainPageAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley_data", "Error on : " + error.getMessage());
                    }
                });

        queue.add(jsonObjectRequest);
    }
}
