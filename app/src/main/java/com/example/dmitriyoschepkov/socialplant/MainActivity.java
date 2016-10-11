package com.example.dmitriyoschepkov.socialplant;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DBHelper mDatabaseHelper;
    DBHelper sqlHelper;
    SQLiteDatabase mSqLiteDatabase;
    ListView mList;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    private static final int NOTIFY_ID = 101;
    final String LOG_TAG = "myLogs";
    NotificationManager nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
        mList = (ListView)findViewById(R.id.list);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
                        + id);
                Intent intent = new Intent(getApplicationContext(), profile.class);
                intent.putExtra("position", position);
                startActivity(intent);


            }
        });
        sqlHelper = new DBHelper(getApplicationContext());
        startService(new Intent(this, MyService.class));
    }
    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = sqlHelper.getReadableDatabase();
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        //получаем данные из бд
        userCursor =  mSqLiteDatabase.rawQuery("select * from table_plant", null);
        String[] headers = new String[] {DBHelper.NAME};
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        System.out.println("Найдено элементов: " + String.valueOf(userCursor.getCount()));
        System.out.println(userAdapter);
        mList.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключения
        db.close();
        userCursor.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.start) {
            startService(new Intent(this, MyService.class));
        }else if (id == R.id.stop) {
            stopService(new Intent(this, MyService.class));
        }else if (id == R.id.notification){
            Context context = getApplicationContext();
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            Resources res = context.getResources();
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_add_white_48dp)
                    .setPriority(Notification.PRIORITY_HIGH)
                    // большая картинка
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_announcement_black_48dp))
                    .setTicker(res.getString(R.string.notification)) // текст в строке состояния
                    .setTicker("Последнее китайское предупреждение!")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                    .setContentTitle("Напоминание")
                    //.setContentText(res.getString(R.string.notifytext))
                    .setContentText("Пора покормить кота"); // Текст уведомления

            Notification notification = builder.build();
            notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, notification);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else  if (id == R.id.allEvents){
            Intent intentAll = new Intent(MainActivity.this, all_type.class);
            startActivity(intentAll);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void add (View view){
        Intent intent = new Intent(MainActivity.this, add.class);
        startActivity(intent);
    }
}
