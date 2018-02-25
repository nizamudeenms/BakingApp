package com.example.nizam.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class RecipeStepsActivity extends AppCompatActivity implements StepsFragment.StepListener {

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
        setTitle(bakingName);

        fetchDbForIngredients(bakingId);
        fetchDBForSteps(bakingId);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.tablet_layout) != null) {
            mTwoPane = true;
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setIngredientsArr(ingredientsArray);
            stepsFragment.setStepsDescArr(stepsDescArray);
            stepsFragment.setTempStepsArr(tempStepsArray);
            fragmentManager.beginTransaction().add(R.id.double_fragment_container, stepsFragment).commit();

            StepsDetailFragment stepsDetailFragment = new StepsDetailFragment();
            fragmentManager.beginTransaction().add(R.id.step_detail_fragment_container, stepsDetailFragment).commit();
        } else {
            mTwoPane = false;
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepsFragment stepsFragment2 = new StepsFragment();
            stepsFragment2.setIngredientsArr(ingredientsArray);
            stepsFragment2.setStepsDescArr(stepsDescArray);
            stepsFragment2.setTempStepsArr(tempStepsArray);
            fragmentManager.beginTransaction().add(R.id.single_fragment_container, stepsFragment2).commit();
        }
    }

    private void fetchDbForIngredients(String bakingId) {
        try {
            BakingDBHelper bakingDBHelper = new BakingDBHelper(this);
            mBakingDB = bakingDBHelper.getWritableDatabase();
            Cursor c = mBakingDB.rawQuery("SELECT baking_id, baking_quantity,baking_measure,baking_ingredient  FROM " +
                    BakingContract.BakingEntry.BAKING_INGREDIENT_TABLE +
                    " where baking_id = '" + bakingId + " ' ", null);
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
            Cursor c = mBakingDB.rawQuery("SELECT baking_id,baking_step_id, baking_short_desc,baking_desc,baking_videourl,baking_thumbnail FROM " +
                    BakingContract.BakingEntry.BAKING_STEPS_TABLE +
                    " where baking_id = '" + bakingId + " ' ", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        bakingStepsObj = new BakingSteps();
                        bakingId = c.getString(c.getColumnIndex("baking_id"));
                        String bakingStepId = c.getString(c.getColumnIndex("baking_step_id"));
                        String bakingShortDesc = c.getString(c.getColumnIndex("baking_short_desc"));
                        String bakingDesc = c.getString(c.getColumnIndex("baking_desc"));
                        String bakingVideoUrl = c.getString(c.getColumnIndex("baking_videourl"));
                        String bakingThumbnailUrl = c.getString(c.getColumnIndex("baking_thumbnail"));
                        stepsDescArray.add(bakingShortDesc);
                        bakingStepsObj.setStepId(bakingStepId);
                        bakingStepsObj.setShortDesc(bakingShortDesc);
                        bakingStepsObj.setDesc(bakingDesc);
                        bakingStepsObj.setVideoUrl(bakingVideoUrl);
                        bakingStepsObj.setThumbnailUrl(bakingThumbnailUrl);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_recipe:
                BakingAppWidgetUpdateService.startBakingService(getApplicationContext(), ingredientsArray);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(Bundle dataBundle) {
        if (mTwoPane) {
            StepsDetailFragment stepsDetailFragment = new StepsDetailFragment();
            stepsDetailFragment.setStepId(dataBundle.getString("stepId"));
            stepsDetailFragment.setVideoUrl(dataBundle.getString("bakingVideo"));
            stepsDetailFragment.setBakingId(dataBundle.getString("bakingId"));
            stepsDetailFragment.setShortDesc(dataBundle.getString("shortDesc"));
            stepsDetailFragment.setDesc(dataBundle.getString("desc"));
            getSupportFragmentManager().beginTransaction().replace(R.id.step_detail_fragment_container, stepsDetailFragment).commit();

        } else {
            Intent intent = new Intent(this, RecipeDetailsActivity.class);
            intent.putExtras(dataBundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
