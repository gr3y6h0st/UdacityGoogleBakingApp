package com.nanodegree.android.bakingapp.BakingAppWidget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.nanodegree.android.bakingapp.BakingData.BakingDataContract;
import com.nanodegree.android.bakingapp.R;

public class BakingAppWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private Context mContext;
    private Cursor mCursor;

    public BakingAppWidgetRemoteViewsFactory(Context context, Intent intent){
        mContext = context;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        if(mCursor != null){
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        //set uri to the CONTENT URI;
        Uri uri = BakingDataContract.CONTENT_URI;

        //query all data in table
        mCursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                null);

        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {
        if(mCursor != null){
            //close cursor
            mCursor.close();
        }

    }

    @Override
    public int getCount() {
        if(mCursor == null) {
            return 0;
        }
        else {
            return mCursor.getCount();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(position == AdapterView.INVALID_POSITION ||
                mCursor == null ||
                !mCursor.moveToPosition(position)){
            return null;
        }

        //recreation of RemoteViews using widget List Item layout.
        RemoteViews listItemRv = new RemoteViews(mContext.getPackageName(), R.layout.baking_app_widget_list_item);
        //set the text on the List Item TextView.
        listItemRv.setTextViewText(R.id.baking_app_widget_item_tv, mCursor.getString(0) + " "
                + mCursor.getString(1) + " "
                + mCursor.getString(2));

        // Construct the RemoteViews object
        /*RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.baking_app_widget_title, mCursor.getString(3));*/

        return listItemRv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
         if(mCursor.move(position)){
             return mCursor.getLong(0);
        } else {
             return position;
         }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
