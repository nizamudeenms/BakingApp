package com.example.nizam.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Baking> bakingArrayList;
    private String TAG = "MainActivity";

    private String bakingId;
    private String bakingName;
    private String servings;
    private String bakingImage;
    private SQLiteDatabase mBakingDB;
    ReceipeAdapter receipeAdapter;
    Context context;
    ProgressBar mRefresh;
    Cursor urlsCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        bakingArrayList = new ArrayList<>();

        mRefresh = findViewById(R.id.progressBar);

        recyclerView.setVisibility(View.INVISIBLE);
        mRefresh.setVisibility(View.VISIBLE);
        BakingDBHelper bakingDBHelper = new BakingDBHelper(this);
        mBakingDB = bakingDBHelper.getWritableDatabase();

        FetchBakingTask();


    }

    private Cursor getBaking() {
        return mBakingDB.query(
                BakingContract.BakingEntry.BAKING_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }


    private Cursor getBakingIng() {
        return mBakingDB.query(
                BakingContract.BakingEntry.BAKING_INGREDIENT_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }


    private Cursor getBakingStep() {
        return mBakingDB.query(
                BakingContract.BakingEntry.BAKING_STEPS_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private Cursor getBakingURL() {
        return mBakingDB.query(
                BakingContract.BakingEntry.BAKING_STEPS_TABLE,
                new String[]{BakingContract.BakingEntry.COLUMN_BAKING_VIDEOURL},
                BakingContract.BakingEntry.COLUMN_BAKING_ID,
                null,
                null,
                null,
                null
        );
    }

    public void FetchBakingTask() {

        final String BAKING_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        JsonArrayRequest req = new JsonArrayRequest(BAKING_BASE_URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "Begining of reponse");
                try {
//                              JSONArray responseBundle = response.getJSONArray();
                    System.out.println(" inside try");
//                        JSONArray responseBundle = response.getJSONArray();
//                        System.out.println(" response is : " + response.toString());
                    System.out.println(" response is : " + response.length());
                    for (int j = 0; j < response.length(); j++) {
                        JSONObject c = response.getJSONObject(j);
//                            System.out.println(" jsonobject : " + c.toString());
                        bakingId = c.getString("id");
                        bakingName = c.getString("name");
                        JSONArray bakingIngredients = c.getJSONArray("ingredients");
                        JSONArray bakingSteps = c.getJSONArray("steps");
                        servings = c.getString("servings");
                        bakingImage = c.getString("image");

                        Log.i(TAG, bakingId);
                        Log.i(TAG, bakingName);
                        Log.i(TAG, bakingIngredients.toString());
                        Log.i(TAG, bakingSteps.toString());
                        Log.i(TAG, servings);
                        Log.i(TAG, bakingImage);

                        Baking baking = new Baking();
                        baking.setBakingId(c.getString("id"));
                        baking.setBakingName(c.getString("name"));
                        baking.setServings(c.getString("servings"));
                        baking.setBakingImage(c.getString("image"));

                        ContentValues cv = new ContentValues();
                        cv.put(BakingContract.BakingEntry.COLUMN_BAKING_ID, bakingId);
                        cv.put(BakingContract.BakingEntry.COLUMN_BAKING_NAME, bakingName);
                        cv.put(BakingContract.BakingEntry.COLUMN_SERVINGS, servings);
                        cv.put(BakingContract.BakingEntry.COLUMN_BAKING_IMAGE, bakingImage);
                        mBakingDB.insert(BakingContract.BakingEntry.BAKING_TABLE, null, cv);
                        System.out.println("Value inserted in Baking DB ");

                        for (int k = 0; k < bakingIngredients.length(); k++) {
                            JSONObject ing = bakingIngredients.getJSONObject(k);
                            System.out.println(" ing is : " + ing.toString());
                            BakingIngredients bakingIngredientsObj = new BakingIngredients();
                            bakingIngredientsObj.setIngredient(ing.getString("quantity"));
                            bakingIngredientsObj.setMeasure(ing.getString("measure"));
                            bakingIngredientsObj.setIngredient(ing.getString("ingredient"));

                            ContentValues cv1 = new ContentValues();
                            System.out.println("bakingId :  " + bakingId);
                            System.out.println(" ing.getString(\"quantity\") :  " + ing.getString("quantity"));
                            System.out.println("ing.getString(\"measure\") :  " + ing.getString("measure"));
                            System.out.println(" ing.getString(\"ingredient\") :  " + ing.getString("ingredient"));
                            cv1.put(BakingContract.BakingEntry.COLUMN_BAKING_ID, bakingId);
                            cv1.put(BakingContract.BakingEntry.COLUMN_BAKING_QUANTITY, ing.getString("quantity"));
                            cv1.put(BakingContract.BakingEntry.COLUMN_BAKING_MEASURE, ing.getString("measure"));
                            cv1.put(BakingContract.BakingEntry.COLUMN_BAKING_INGREDIENT, ing.getString("ingredient"));
                            mBakingDB.insert(BakingContract.BakingEntry.BAKING_INGREDIENT_TABLE, null, cv1);
                            System.out.println("Value inserted in Baking Ingredient DB ");
//                                baking.setBakingIngredients(bakingIngredientsObj);
                        }

                        for (int h = 0; h < bakingSteps.length(); h++) {
                            JSONObject steps = bakingSteps.getJSONObject(h);
                            BakingSteps bakingStepsObj = new BakingSteps();
                            bakingStepsObj.setId(steps.getString("id"));
                            bakingStepsObj.setDesc(steps.getString("description"));
                            bakingStepsObj.setShortDesc(steps.getString("shortDescription"));
                            bakingStepsObj.setVideoUrl(steps.getString("videoURL"));
                            bakingStepsObj.setThumnailUrl(steps.getString("thumbnailURL"));

                            ContentValues cv2 = new ContentValues();
                            cv2.put(BakingContract.BakingEntry.COLUMN_BAKING_ID, bakingId);
                            cv2.put(BakingContract.BakingEntry.COLUMN_BAKING_STEP_ID, steps.getString("id"));
                            cv2.put(BakingContract.BakingEntry.COLUMN_BAKING_SHORT_DESC, steps.getString("shortDescription"));
                            cv2.put(BakingContract.BakingEntry.COLUMN_BAKING_DESC, steps.getString("description"));
                            cv2.put(BakingContract.BakingEntry.COLUMN_BAKING_VIDEOURL, steps.getString("videoURL"));
                            cv2.put(BakingContract.BakingEntry.COLUMN_BAKING_THUMBNAILURL, steps.getString("thumbnailURL"));
                            mBakingDB.insert(BakingContract.BakingEntry.BAKING_STEPS_TABLE, null, cv2);
                            System.out.println("Value inserted in Baking STEPS DB ");

                            bakingImage = steps.getString("videoURL");
                        }

                        System.out.println("steps.getString(\"videoURL\") : " + bakingImage);


                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
                loadImages();



                Cursor cBaking = getBaking();
                receipeAdapter = new ReceipeAdapter(context, cBaking);
                recyclerView.setAdapter(receipeAdapter);
                System.out.println(" after settings adapter");

                receipeAdapter.notifyDataSetChanged();
                mRefresh.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                Toast toast = Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG);
                toast.show();
                System.out.println("after toast");
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                toast.show();
                volleyError.printStackTrace();
            }
        });
        BakingController.getInstance().addToRequestQueue(req);
    }

    public Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            System.out.println("inside retieve video method");
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14) {
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            } else {
                mediaMetadataRetriever.setDataSource(videoPath);
            }
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public void loadImages() {

        System.out.println("inside load images");

//        MovieDbHelper movieDbHelper = new MovieDbHelper(this);
//        mMovieDb = movieDbHelper.getWritableDatabase();
//        favoriteMovies = mMovieDb.rawQuery("SELECT  * FROM POPULAR_MOVIES_TABLE POP WHERE  id = '"+movieId+"'",null );


        urlsCursor = mBakingDB.rawQuery("select max(baking_step_id) ,baking_id,baking_videourl from baking_steps_table group by baking_id", null);
        System.out.println("urlsCursor.getCount() : " + urlsCursor.getCount());

        if (urlsCursor.getCount() != 0) {
            System.out.println("favoriteMovies Cursor : " + urlsCursor);
            if (urlsCursor.moveToFirst()) {
                while (!urlsCursor.isAfterLast()) {
                    String data = urlsCursor.getString(urlsCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_VIDEOURL));
                    String id = urlsCursor.getString(urlsCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_BAKING_ID));
                    System.out.println("baking id : " + id);
                    System.out.println("data is : " + data);
//                    new Thread(new Runnable() {
//                        public void run() {
//                            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                            Bitmap bmThumbnail = null;
                            try {
                                ContentValues cv3 = new ContentValues();
                                bmThumbnail = retriveVideoFrameFromVideo(data);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bmThumbnail.compress(Bitmap.CompressFormat.PNG, 10, stream);
                                cv3.put(BakingContract.BakingEntry.COLUMN_BAKING_IMAGE, stream.toByteArray());
                                String whereArgs[] = {id};
                                mBakingDB.update(BakingContract.BakingEntry.BAKING_TABLE, cv3, "baking_id=?", whereArgs);
                                System.out.println("Value updated in Baking Main DB for video URL ");
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
//                        }
//                    }).start();
                    urlsCursor.moveToNext();
                }
            }
//            getBakingURL().close();
        }


    }


}
