package com.nanodegree.android.bakingapp.BakingAppWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.nanodegree.android.bakingapp.BakingData.BakingDataContract;
import com.nanodegree.android.bakingapp.R;
import com.nanodegree.android.bakingapp.RecipeInfoActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    /**
     * This method is used w/in the onUpdate method inside the for loop.
     * Sets the text
     */
    //
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        Bundle bundle = new Bundle();

        //set RecipeName as title
        Cursor cursor = context.getContentResolver().query(
                BakingDataContract.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            cursor.moveToFirst();

            //columnIndex 3 should be the RecipeName column in data table.
            views.setTextViewText(R.id.baking_app_widget_title, cursor.getString(3));
            bundle.putString("recipeName", cursor.getString(3));
            bundle.putString("recipeID", cursor.getString(4));


            //Log.v("TEST VALUE", cursor.getString(3));
            cursor.close();
        } else{
            Log.v("REMOTEVIEW ERROR: ", "unable to populate Title.");
            cursor.close();
        }

        //creates Intent to launch RecipeInfoActivity when clicked.
        Intent RecipeInfoIntent = new Intent(context, RecipeInfoActivity.class);
        //TODO:try putting in extras in recipeInfoIntent using ContentProvider data

        RecipeInfoIntent.putExtras(bundle);
        //creates PendingIntent required to wrap the RecipeInfoIntent above
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, RecipeInfoIntent, 0);
        //Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.baking_app_widget_title, pendingIntent);

        //creates Intent to launch RemoteViewService
        Intent remoteViewServiceIntent = new Intent (context, BakingAppWidgetRemoteViewsService.class);
        //set Adapter using intent above
        views.setRemoteAdapter(R.id.bakingAppListView, remoteViewServiceIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action != null && action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {

            //refresh all widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context,
                    BakingAppWidgetProvider.class);

            //Calling onUpdate & notifyAppWidgetViewDataChanged will set the widget to
            //display the last recipe the user selected.
            onUpdate(context, mgr, mgr.getAppWidgetIds(componentName));
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(componentName),
                    R.id.bakingAppListView);

        }
        super.onReceive(context, intent);
    }

    public static void sendRefreshBroadcast(Context context){
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, BakingAppWidgetProvider.class));
        context.sendBroadcast(intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

