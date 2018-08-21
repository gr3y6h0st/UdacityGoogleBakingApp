package com.nanodegree.android.bakingapp.BakingAppWidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class BakingAppWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        //Service's sole purpose is to return a RemoteViewsFactory object.
        //RemoteViewsFactory obj is where all the data from the main app is assigned to widget listItemViews etc.
        //Acts very similar to an adapter.
        return new BakingAppWidgetRemoteViewsFactory
                (this.getApplicationContext(), intent);
    }
}
