package com.example.dmitriyoschepkov.socialplant;

import android.app.DatePickerDialog;
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
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class add_type extends AppCompatActivity {
    TextView currentDateTime;
    public DBHelper mDatabaseHelper;
    public SQLiteDatabase mSqLiteDatabase;
    Calendar dateAndTime=Calendar.getInstance();
    private int id;
    private String select_id_for_add_type;
    ListView listType;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    final String LOG_TAG = "myLogs";
    public Long typeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);
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
        mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        id = getIntent().getIntExtra("id", 0);
        System.out.println("select_id: "+id);
        String select = "select * from table_plant";
        mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.rawQuery(select, null);
        cursor.moveToPosition(id);
        select_id_for_add_type = cursor.getString(cursor.getColumnIndex(DBHelper._ID));
        currentDateTime=(TextView)findViewById(R.id.currentDateTime);
        setInitialDateTime();
        setTitle("Добавление события");
        listType = (ListView)findViewById(R.id.listType);
        //
        userCursor =  mSqLiteDatabase.rawQuery("Select * FROM table_type;", null);
        System.out.println("Найдено элементов: " + String.valueOf(userCursor.getCount()));
        listType.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final String[] headers = new String[] {DBHelper.PROP_VALUE};
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_single_choice,
                userCursor, headers, new int[]{android.R.id.text1}, 0);

        System.out.println(userAdapter);
        listType.setAdapter(userAdapter);

        listType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long checkId) {
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
                        + checkId);
               typeId = checkId;
            }
        });
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
        //
        System.out.println("ID: " + typeId);
        String insertIntoActivity = "insert into table_activity (id_plant, type, date_type, actual) values ("+
                select_id_for_add_type+", "+typeId+", '"+currentDate+"', 0);";
        mSqLiteDatabase.execSQL(insertIntoActivity);
        System.out.println(insertIntoActivity);
        Toast toastAdd = Toast.makeText(getApplicationContext(),
                "Событие добавлено",
                Toast.LENGTH_SHORT);
        toastAdd.setGravity(Gravity.CENTER, 0, 0);
        toastAdd.show();
    }

}
