package com.example.nizam.bakingapp;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


public class RecipeDetailsActivity extends AppCompatActivity {

    static String SELECTED_RECIPES="Selected_Recipes";
    static String SELECTED_STEPS="Selected_Steps";
    static String SELECTED_INDEX="Selected_Index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String bakingName = getIntent().getStringExtra("bakingName");
        setContentView(R.layout.activity_recipe_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setTitle(bakingName);


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
