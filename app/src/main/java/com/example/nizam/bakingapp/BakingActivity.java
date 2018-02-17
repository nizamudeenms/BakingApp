package com.example.nizam.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
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
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BakingActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar mRefresh;

    private String TAG = "BakingActivity";

    private String bakingId;
    private String bakingName;
    private String servings;
    private String bakingImage;
    private SQLiteDatabase mBakingDB;
    BakingAdapter bakingAdapter;
    Context context;

    boolean isAppInstalled = false;
    public SharedPreferences appPreferences;

    @Nullable
    private SimpleIdlingResource mIdlingResource;


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isAppInstalled = appPreferences.getBoolean("isAppInstalled", false);
        installShortcut(isAppInstalled);

        ButterKnife.bind(this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), getResources().getInteger(R.integer.grid_number_cols));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        recyclerView.setVisibility(View.INVISIBLE);
        mRefresh.setVisibility(View.VISIBLE);
        BakingDBHelper bakingDBHelper = new BakingDBHelper(this);
        mBakingDB = bakingDBHelper.getWritableDatabase();

        FetchBakingTask bakingTask = new FetchBakingTask();
        bakingTask.execute();


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("ListState", recyclerView.getLayoutManager().onSaveInstanceState());

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

    private void installShortcut(Boolean isAppInstalled) {

        if (!isAppInstalled) {

            Intent shortcutIntent = new Intent(getApplicationContext(), BakingActivity.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Baking APP Udacity");
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource
                    .fromContext(getApplicationContext(), R.mipmap.ic_launcher_round));
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(intent);

            SharedPreferences.Editor editor = appPreferences.edit();
            editor.putBoolean("isAppInstalled", true);
            editor.apply();
        }

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

    public class FetchBakingTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            final String BAKING_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

            JsonArrayRequest req = new JsonArrayRequest(BAKING_BASE_URL, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, response.toString());
                    try {
                        for (int j = 0; j < response.length(); j++) {
                            JSONObject c = response.getJSONObject(j);
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

                            for (int k = 0; k < bakingIngredients.length(); k++) {
                                JSONObject ing = bakingIngredients.getJSONObject(k);
                                BakingIngredients bakingIngredientsObj = new BakingIngredients();
                                bakingIngredientsObj.setIngredient(ing.getString("quantity"));
                                bakingIngredientsObj.setMeasure(ing.getString("measure"));
                                bakingIngredientsObj.setIngredient(ing.getString("ingredient"));

                                ContentValues cv1 = new ContentValues();
                                cv1.put(BakingContract.BakingEntry.COLUMN_BAKING_ID, bakingId);
                                cv1.put(BakingContract.BakingEntry.COLUMN_BAKING_QUANTITY, ing.getString("quantity"));
                                cv1.put(BakingContract.BakingEntry.COLUMN_BAKING_MEASURE, ing.getString("measure"));
                                cv1.put(BakingContract.BakingEntry.COLUMN_BAKING_INGREDIENT, ing.getString("ingredient"));
                                mBakingDB.insert(BakingContract.BakingEntry.BAKING_INGREDIENT_TABLE, null, cv1);
                            }

                            for (int h = 0; h < bakingSteps.length(); h++) {
                                JSONObject steps = bakingSteps.getJSONObject(h);
                                BakingSteps bakingStepsObj = new BakingSteps();
                                bakingStepsObj.setStepId(steps.getString("id"));
                                bakingStepsObj.setDesc(steps.getString("description"));
                                bakingStepsObj.setShortDesc(steps.getString("shortDescription"));
                                bakingStepsObj.setVideoUrl(steps.getString("videoURL"));
                                bakingStepsObj.setThumbnailUrl(steps.getString("thumbnailURL"));

                                ContentValues cv2 = new ContentValues();
                                cv2.put(BakingContract.BakingEntry.COLUMN_BAKING_ID, bakingId);
                                cv2.put(BakingContract.BakingEntry.COLUMN_BAKING_STEP_ID, steps.getString("id"));
                                cv2.put(BakingContract.BakingEntry.COLUMN_BAKING_SHORT_DESC, steps.getString("shortDescription"));
                                cv2.put(BakingContract.BakingEntry.COLUMN_BAKING_DESC, steps.getString("description"));
                                cv2.put(BakingContract.BakingEntry.COLUMN_BAKING_VIDEOURL, steps.getString("videoURL"));
                                cv2.put(BakingContract.BakingEntry.COLUMN_BAKING_THUMBNAILURL, steps.getString("thumbnailURL"));
                                mBakingDB.insert(BakingContract.BakingEntry.BAKING_STEPS_TABLE, null, cv2);
                                bakingImage = steps.getString("videoURL");
                            }
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }

                    Cursor cBaking = getBaking();
                    bakingAdapter = new BakingAdapter(context, cBaking);
                    recyclerView.setAdapter(bakingAdapter);

                    bakingAdapter.notifyDataSetChanged();
                    mRefresh.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG);
                    toast.show();
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
                    } else if (volleyError instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                    }

                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                    toast.show();
                    volleyError.printStackTrace();
                }
            });
            BakingController.getInstance().addToRequestQueue(req);

            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(true);
            }
            return null;
        }

        public synchronized Bitmap retriveVideoFrameFromVideo(String videoPath)
                throws Throwable {
            Bitmap bitmap = null;
            MediaMetadataRetriever mediaMetadataRetriever = null;
            try {
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

    }
}
