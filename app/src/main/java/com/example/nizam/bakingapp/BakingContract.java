package com.example.nizam.bakingapp;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Nizam on 07-Nov-2017 007.
 */

public class BakingContract {
    public static final String CONTENT_AUTHORITY = "com.example.nizamudeenms.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BAKE = "bakingapp";

    public static final class BakingEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_BAKE)
                .build();

        public static final String BAKING_TABLE = "baking_main_table";
        public static final String BAKING_INGREDIENT_TABLE = "baking_ingredient_table";
        public static final String BAKING_STEPS_TABLE = "baking_steps_table";
        public static final String COLUMN_BAKING_ID = "baking_id";
        public static final String COLUMN_BAKING_NAME = "baking_name";
        public static final String COLUMN_SERVINGS= "baking_servings";
        public static final String COLUMN_BAKING_IMAGE= "baking_image";
        public static final String COLUMN_BAKING_STEP_ID= "baking_step_id";
        public static final String COLUMN_BAKING_SHORT_DESC= "baking_short_desc";
        public static final String COLUMN_BAKING_DESC= "baking_desc";
        public static final String COLUMN_BAKING_VIDEOURL= "baking_videourl";
        public static final String COLUMN_BAKING_THUMBNAILURL= "baking_thumbnail";
        public static final String COLUMN_BAKING_QUANTITY= "baking_quantity";
        public static final String COLUMN_BAKING_MEASURE= "baking_measure";
        public static final String COLUMN_BAKING_INGREDIENT= "baking_ingredient";

    }

}
