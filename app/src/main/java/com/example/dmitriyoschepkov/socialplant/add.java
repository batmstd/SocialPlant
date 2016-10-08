package com.example.dmitriyoschepkov.socialplant;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class add extends AppCompatActivity {
    public DBHelper mDatabaseHelper;
    public SQLiteDatabase mSqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        setTitle("Создание");
    }
    public void add(View view){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        EditText name = (EditText)findViewById(R.id.name);
        EditText about = (EditText)findViewById(R.id.about);
        String name_plant = name.getText().toString();
        String about_plant = about.getText().toString();
        String img = "NULL";
        String insert_new_plant = "insert into "
                + DBHelper.TABLE_PLANT + " ("
                + DBHelper.DATE_CREATE + ", "
                + DBHelper.NAME + ", "
                + DBHelper.IMAGE + ", "
                + DBHelper.ABOUT + ") "
                + "values ('"
                + dateFormat.format(currentDate)+ "', '"
                + name_plant+ "', '"
                + img + "', '"
                + about_plant +"');";
        mSqLiteDatabase.execSQL(insert_new_plant);
        System.out.println("insert to DB: "+insert_new_plant);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Добавлено",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void photo (View v){
        startService(new Intent(this, MyService.class));
    }
}
