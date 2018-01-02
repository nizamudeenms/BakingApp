package com.example.nizam.bakingapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

public class RecipeStepsActivity extends AppCompatActivity implements StepsFragment.OnStepClickListener {

    private boolean mTwoPane;
    private ArrayList<String> ingredientsArray = new ArrayList<String>();
    private ArrayList<String> stepsDescArray = new ArrayList<String>();
    private ArrayList<BakingSteps> tempStepsArray = new ArrayList<BakingSteps>();
    private SQLiteDatabase mBakingDB;
    BakingSteps bakingStepsObj;
    String bakingName;
    String bakingId;
    String stepId;
    String videoUrl;
    String shortDesc;
    String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);
        bakingName = getIntent().getStringExtra("bakingName");
        bakingId = getIntent().getStringExtra("bakingId");
//        stepId = getIntent().getStringExtra("stepId");
//        videoUrl = getIntent().getStringExtra("bakingVideo");
//        shortDesc = getIntent().getStringExtra("bakingShortDesc");
//        desc = getIntent().getStringExtra("bakingDesc");

        setTitle(bakingName);

        System.out.println("baking id from object : " + bakingId);
//        System.out.println("step id from object : " + stepId);
//        System.out.println("videoUrl from object : " + videoUrl);
//        System.out.println("shortDesc from object : " + shortDesc);
//        System.out.println("desc from object : " + desc);
//
        fetchDbForIngredients(bakingId);
        fetchDBForSteps(bakingId);


        System.out.println("reuslts : " + ingredientsArray.toString());
        System.out.println("stepsDescArray : " + stepsDescArray.toString());
        System.out.println("size of tempArray :" + tempStepsArray.size());

        if (findViewById(R.id.tablet_layout) != null) {
            System.out.println(" two pane started");
            mTwoPane = true;

            FragmentManager fragmentManager = getSupportFragmentManager();

            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setIngredientsArr(ingredientsArray);
            stepsFragment.setStepsDescArr(stepsDescArray);
            stepsFragment.setTempStepsArr(tempStepsArray);
            fragmentManager.beginTransaction().add(R.id.double_fragment_container, stepsFragment).commit();

            System.out.println(" steps  in 2 pane fragment done ");

            StepsDetailFragment stepsDetailFragment = new StepsDetailFragment();
//            stepsDetailFragment.setVideoUrl(tempStepsArray.get());
            fragmentManager.beginTransaction().add(R.id.step_detail_fragment_container, stepsDetailFragment).commit();
            System.out.println(" details  in 2 pane fragment done ");
//            StepsDetailFragment stepsDetailFragment = new StepsDetailFragment();
//            fragmentManager.beginTransaction().add(R.id.step_detail_fragment_container, stepsDetailFragment).commit();
        } else {

            System.out.println(" single pane started");
            mTwoPane = false;
            FragmentManager fragmentManager = getSupportFragmentManager();

            StepsFragment stepsFragment2 = new StepsFragment();
            stepsFragment2.setIngredientsArr(ingredientsArray);
            stepsFragment2.setStepsDescArr(stepsDescArray);
            stepsFragment2.setTempStepsArr(tempStepsArray);
            fragmentManager.beginTransaction().add(R.id.single_fragment_container, stepsFragment2).commit();

        }


    }


    @Override
    public void onStepSelected(Bundle dataBundle) {
        if (mTwoPane) {
            StepsDetailFragment stepsDetailFragment = new StepsDetailFragment();
            stepsDetailFragment.setStepId(dataBundle.getString("stepId"));
            stepsDetailFragment.setVideoUrl(dataBundle.getString("bakingVideo"));
            stepsDetailFragment.setBakingId(dataBundle.getString("bakingId"));
            stepsDetailFragment.setShortDesc(dataBundle.getString("shortDesc"));
            stepsDetailFragment.setDesc(dataBundle.getString("desc"));
            getSupportFragmentManager().beginTransaction().replace(R.id.step_detail_fragment_container, stepsDetailFragment).commit();

        } else {
            System.out.println(" else part on step sleected");
            Intent intent = new Intent(this, RecipeDetailsActivity.class);
            intent.putExtras(dataBundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    private void fetchDbForIngredients(String bakingId) {
        try {
            BakingDBHelper bakingDBHelper = new BakingDBHelper(this);
            mBakingDB = bakingDBHelper.getWritableDatabase();
            Cursor c = mBakingDB.rawQuery("SELECT baking_id, baking_quantity,baking_measure,baking_ingredient  FROM " +
                    BakingContract.BakingEntry.BAKING_INGREDIENT_TABLE +
                    " where baking_id = '" + bakingId + " ' ", null);
            System.out.println("cursor : " + c.toString());
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String bakingQuantity = c.getString(c.getColumnIndex("baking_quantity"));
                        String bakingMeasure = c.getString(c.getColumnIndex("baking_measure"));
                        String bakingIngredient = c.getString(c.getColumnIndex("baking_ingredient"));
                        ingredientsArray.add(bakingQuantity + " " + bakingMeasure + " " + bakingIngredient);
                    } while (c.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        } finally {
            if (mBakingDB != null)
                mBakingDB.close();
        }
    }

    private void fetchDBForSteps(String bakingId) {
        try {
            BakingDBHelper bakingDBHelper = new BakingDBHelper(this);
            mBakingDB = bakingDBHelper.getWritableDatabase();
            Cursor c = mBakingDB.rawQuery("SELECT baking_id,baking_step_id, baking_short_desc,baking_desc,baking_videourl FROM " +
                    BakingContract.BakingEntry.BAKING_STEPS_TABLE +
                    " where baking_id = '" + bakingId + " ' ", null);
            System.out.println("cursor : " + c.toString());
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        bakingStepsObj = new BakingSteps();
                        bakingId = c.getString(c.getColumnIndex("baking_id"));
                        String bakingStepId = c.getString(c.getColumnIndex("baking_step_id"));
                        String bakingShortDesc = c.getString(c.getColumnIndex("baking_short_desc"));
                        String bakingDesc = c.getString(c.getColumnIndex("baking_desc"));
                        String bakingVideoUrl = c.getString(c.getColumnIndex("baking_videourl"));
                        stepsDescArray.add(bakingShortDesc);
                        bakingStepsObj.setStepId(bakingStepId);
                        bakingStepsObj.setShortDesc(bakingShortDesc);
                        bakingStepsObj.setDesc(bakingDesc);
                        bakingStepsObj.setVideoUrl(bakingVideoUrl);
                        tempStepsArray.add(bakingStepsObj);
                    } while (c.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        } finally {
            if (mBakingDB != null)
                mBakingDB.close();
        }
    }
}
