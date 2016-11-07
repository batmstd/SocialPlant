package com.example.dmitriyoschepkov.socialplant;

/**
 * Created by Dmitriy.Oschepkov on 07.10.2016.
 */
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
public class Receiver extends BroadcastReceiver {
    private static final int NOTIFY_ID = 1;
    final String LOG = "myLogs";
    Calendar dateAndTime=Calendar.getInstance();
    public DBHelper mDatabaseHelper;
    public SQLiteDatabase mSqLiteDatabase;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String currentDate = DateUtils.formatDateTime(context,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
        System.out.println("Now: "+currentDate);
        String selectToday = "Select * " +
                "FROM table_activity " +
                "INNER JOIN table_plant " +
                "ON table_plant._ID=table_activity.id_plant " +
                "INNER JOIN table_type " +
                "ON table_activity.type=table_type._ID " +
                "where date_type ='"+currentDate+"';";
        mDatabaseHelper = new DBHelper(context, "plant.db", null, DBHelper.DATABASE_VERSION);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = mSqLiteDatabase.rawQuery(selectToday, null);
        String updateActual = "update table_activity set actual = 1 where date_type = '"+currentDate+"';";
        Cursor updateCursor = mSqLiteDatabase.rawQuery(updateActual, null);
        int count = cursor.getCount();
        int countUpdate = updateCursor.getCount();
        System.out.println("Строк по join дате: "+ count);
        System.out.println("update: "+ countUpdate);
        if (count==0){
            System.out.print("сегодня событий нет");
        }else if(count==1){
            cursor.moveToFirst();
            String select_date_type1 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
            String select_name1 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
            String select_type1 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
            System.out.println(select_date_type1+";"+select_name1+";"+select_type1);
            Log.d(LOG, "выполняется: " + intent.getAction());
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            Resources res = context.getResources();
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_local_florist_teal_600_48dp)
                    .setPriority(Notification.PRIORITY_HIGH)// большая картинка
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_local_florist_teal_600_48dp))
                    .setTicker(select_name1) // текст в строке состояния
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setStyle(new Notification.InboxStyle()
                            .addLine(select_name1+" "+select_type1)
                            .setBigContentTitle("Пора заняться растениями :)"))
                            //.setSummaryText("хз"))
                    .setContentText(select_name1+" "+select_type1)
                    .setContentTitle("Пора заняться растениями :)");
            Notification notification = builder.build();
            notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, notification);
        }else if(count==2){
            cursor.moveToFirst();
            String select_date_type1 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
            String select_name1 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
            String select_type1 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
            System.out.println(select_date_type1+";"+select_name1+";"+select_type1);
            cursor.moveToNext();
            String select_date_type2 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
            String select_name2 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
            String select_type2 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
            System.out.println(select_date_type2+";"+select_name2+";"+select_type2);
            Log.d(LOG, "выполняется: " + intent.getAction());
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            Resources res = context.getResources();
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_local_florist_teal_600_48dp)
                    .setPriority(Notification.PRIORITY_HIGH)// большая картинка
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_local_florist_teal_600_48dp))
                    .setTicker(select_name1) // текст в строке состояния
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setStyle(new Notification.InboxStyle()
                            .addLine(select_name1+" "+select_type1)
                            .addLine(select_name2+" "+select_type2)
                            .setBigContentTitle("Пора заняться растениями :)")
                    )
                    .setContentText(select_name1+" "+select_type1+"...")
                    .setContentTitle("Пора заняться растениями :)");
            Notification notification = builder.build();
            notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, notification);
        }else if(count==3){
            cursor.moveToFirst();
            String select_date_type1 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
            String select_name1 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
            String select_type1 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
            System.out.println(select_date_type1+";"+select_name1+";"+select_type1);
            cursor.moveToNext();
            String select_date_type2 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
            String select_name2 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
            String select_type2 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
            System.out.println(select_date_type2+";"+select_name2+";"+select_type2);
            cursor.moveToNext();
            String select_date_type3 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
            String select_name3 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
            String select_type3 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
            System.out.println(select_date_type3+";"+select_name3+";"+select_type3);
            Log.d(LOG, "выполняется: " + intent.getAction());
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            Resources res = context.getResources();
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_local_florist_teal_600_48dp)
                    .setPriority(Notification.PRIORITY_HIGH)// большая картинка
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_local_florist_teal_600_48dp))
                    .setTicker(select_name1) // текст в строке состояния
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setStyle(new Notification.InboxStyle()
                            .addLine(select_name1+" "+select_type1)
                            .addLine(select_name2+" "+select_type2)
                            .addLine(select_name3+" "+select_type3)
                            .setBigContentTitle("Пора заняться растениями :)")
                    )
                    .setContentText(select_name1+" "+select_type1+" и еще ...")
                    .setContentTitle("Пора заняться растениями :)");
            Notification notification = builder.build();
            notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, notification);
        }else if(count==4){
        cursor.moveToFirst();
        String select_date_type1 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
        String select_name1 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
        String select_type1 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
        System.out.println(select_date_type1+";"+select_name1+";"+select_type1);
        cursor.moveToNext();
        String select_date_type2 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
        String select_name2 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
        String select_type2 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
        System.out.println(select_date_type2+";"+select_name2+";"+select_type2);
        cursor.moveToNext();
        String select_date_type3 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
        String select_name3 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
        String select_type3 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
        System.out.println(select_date_type3+";"+select_name3+";"+select_type3);
        cursor.moveToNext();
        String select_date_type4 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
        String select_name4 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
        String select_type4 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
        System.out.println(select_date_type4+";"+select_name4+";"+select_type4);
        Log.d(LOG, "выполняется: " + intent.getAction());
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_local_florist_teal_600_48dp)
                .setPriority(Notification.PRIORITY_HIGH)// большая картинка
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_local_florist_teal_600_48dp))
                .setTicker(select_name1) // текст в строке состояния
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setStyle(new Notification.InboxStyle()
                        .addLine(select_name1+" "+select_type1)
                        .addLine(select_name2+" "+select_type2)
                        .addLine(select_name3+" "+select_type3)
                        .addLine(select_name4+" "+select_type4)
                        .setBigContentTitle("Пора заняться растениями :)")
                )
                .setContentText(select_name1+" "+select_type1+" и еще ...")
                .setContentTitle("Пора заняться растениями :)");
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }
        else if(count>4){
            cursor.moveToFirst();
            String select_date_type1 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
            String select_name1 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
            String select_type1 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
            System.out.println(select_date_type1+";"+select_name1+";"+select_type1);
            cursor.moveToNext();
            String select_date_type2 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
            String select_name2 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
            String select_type2 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
            System.out.println(select_date_type2+";"+select_name2+";"+select_type2);
            cursor.moveToNext();
            String select_date_type3 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
            String select_name3 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
            String select_type3 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
            System.out.println(select_date_type3+";"+select_name3+";"+select_type3);
            cursor.moveToNext();
            String select_date_type4 = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_TYPE));
            String select_name4 = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
            String select_type4 = cursor.getString(cursor.getColumnIndex(DBHelper.PROP_VALUE));
            System.out.println(select_date_type4+";"+select_name4+";"+select_type4);
            Log.d(LOG, "выполняется: " + intent.getAction());
            int countOstatok = count -4;
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            Resources res = context.getResources();
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_local_florist_teal_600_48dp)
                    .setPriority(Notification.PRIORITY_HIGH)// большая картинка
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_local_florist_teal_600_48dp))
                    .setTicker(select_name1) // текст в строке состояния
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setStyle(new Notification.InboxStyle()
                            .addLine(select_name1+" "+select_type1)
                            .addLine(select_name2+" "+select_type2)
                            .addLine(select_name3+" "+select_type3)
                            .addLine(select_name4+" "+select_type4)
                            .setSummaryText("Еще событий: "+countOstatok)
                            .setBigContentTitle("Пора заняться растениями :)")
                    )
                    .setContentText(select_name1+" "+select_type1+" и еще ...")
                    .setContentTitle("Пора заняться растениями :)");
            Notification notification = builder.build();
            notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, notification);
        }
    cursor.close();
    mDatabaseHelper.close();
}



}
