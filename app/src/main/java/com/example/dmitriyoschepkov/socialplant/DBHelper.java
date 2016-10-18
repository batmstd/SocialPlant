package com.example.dmitriyoschepkov.socialplant;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;



public class DBHelper extends SQLiteOpenHelper implements BaseColumns {


    //name BD
    private static final String DATABASE_NAME = "social.db";
    //version
    public static final int DATABASE_VERSION = 5;


    //name  table
    public static final String TABLE_PLANT = "table_plant";
    public static final String TABLE_TYPE = "table_type";
    public static final String TABLE_ACTIVITY = "table_activity";
    public static final String TABLE_STATUS = "table_status";
    //name column
    //table PLANT
    public static final String DATE_CREATE = "date_create";
    public static final String NAME = "name";
    public static final String IMAGE = "image";
    public static final String ABOUT = "about";
    //status
    public static final String STATUS = "status";
    // table activity
    public static final String ID_plant = "id_plant";
    public static final String TYPE = "type";
    public static final String DATE_TYPE = "date_type";
    public static final String ACTUAL = "actual";
    //table TYPE
    public static final String PROP_VALUE = "prop_value";
    public static final String PROP_NAME = "prop_name";
    //create
    private static final String DATABASE_CREATE_SCRIPT_PLANT = "create table "
            + TABLE_PLANT
            + " (" +BaseColumns._ID + " integer primary key autoincrement, "
            + DATE_CREATE + " CHAR, "
            + NAME + " CHAR, "
            + IMAGE + " CHAR, "
            + ABOUT + " CHAR, "
            + STATUS + " CHAR);";
    private static final String DATABASE_CREATE_SCRIPT_ACTIVITY = "create table "
            + TABLE_ACTIVITY+ " ("
            +BaseColumns._ID + " integer primary key autoincrement, "
            + ID_plant + " int, "
            + TYPE + " int, "
            + ACTUAL + " int, "
            + DATE_TYPE + " CHAR);";
    private static final String DATABASE_CREATE_SCRIPT_TYPE = "create table "
            + TABLE_TYPE+ " ("
            + PROP_NAME + " int, "
            + PROP_VALUE + " CHAR);";
    private static final  String INSERT_TABLE_TYPE = "insert INTO "
            + TABLE_TYPE+ " ("
            +PROP_NAME
            +", "
            +PROP_VALUE
            +") values (1, 'Поливка'), (2, 'Пересадка'), (3, 'Протирание листьев'), (4, 'Удобрение');";
    private static final String DATABASE_CREATE_SCRIPT_STATUS = "create table "
            + TABLE_STATUS+ " ("
            + ID_plant + " int, "
            + STATUS + " CHAR);";

    DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

   public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler){
        super(context, name, factory, version, errorHandler);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE_SCRIPT_PLANT);
        db.execSQL(DATABASE_CREATE_SCRIPT_ACTIVITY);
        db.execSQL(DATABASE_CREATE_SCRIPT_TYPE);
        db.execSQL(INSERT_TABLE_TYPE);
        db.execSQL(DATABASE_CREATE_SCRIPT_STATUS);
        System.out.println("Create DB: "+DATABASE_CREATE_SCRIPT_PLANT);
        System.out.println("Create DB: "+DATABASE_CREATE_SCRIPT_ACTIVITY);
        System.out.println("Create DB: "+DATABASE_CREATE_SCRIPT_TYPE);
        System.out.println("Insert to DB: "+INSERT_TABLE_TYPE);
        System.out.println("Create DB: "+DATABASE_CREATE_SCRIPT_STATUS);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);

        // Создаём новую таблицу
        onCreate(db);
    }
    @Override
    public void onDowngrade (SQLiteDatabase db, int oldVersion, int newVersion){

    }
}



