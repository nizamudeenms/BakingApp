package com.example.nizam.bakingapp;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


public class RecipeDetailsActivity extends AppCompatActivity {
    StepsDetailFragment stepsDetailFragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String bakingName = getIntent().getStringExtra("bakingName");
        setContentView(R.layout.activity_recipe_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setTitle(bakingName);


        FragmentManager fragmentManager2 = getSupportFragmentManager();
        stepsDetailFragment2 = new StepsDetailFragment();
        stepsDetailFragment2.setBakingId(getIntent().getStringExtra("bakingId"));
        stepsDetailFragment2.setVideoUrl(getIntent().getStringExtra("bakingVideo"));
        stepsDetailFragment2.setStepId(getIntent().getStringExtra("stepId"));
        stepsDetailFragment2.setShortDesc(getIntent().getStringExtra("shortDesc"));
        stepsDetailFragment2.setDesc(getIntent().getStringExtra("desc"));


        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction().add(R.id.step_detail_fragment_container, stepsDetailFragment2);
            fragmentTransaction2.commit();
        } else {
            stepsDetailFragment2 = (StepsDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, "stepsDetailFragment");
        }
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
}
