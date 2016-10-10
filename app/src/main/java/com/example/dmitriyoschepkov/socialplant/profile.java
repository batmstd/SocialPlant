package com.example.dmitriyoschepkov.socialplant;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class profile extends AppCompatActivity {
    public DBHelper mDatabaseHelper;
    public SQLiteDatabase mSqLiteDatabase;
    private TextView about;
    private TextView date_create;
    TextView type1, type2, type3, type4, type5;
    public  int position;
    public  int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        about = (TextView) findViewById(R.id.profile_about);
        type1 = (TextView) findViewById(R.id.type1);
        type2 = (TextView) findViewById(R.id.type2);
        type3 = (TextView) findViewById(R.id.type3);
        type4 = (TextView) findViewById(R.id.type4);
        date_create = (TextView) findViewById(R.id.date_create);
        //передаем позицию из списка
        position = getIntent().getIntExtra("position", 0);
        System.out.println("Position: " + position);
        //делаем соотношение с айди
        id = position + 1;
        //идем в бд за данными
        String select = "select * from table_plant where _id = " + id;
        mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.rawQuery(select, null);
        cursor.moveToFirst();
        String select_date = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_CREATE));
        String select_name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
        String select_image = cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE));
        String select_about = cursor.getString(cursor.getColumnIndex(DBHelper.ABOUT));
        String select_id = cursor.getString(cursor.getColumnIndex(DBHelper._ID));
        String text = select_id + ", " + select_date + ", " + select_name + ", " + select_image + ", " + select_about;
        System.out.println(text);
        String text_about = select_about;
        //заполняем данные
        setTitle(select_name);
        about.setText(text_about);
        date_create.setText(" " + select_date);
        cursor.close();

        //заполняем события
        //type1
        String selectType1 = "select * from table_activity where type = 1 and id_plant = " + id;
        Cursor typeCursor1 = mSqLiteDatabase.rawQuery(selectType1, null);
        int strok1 = typeCursor1.getCount();
        System.out.println("Строк для тайп 1 и айди " + id + " равно: " + strok1);
        if (strok1 == 0) {
            type1.setText("Событий нет.");
        } else if (strok1 > 0) {
            typeCursor1.moveToLast();
            String settype1 = typeCursor1.getString(typeCursor1.getColumnIndex(DBHelper.DATE_TYPE));
            type1.setText(" "+settype1);
        }

        //type2
        String selectType2 = "select * from table_activity where type = 2 and id_plant  = " + id;
        Cursor typeCursor2 = mSqLiteDatabase.rawQuery(selectType2, null);
        int strok2 = typeCursor2.getCount();
        System.out.println("Строк для тайп 1 и айди " + id + " равно: " + strok2);
        if (strok2 == 0) {
            type2.setText("Событий нет.");
        } else if (strok2 > 0) {
            typeCursor2.moveToLast();
            String settype2 = typeCursor2.getString(typeCursor2.getColumnIndex(DBHelper.DATE_TYPE));
            type2.setText(" "+settype2);
        }
        //type3
        String selectType3 = "select * from table_activity where type = 3 and id_plant  = " + id;
        Cursor typeCursor3 = mSqLiteDatabase.rawQuery(selectType3, null);
        int strok3 = typeCursor3.getCount();
        System.out.println("Строк для тайп 1 и айди " + id + " равно: " + strok3);
        if (strok3 == 0) {
            type3.setText("Событий нет.");
        } else if (strok3 > 0) {
            typeCursor3.moveToLast();
            String settype3 = typeCursor3.getString(typeCursor3.getColumnIndex(DBHelper.DATE_TYPE));
            type3.setText(" "+settype3);
        }
        //type4
        String selectType4 = "select * from table_activity where type = 4 and id_plant  = " + id;
        Cursor typeCursor4 = mSqLiteDatabase.rawQuery(selectType4, null);
        int strok4 = typeCursor4.getCount();
        System.out.println("Строк для тайп 1 и айди " + id + " равно: " + strok4);
        if (strok4 == 0) {
            type4.setText("Событий нет.");
        } else if (strok4 > 0) {
            typeCursor4.moveToLast();
            String settype4 = typeCursor4.getString(typeCursor4.getColumnIndex(DBHelper.DATE_TYPE));
            type4.setText(" "+settype4);
        }
        typeCursor1.close();
        typeCursor2.close();
        typeCursor3.close();
        typeCursor4.close();

    }
    public void profile_add_type(View view){
        Intent intent = new Intent(getApplicationContext(), add_type.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
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
        if (id == R.id.update) {
            Intent intent_update = getIntent();
            finish();
            startActivity(intent_update);
        }else  if (id == R.id.editProfile){
            Intent intentEdit = new Intent(getApplicationContext(), edit.class);
            intentEdit.putExtra("position", position);
            startActivity(intentEdit);
        }

        return super.onOptionsItemSelected(item);
    }

}
