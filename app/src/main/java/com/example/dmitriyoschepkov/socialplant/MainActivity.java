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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DBHelper mDatabaseHelper;
    DBHelper sqlHelper;
    SQLiteDatabase mSqLiteDatabase;
    ListView mList;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    final String LOG_TAG = "myLogs";
    NotificationManager nm;
    ArrayList<ListPlants> listPlants = new ArrayList<ListPlants>();
    ListPlantsAdapter listPlantsAdapter;

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
        //
        fillData();
        listPlantsAdapter = new ListPlantsAdapter(this, listPlants);
        ListView lvMain = (ListView)findViewById(R.id.list);
        lvMain.setAdapter(listPlantsAdapter);

    }
    void fillData(){
        db = sqlHelper.getReadableDatabase();
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        userCursor =  mSqLiteDatabase.rawQuery("select * from table_plant where live = 'yes'", null);
        String[] headers = new String[] {DBHelper.NAME, DBHelper.ABOUT, DBHelper.IMAGE};
        userAdapter = new SimpleCursorAdapter(this, R.layout.main_cards_list_view,
                userCursor, headers, new int[]{R.id.namePlant, R.id.aboutPlant, R.id.imageView2}, 0);
        System.out.println("Найдено элементов: " + String.valueOf(userCursor.getCount()));
        System.out.println(userAdapter);
        userCursor.moveToFirst();
        String namePlant;
        String imagePlant;
        String aboutPlant;
        while (userCursor.isAfterLast()==false){
            namePlant = userCursor.getString(userCursor.getColumnIndex(DBHelper.NAME));
            imagePlant = userCursor.getString(userCursor.getColumnIndex(DBHelper.IMAGE));
            aboutPlant = userCursor.getString(userCursor.getColumnIndex(DBHelper.ABOUT));
            listPlants.add(new ListPlants(namePlant+" ", imagePlant, aboutPlant));
            userCursor.moveToNext();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = sqlHelper.getReadableDatabase();
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        //получаем данные из бд
        userCursor =  mSqLiteDatabase.rawQuery("select * from table_plant", null);
        String[] headers = new String[] {DBHelper.NAME, DBHelper.IMAGE};
        userAdapter = new SimpleCursorAdapter(this, R.layout.main_cards_list_view,
                userCursor, headers, new int[]{R.id.namePlant, R.id.imageView2}, 0);
        System.out.println("Найдено элементов: " + String.valueOf(userCursor.getCount()));
        System.out.println(userAdapter);
        mList.setDivider(null);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
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
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.allEvents){
            Intent intentAll = new Intent(MainActivity.this, allEvents.class);
            startActivity(intentAll);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void add (View view){
        Intent intent = new Intent(MainActivity.this, add_profile.class);
        startActivity(intent);
    }

}

