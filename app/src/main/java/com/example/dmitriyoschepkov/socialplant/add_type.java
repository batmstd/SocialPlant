package com.example.dmitriyoschepkov.socialplant;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class add_type extends AppCompatActivity {
    TextView currentDateTime;
    public DBHelper mDatabaseHelper;
    public SQLiteDatabase mSqLiteDatabase;
    RadioButton type1, type2, type3, type4;
    Calendar dateAndTime=Calendar.getInstance();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        id = getIntent().getIntExtra("id", 0);
        System.out.println("ID: "+id);
        currentDateTime=(TextView)findViewById(R.id.currentDateTime);
        setInitialDateTime();
        setTitle("Добавление события");


    }
    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(add_type.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // установка начальных даты и времени
    public void setInitialDateTime() {
        String currentDate = DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
        currentDateTime.setText(currentDate);
    }
    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    public void add_type(View view){
        String currentDate = DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
        type1 = (RadioButton)findViewById(R.id.type1);
        type2 = (RadioButton)findViewById(R.id.type2);
        type3 = (RadioButton)findViewById(R.id.type3);
        type4 = (RadioButton)findViewById(R.id.type4);
        if (type1.isChecked()){
            String insert = "insert into 'table_activity' (id_plant, type, date_type) values ("+
                    id+", 1, '"+currentDate+"');";
            mSqLiteDatabase.execSQL(insert);
            System.out.println(insert);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Событие добавлено",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else if (type2.isChecked()){
            String insert = "insert into 'table_activity' (id_plant, type, date_type) values ("+
                    id+", 2, '"+currentDate+"');";
            mSqLiteDatabase.execSQL(insert);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Событие добавлено",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else if (type3.isChecked()){
            String insert = "insert into 'table_activity' (id_plant, type, date_type) values ("+
                    id+", 3, '"+currentDate+"');";
            mSqLiteDatabase.execSQL(insert);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Событие добавлено",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else if (type4.isChecked()){
            String insert = "insert into 'table_activity' (id_plant, type, date_type) values ("+
                    id+", 4, '"+currentDate+"');";
            mSqLiteDatabase.execSQL(insert);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Событие добавлено",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

}
