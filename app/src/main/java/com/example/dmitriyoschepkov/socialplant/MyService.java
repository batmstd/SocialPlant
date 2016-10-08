package com.example.dmitriyoschepkov.socialplant;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
public class MyService extends Service {

    final String LOG_TAG = "myLogs";
    NotificationManager nm;
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Служба запущена");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Служба в работе");
        setAlarm(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Служба остановлена");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

    public static void setAlarm(Context ctx)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ctx, Receiver.class);
        intent.setAction("notify");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (calendar.getTimeInMillis()<System.currentTimeMillis()+500){
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() +24*60*60*1000,AlarmManager.INTERVAL_DAY, pendingIntent);
            System.out.println("Сейчас: "+System.currentTimeMillis()+", передвигаем на: "+calendar.getTimeInMillis() +24*60*60*1000);
        }else am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() ,AlarmManager.INTERVAL_DAY, pendingIntent);
        /*
            long alarmTime = calendar.getTimeInMillis();
    if (alarmTime < System.currentTimeMillis() + 500)
        alarmTime += 24*60*60*1000;
    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
            alarmTime, AlarmManager.INTERVAL_DAY,
            pendingIntent);
         */
    }
}