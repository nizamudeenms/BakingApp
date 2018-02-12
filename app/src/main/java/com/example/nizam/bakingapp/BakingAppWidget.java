package com.example.nizam.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.ArrayList;

import static com.example.nizam.bakingapp.BakingAppWidgetUpdateService.FROM_ACTIVITY_INGREDIENTS_LIST;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {
    static ArrayList<String> ingredientsList = new ArrayList<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        //call activity when widget is clicked, but resume activity from stack so you do not pass intent.extras afresh
//        Intent appIntent = new Intent(context, RecipeDetailsActivity.class);
//        appIntent.addCategory(Intent.ACTION_MAIN);
//        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        appIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setPendingIntentTemplate(R.id.ingredient_list, appPendingIntent);

        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        views.setRemoteAdapter(R.id.ingredient_list, intent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidget.class));

        final String action = intent.getAction();

        if (action.equals("android.appwidget.action.APPWIDGET_UPDATE2")) {
            ingredientsList = intent.getExtras().getStringArrayList(FROM_ACTIVITY_INGREDIENTS_LIST);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredient_list);
            //Now update all widgets
            updateBakingWidgets(context, appWidgetManager, appWidgetIds);
            super.onReceive(context, intent);
        }
    }
}