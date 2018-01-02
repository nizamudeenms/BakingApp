package com.example.nizam.bakingapp;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;


public class RecipeDetailsActivity extends FragmentActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//
//
//        System.out.println(" Screen laypout : "+ getResources().getConfiguration().smallestScreenWidthDp);
//
//

//        if(getResources().getConfiguration().smallestScreenWidthDp > 600){
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            StepsFragment stepsFragment = new StepsFragment();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().add(R.id.fragment_container,stepsFragment);
//            fragmentTransaction.commit();
//
        System.out.println(" before Details activity ");

        System.out.println("getIntent().getStringExtra(\" bakingId\")"+ getIntent().getStringExtra("bakingId"));
        System.out.println("getIntent().getStringExtra(\" bakingId\")"+ getIntent().getStringExtra("bakingVideo"));
        System.out.println("getIntent().getStringExtra(\" bakingId\")"+ getIntent().getStringExtra("stepId"));
        System.out.println("getIntent().getStringExtra(\" bakingId\")"+ getIntent().getStringExtra("bakingName"));
        System.out.println("getIntent().getStringExtra(\" bakingId\")"+ getIntent().getStringExtra("shortDesc"));
        System.out.println("getIntent().getStringExtra(\" bakingId\")"+ getIntent().getStringExtra("desc"));
            FragmentManager fragmentManager2 = getSupportFragmentManager();
            StepsDetailFragment stepsDetailFragment2 = new StepsDetailFragment();
            stepsDetailFragment2.setBakingId(getIntent().getStringExtra("bakingId"));
            stepsDetailFragment2.setVideoUrl(getIntent().getStringExtra("bakingVideo"));
            stepsDetailFragment2.setStepId(getIntent().getStringExtra("stepId"));
            stepsDetailFragment2.setShortDesc(getIntent().getStringExtra("shortDesc"));
            stepsDetailFragment2.setDesc(getIntent().getStringExtra("desc"));


            FragmentTransaction fragmentTransaction2= fragmentManager2.beginTransaction().add(R.id.step_detail_fragment_container,stepsDetailFragment2);
            fragmentTransaction2.commit();
        System.out.println(" after Details activity ");

//
//        }else{
//
//        if(findViewById(R.id.tablet_layout) != null) {
//            System.out.println(" inside table layout");
//            FragmentManager fragmentManager = getSupportFragmentManager();
//
//            StepsFragment stepsFragment = new StepsFragment();
//            fragmentManager.beginTransaction().add(R.id.fragment_container, stepsFragment).commit();
//
//            StepsDetailFragment stepsDetailFragment = new StepsDetailFragment();
//            fragmentManager.beginTransaction().add(R.id.step_detail_fragment_container,stepsDetailFragment).commit();
//        }else{
//            System.out.println(" inside normal layout");
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            StepsFragment stepsFragment = new StepsFragment();
//            fragmentTransaction.add(R.id.fragment_container, stepsFragment);
//            fragmentTransaction.commit();
//        }
//        }

//        fragmentManager.beginTransaction().add(R.id.fragment_container, mRecipeFragment).commit();

        }



}
