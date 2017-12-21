package com.example.nizam.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        setTitle(getIntent().getStringExtra("bakingName"));
        TextView textView = findViewById(R.id.textView);
        String bakingId = getIntent().getStringExtra("bakingId");
        System.out.println("getIntent().getStringExtra(\"bakingId\").toString(): "+bakingId);
        textView.setText(bakingId);
//        Toast.makeText(this, getIntent().getIntExtra("bakingId",1), Toast.LENGTH_SHORT).show();
    }
}
