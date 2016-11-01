package com.example.dmitriyoschepkov.socialplant;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Calendar;

public class allEvents extends AppCompatActivity {
    DBHelper mDatabaseHelper;
    DBHelper sqlHelper;
    SQLiteDatabase mSqLiteDatabase;
    ListView mListAll;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    final String LOG_TAG = "myLogs";
    Calendar dateAndTime=Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Будующие события");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
        mListAll = (ListView)findViewById(R.id.mListAll);
        /*mListAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "+id);
            }
        });*/
        sqlHelper = new DBHelper(getApplicationContext());
    }
    @Override
    public void onResume() {
        String currentDate = DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
        System.out.println(currentDate);

        super.onResume();
        // открываем подключение
        db = sqlHelper.getReadableDatabase();
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        //получаем данные из бд
        userCursor =  mSqLiteDatabase.rawQuery("Select * " +
                "FROM table_activity " +
                "INNER JOIN table_plant " +
                "ON table_plant._ID=table_activity.id_plant " +
                "INNER JOIN table_type " +
                "ON table_activity.type=table_type.prop_name " +
                "where actual is not 1 order by date_type;", null);
        System.out.println("Найдено элементов: " + String.valueOf(userCursor.getCount()));
        String[] headers = new String[] {DBHelper.DATE_TYPE, DBHelper.NAME, DBHelper.PROP_VALUE};
        userAdapter = new SimpleCursorAdapter(this, R.layout.card_for_all_events,
                userCursor, headers, new int[]{R.id.textDate,R.id.textName, R.id.textType}, 0);

        System.out.println(userAdapter);
        mListAll.setAdapter(userAdapter);
        mListAll.setDivider(null);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключения
        db.close();
        userCursor.close();
    }
}
