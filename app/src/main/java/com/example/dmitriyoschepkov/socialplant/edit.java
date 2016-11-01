package com.example.dmitriyoschepkov.socialplant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;

public class edit extends AppCompatActivity implements
        android.view.View.OnClickListener {
    public int id_plant;
    public int select_id;
    EditText editName, editAbout;
    public DBHelper mDatabaseHelper;
    public SQLiteDatabase mSqLiteDatabase;
    private Button loadButton;
    private static final int REQUEST = 1;
    public  String filePath, select_id_for_update, select_image;
    private ImageView image1;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        setTitle("Редактирование");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
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
        select_id = getIntent().getIntExtra("sendSelect_id", 0);
        System.out.println("sendSelect_id:  " + select_id);
        //делаем соотношение с айди
        //id_plant = select_id + 1;
        editName = (EditText)findViewById(R.id.editName);
        editAbout = (EditText)findViewById(R.id.editAbout);

        //String select = "select * from table_plant where _id = '"+select_id+"';";
        String select = "select * from table_plant";
        mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.rawQuery(select, null);
        cursor.moveToPosition(select_id);
        select_id_for_update = cursor.getString(cursor.getColumnIndex(DBHelper._ID));
        String select_name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
        String select_about = cursor.getString(cursor.getColumnIndex(DBHelper.ABOUT));
        select_image = cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE));
        editName.setText(select_name);
        editAbout.setText(select_about);
        String text = select_id + ", " +  ", " + select_name + ", " + select_image + ", " + select_about;
        System.out.println(text);
        ImageView imageEdit = (ImageView)findViewById(R.id.backdrop);
        if (select_image != null){
            Context context = getApplicationContext();
            Picasso.Builder picassoBuilder = new Picasso.Builder(context);
            Picasso picasso = picassoBuilder.build();
            picasso.load(Uri.parse("file://"+select_image))
                    .fit()
                    .placeholder(R.drawable.plant)
                    .error(R.drawable.plant)
                    .centerInside()
                    .into(imageEdit);
            //imageEdit.setImageResource(R.drawable.plant);
        }else if (select_image == null){
            imageEdit.setImageResource(R.drawable.plant);
        }
        cursor.close();
        //loadButton = (Button) findViewById(R.id.editPhoto1);
        fab.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.SaveProfile) {
            System.out.println("получаю" +filePath);
            String imgI;
            if (filePath == "null"){
                imgI=select_image;
            }else imgI = filePath;

            //imgI = filePath;
            String newName = editName.getText().toString();
            String newAbout = editAbout.getText().toString();
            String updateName = "update table_plant set name ='"+newName+"' where _id="+select_id_for_update;
            String updateAbout = "update table_plant set about ='"+newAbout+"' where _id="+select_id_for_update;

            mSqLiteDatabase.execSQL(updateName);
            mSqLiteDatabase.execSQL(updateAbout);

            //mSqLiteDatabase.close();
            System.out.println(updateName+ updateAbout);
            Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intentBack);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Отредактировано",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        AlertDialog.Builder complete = new AlertDialog.Builder(edit.this);
        complete.setMessage("Выберите вариант загрузки:");
        complete.setPositiveButton("Галерея", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, REQUEST);
            }
        });
        complete.setNeutralButton("Камера", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//(Intent.ACTION_PICK);
                startActivityForResult(i, REQUEST);
            }
        });
        complete.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap img = null;
        if (requestCode == REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                img = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            image1 = (ImageView)findViewById(R.id.backdrop);
            image1.setImageBitmap(img);
            System.out.println("original "+selectedImage.toString());
            Intent intentImg = new Intent(getApplicationContext(), edit.class);
            intentImg.putExtra("img", selectedImage.toString());
            Cursor cursor = getContentResolver().query(selectedImage, new String[] {
                    android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            cursor.moveToFirst();
            filePath = cursor.getString(0);
            cursor.close();
            System.out.println("отправляю "+filePath);
            Context context = getApplicationContext();
            Picasso.Builder picassoBuilder = new Picasso.Builder(context);
            Picasso picasso = picassoBuilder.build();
            picasso.load(Uri.parse("file://"+filePath))
                    .fit()
                    .placeholder(R.drawable.plant)
                    .error(R.drawable.plant)
                    .centerInside()
                    .into(image1);
            String updateImage = "update table_plant set image ='"+filePath+"' where _id="+select_id_for_update;
            //mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
           // mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
            mSqLiteDatabase.execSQL(updateImage);
           // mSqLiteDatabase.close();
            System.out.println("обновляю картинку"+filePath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void editProfile(View view){

    }

}
