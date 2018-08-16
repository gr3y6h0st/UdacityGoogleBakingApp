package com.nanodegree.android.bakingapp.BakingAppWidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class BakingAppWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingAppWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
