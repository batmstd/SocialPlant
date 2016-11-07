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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class newtype extends AppCompatActivity {
    final String LOG_TAG = "myLogs";
    public DBHelper mDatabaseHelper;
    public SQLiteDatabase mSqLiteDatabase;
    ListView listViewType;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    EditText newType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtype);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("Добавление нового типа событий");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        listViewType = (ListView)findViewById(R.id.listViewType);
        mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        userCursor =  mSqLiteDatabase.rawQuery("Select * FROM table_type;", null);
        Log.d(LOG_TAG, "Найдено элементов: " + String.valueOf(userCursor.getCount()));
        final String[] headers = new String[] {DBHelper.PROP_VALUE};
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                userCursor, headers, new int[]{android.R.id.text1}, 0);
        System.out.println(userAdapter);
        listViewType.setAdapter(userAdapter);
        listViewType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long checkId) {
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
                        + checkId);
            }
        });
        mSqLiteDatabase.close();

    }
    public void newtypeClick (View view){
        newType = (EditText)findViewById(R.id.newType);
        String propValue = newType.getText().toString();
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        String insert = "insert into table_type (PROP_VALUE) values ('"+propValue+"');";
        mSqLiteDatabase.execSQL(insert);
        Intent intent_update = getIntent();
        finish();
        startActivity(intent_update);
    }

}
