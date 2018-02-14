package com.example.nizam.bakingapp;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class RecipeDetailsActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        FragmentManager fragmentManager2 = getSupportFragmentManager();
        StepsDetailFragment stepsDetailFragment2 = new StepsDetailFragment();
        stepsDetailFragment2.setBakingId(getIntent().getStringExtra("bakingId"));
        stepsDetailFragment2.setVideoUrl(getIntent().getStringExtra("bakingVideo"));
        stepsDetailFragment2.setStepId(getIntent().getStringExtra("stepId"));
        stepsDetailFragment2.setShortDesc(getIntent().getStringExtra("shortDesc"));
        stepsDetailFragment2.setDesc(getIntent().getStringExtra("desc"));

        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction().add(R.id.step_detail_fragment_container, stepsDetailFragment2);
        fragmentTransaction2.commit();
    }

}
