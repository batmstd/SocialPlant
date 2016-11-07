package com.example.dmitriyoschepkov.socialplant;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class settings extends AppCompatActivity {
    ListView listViewSettings;
    final String LOG_TAG = "myLogs";
    DBHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
        setTitle("Настройки");
        mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
        listViewSettings = (ListView)findViewById(R.id.listViewSettings);
        final String[] listSettings = getResources().getStringArray(R.array.list_settings);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listSettings);
        listViewSettings.setAdapter(adapter);
        listViewSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
                    + id);
            switch (position){
                case 0:
                    Log.d(LOG_TAG, "Выбран 1ый пункт меню: Добавление списка событий");
                    Intent intent_newtype = new Intent(settings.this, newtype.class);
                    finish();
                    startActivity(intent_newtype);
                    break;
                case 1:
                    Log.d(LOG_TAG, "Выбран 2ой пункт меню: Начать все с чистого листа");
                    Log.d(LOG_TAG, "Вызываю диалоговое окно");
                    AlertDialog.Builder complete = new AlertDialog.Builder(settings.this);
                    complete.setTitle("Вы действительно хотите все удалить?");
                    complete.setMessage("Все данные будут удалены. Растения и события более не будут доступны.");
                    complete.setPositiveButton("подтверждаю", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String delete = "delete from table_plant; " +
                                    "delete from table_activity; " +
                                    "delete from table_type; " +
                                    "delete from table_status;";
                            mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
                            mSqLiteDatabase.execSQL(delete);
                            Log.d(LOG_TAG, "Выполняется удаление: "+delete);
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Все стерто, начните с чистого листа :)",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            mSqLiteDatabase.close();
                            Intent intent_update = new Intent(settings.this, MainActivity.class);
                            finish();
                            startActivity(intent_update);
                        }
                    });
                    complete.setNeutralButton("отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(LOG_TAG, "отмена");
                            finish();
                        }
                    });
                    complete.show();
                    break;
            }

        }
    });
    }

}
