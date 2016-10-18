package com.example.dmitriyoschepkov.socialplant;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.util.Log;
import java.util.Calendar;

/**
 * Created by Dmitriy.Oschepkov on 18.10.2016.
 */
public  class  ReceiverUpdate extends BroadcastReceiver {
    Calendar dateAndTime=Calendar.getInstance();
    public DBHelper mDatabaseHelper;
    public SQLiteDatabase mSqLiteDatabase;
    @Override
    public void onReceive(Context context, Intent intent){
        String currentDate = DateUtils.formatDateTime(context,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
        System.out.println("Now: "+currentDate);
        String updateActual = "update table_activity set actual = 1 where date_type = '"+currentDate+"';";
        Cursor updateCursor = mSqLiteDatabase.rawQuery(updateActual, null);
        int countUpdate = updateCursor.getCount();
        System.out.println("update: "+ countUpdate);
        updateCursor.close();
        mDatabaseHelper.close();
    }
}