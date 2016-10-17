package com.example.dmitriyoschepkov.socialplant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
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

public class add_profile extends AppCompatActivity implements
        android.view.View.OnClickListener {

    private Button loadButton;
    private ImageView image;
    private static final int REQUEST = 1;
    DBHelper mDatabaseHelper;
    DBHelper sqlHelper;
    SQLiteDatabase mSqLiteDatabase;
    private Uri selectedImage;
    public  String filePath;
    private static final int WRITE_PERMISSION = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        image = (ImageView) findViewById(R.id.imageView1);
        loadButton = (Button) findViewById(R.id.editPhoto1);
        ImageView image = (ImageView)findViewById(R.id.imageView);
        setTitle(" ");
        loadButton.setOnClickListener(this);
        requestWritePermission();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == WRITE_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "вы должны разрешить доступ к файлам", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void requestWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
       if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
     ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
 }
    }
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_profile, menu);
        return true;
    }


    @Override
    public void onClick(View v) {

        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap img = null;

        if (requestCode == REQUEST && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();
            try {
                img = Media.getBitmap(getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.setImageBitmap(img);
            System.out.println("original "+selectedImage.toString());
            Intent intentImg = new Intent(getApplicationContext(), add_profile.class);
            intentImg.putExtra("img", selectedImage.toString());
            //startActivityForResult(intentImg,1);
            Cursor cursor = getContentResolver().query(selectedImage, new String[] {
                    android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            cursor.moveToFirst();
            filePath = cursor.getString(0);
            cursor.close();
            System.out.println("plz "+filePath);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("plz2 "+filePath);
        int id = item.getItemId();
        if (id == R.id.addNewProfile){
            String img;
            //System.out.println("plz "+selectedImage.toString());
            img = filePath;
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = null;
            dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
            mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
            EditText name = (EditText)findViewById(R.id.addName);
            EditText about = (EditText)findViewById(R.id.addAbout);
            String name_plant = name.getText().toString();
            String about_plant = about.getText().toString();
            System.out.println(img);
            //String img = image.toString();

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
            Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intentBack);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Добавлено",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            mSqLiteDatabase.close();
        }
        return super.onOptionsItemSelected(item);
    }
}