package com.example.dmitriyoschepkov.socialplant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {
    public DBHelper mDatabaseHelper;
    public SQLiteDatabase mSqLiteDatabase;
    private TextView about;
    private TextView date_create;

    public int position;
    public int id, idUpdate;
    public String select_id, sendSelect_id;
    ListView typeListProfile;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    final String LOG_TAG = "myLogs";
    private GoogleApiClient client;
    int positionUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        about = (TextView) findViewById(R.id.profile_about);
        date_create = (TextView) findViewById(R.id.date_create);
        //передаем позицию из списка
        position = getIntent().getIntExtra("position", 0);
        System.out.println("Position: " + position);
        //делаем соотношение с айди
        id = position + 1;
        idUpdate = position + 1;
        //идем в бд за данными
        //String select = "select * from table_plant where _id = " + id;
        String select = "select * from table_plant";
        mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.rawQuery(select, null);
        cursor.moveToPosition(position);
        String select_date = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_CREATE));
        String select_name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
        String select_image = cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE));
        String select_about = cursor.getString(cursor.getColumnIndex(DBHelper.ABOUT));
        select_id = cursor.getString(cursor.getColumnIndex(DBHelper._ID));
        sendSelect_id = select_id;
        String text = select_id + ", " + select_date + ", " + select_name + ", " + select_image + ", " + select_about;
        System.out.println(text);
        String text_about = select_about;
        ImageView imageEdit = (ImageView) findViewById(R.id.backdrop);
        if (select_image != null) {
            Context context = getApplicationContext();
            Picasso.Builder picassoBuilder = new Picasso.Builder(context);
            Picasso picasso = picassoBuilder.build();
            picasso.load(Uri.parse("file://" + select_image))
                    .fit()
                    .placeholder(R.drawable.plant)
                    .error(R.drawable.plant)
                    .centerInside()
                    .into(imageEdit);
        } else if (select_image == null) {
            imageEdit.setImageResource(R.drawable.plant);
        }
        //заполняем данные
        setTitle(select_name);
        about.setText(text_about);
        date_create.setText(" " + select_date);
        cursor.close();
//
        typeListProfile = (ListView) findViewById(R.id.typeListProfile);
        userCursor = mSqLiteDatabase.rawQuery("Select * " +
                "FROM table_activity " +
                "INNER JOIN table_plant " +
                "ON table_plant._ID=table_activity.id_plant " +
                "INNER JOIN table_type " +
                "ON table_activity.type=table_type._ID " +
                "where id_plant = " +
                select_id +
                " and actual is not 1 order by date_type;", null);
        System.out.println("Найдено элементов: " + String.valueOf(userCursor.getCount()));
        //typeListProfile.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final String[] headers = new String[]{DBHelper.PROP_VALUE, DBHelper.DATE_TYPE};
        userAdapter = new SimpleCursorAdapter(this, R.layout.card_for_profile_type,
              userCursor, headers, new int[]{R.id.textType, R.id.textDate}, 0);

        System.out.println(userAdapter);
        typeListProfile.setAdapter(userAdapter);

        typeListProfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long checkId) {
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
                        + checkId);
                positionUpdate = Integer.valueOf(userCursor.getCount());
                userCursor.moveToPosition(position);
                System.out.println("POS: "+positionUpdate);
                String select_id_to_update = userCursor.getString(userCursor.getColumnIndex(DBHelper._ID));
                System.out.println("POSPOS: "+select_id_to_update);
                AlertDialog.Builder complete = new AlertDialog.Builder(profile.this);
                complete.setTitle("Отметить событие как выполненное?");
                complete.setMessage("Это событие будет помечено как выполненное и далее недоступное в списке событий.");
                complete.setPositiveButton("подтверждаю", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        positionUpdate = Integer.valueOf(userCursor.getCount());
                        userCursor.moveToPosition(position);
                        System.out.println("POS: "+positionUpdate);
                        String select_id_to_update = userCursor.getString(userCursor.getColumnIndex(DBHelper._ID));
                        System.out.println("POSPOS: "+select_id_to_update);
                        String updateActual = "update table_activity set actual = 1 where _ID = "+select_id_to_update+";";
                        mDatabaseHelper = new DBHelper(profile.this, "plant.db", null, DBHelper.DATABASE_VERSION);
                        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
                        mSqLiteDatabase.execSQL(updateActual);
                        System.out.println(updateActual);
                        Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intentBack);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Событие помечено как выполненое :)",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        //mSqLiteDatabase.close();
                    }
                });
                complete.setNeutralButton("отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                complete.show();

            }
        });
        setListViewHeightBasedOnChildren(typeListProfile);
        typeListProfile.setDivider(null);

        //mSqLiteDatabase.close();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, Toolbar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void profile_add_type(View view) {
        Intent intent = new Intent(getApplicationContext(), add_type.class);
        intent.putExtra("id", position);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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
        } else if (id == R.id.editProfile) {
            Intent intentEdit = new Intent(getApplicationContext(), edit.class);
            intentEdit.putExtra("sendSelect_id", position);
            System.out.println("sendId: " + position);
            startActivity(intentEdit);
        } else if (id == android.R.id.home) {

        } else if (id == R.id.deleteProfile) {

            AlertDialog.Builder complete = new AlertDialog.Builder(profile.this);
            complete.setTitle("Вы действительно хотите удалить?");
            complete.setMessage("восстановить данные будет невозможно, подтвердите удаление");
            complete.setPositiveButton("удалить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String liveNo = "delete from table_plant where _id = " + select_id;
                    mDatabaseHelper = new DBHelper(profile.this, "plant.db", null, DBHelper.DATABASE_VERSION);
                    mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
                    mSqLiteDatabase.execSQL(liveNo);
                    System.out.println(liveNo);
                    Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentBack);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Удалено :(",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    //mSqLiteDatabase.close();
                }
            });
            complete.setNeutralButton("отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            complete.show();


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "profile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.dmitriyoschepkov.socialplant/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "profile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.dmitriyoschepkov.socialplant/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}
