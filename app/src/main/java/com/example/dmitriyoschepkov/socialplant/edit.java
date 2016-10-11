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
import android.widget.EditText;
import android.widget.Toast;

public class edit extends AppCompatActivity {
    public int id;
    public int positionEdit;
    EditText editName, editAbout;
    public DBHelper mDatabaseHelper;
    public SQLiteDatabase mSqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Редактирование");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        positionEdit = getIntent().getIntExtra("position", 0);
        System.out.println("Position:  " + positionEdit);
        //делаем соотношение с айди
        id = positionEdit + 1;
        editName = (EditText)findViewById(R.id.editName);
        editAbout = (EditText)findViewById(R.id.editAbout);
        String select = "select * from table_plant where _id = '"+id+"';";
        mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.rawQuery(select, null);
        cursor.moveToFirst();
        String select_name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
        String select_about = cursor.getString(cursor.getColumnIndex(DBHelper.ABOUT));
        editName.setText(select_name);
        editAbout.setText(select_about);
        cursor.close();
    }
    public void editProfile(View view){
        String newName = editName.getText().toString();
        String newAbout = editAbout.getText().toString();
        String updateName = "update table_plant set name ='"+newName+"' where _id="+id;
        String updateAbout = "update table_plant set about ='"+newAbout+"' where _id="+id;
        mSqLiteDatabase.execSQL(updateName);
        mSqLiteDatabase.execSQL(updateAbout);
        mSqLiteDatabase.close();
        Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentBack);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Отредактировано",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
