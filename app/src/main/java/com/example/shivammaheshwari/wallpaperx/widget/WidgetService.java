package com.example.shivammaheshwari.wallpaperx.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class WidgetService extends IntentService {

    public static String WIDGET_LIST = "WIDGET_LIST";

    public WidgetService() {
        super("WidgetService");
    }


    public static void startBakingService(Context context, ArrayList<String> ingredientsForWidget) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(WIDGET_LIST, ingredientsForWidget);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            ArrayList<String> itemsFromFavouriteList = intent.getExtras().getStringArrayList(WIDGET_LIST);
            handleActionUpdateBakingWidgets(itemsFromFavouriteList);
        }
    }

    private void handleActionUpdateBakingWidgets(ArrayList<String> itemsFromFavouriteList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(WIDGET_LIST, itemsFromFavouriteList);
        sendBroadcast(intent);
    }
}
