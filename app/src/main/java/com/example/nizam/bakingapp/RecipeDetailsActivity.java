package com.example.nizam.bakingapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class RecipeDetailsActivity extends AppCompatActivity {

    Fragment mRecipeFragment;
    private ListView mListView1, mListView2;
    private SQLiteDatabase mBakingDB;
    private ArrayList<String> ingredientsArray = new ArrayList<String>();
    private ArrayList<String> stepsDescArray = new ArrayList<String>();
    private ArrayList<BakingSteps> tempStepsArray = new ArrayList<BakingSteps>();
    BakingSteps bakingStepsObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        String bakingName = getIntent().getStringExtra("bakingName");
        String bakingId = getIntent().getStringExtra("bakingId");
        setTitle(bakingName);
        System.out.println("getIntent().getStringExtra(\"bakingId\").toString(): " + bakingId);

        fetchDbForIngredients(bakingId);
        fetchDBForSteps(bakingId);

        System.out.println("reuslts : " + ingredientsArray.toString());
        System.out.println("stepsDescArray : " + stepsDescArray.toString());

        mListView1 = findViewById(R.id.ingredient_list);
        mListView2 = findViewById(R.id.steps_list);

        mListView1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientsArray));
        mListView2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stepsDescArray));

        ListUtils.setDynamicHeight(mListView1);
        ListUtils.setDynamicHeight(mListView2);


        System.out.println("size of tempArray :"+tempStepsArray.size());

        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                System.out.println("position is : "+position);
//                if(!tempStepsArray.get(position).getVideoUrl().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), RecipeStepsActivity.class);
                    intent.putExtra("bakingVideo", (tempStepsArray.get(position).getVideoUrl().isEmpty() ? "nil" : tempStepsArray.get(position).getVideoUrl()));
                    intent.putExtra("stepId", tempStepsArray.get(position).getStepId());
                    intent.putExtra("bakingName", bakingName);
                    intent.putExtra("bakingId", bakingId);
                    intent.putExtra("bakingShortDesc", tempStepsArray.get(position).getShortDesc());
                    intent.putExtra("bakingDesc", tempStepsArray.get(position).getDesc());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
//                }else{
//                    Toast noVideosMessageToast = Toast.makeText(getApplicationContext(), "Video Not Available", Toast.LENGTH_SHORT);
//                    noVideosMessageToast.show();                }
            }
        });

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

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

}
