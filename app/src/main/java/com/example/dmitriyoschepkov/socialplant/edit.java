package com.example.dmitriyoschepkov.socialplant;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
    public int positionEdit;
    EditText editName, editAbout;
    public DBHelper mDatabaseHelper;
    public SQLiteDatabase mSqLiteDatabase;
    private Button loadButton;
    private static final int REQUEST = 1;
    public  String filePath;
    private ImageView image1;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        setTitle("Редактирование");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        positionEdit = getIntent().getIntExtra("position", 0);
        System.out.println("Position:  " + positionEdit);
        //делаем соотношение с айди
        id_plant = positionEdit + 1;
        editName = (EditText)findViewById(R.id.editName);
        editAbout = (EditText)findViewById(R.id.editAbout);
        ImageView imageEdit = (ImageView)findViewById(R.id.editImage);
        String select = "select * from table_plant where _id = '"+id_plant+"';";
        mDatabaseHelper = new DBHelper(this, "plant.db", null, DBHelper.DATABASE_VERSION);
        mSqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.rawQuery(select, null);
        cursor.moveToFirst();
        String select_name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
        String select_about = cursor.getString(cursor.getColumnIndex(DBHelper.ABOUT));
        String select_image = cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE));
        editName.setText(select_name);
        editAbout.setText(select_about);
        Context context = getApplicationContext();
        Picasso.Builder picassoBuilder = new Picasso.Builder(context);
        Picasso picasso = picassoBuilder.build();
        picasso.load(Uri.parse("file://"+select_image))
                .fit()
                .placeholder(R.drawable.plant)
                .error(R.drawable.plant)
                .centerInside()
                .into(imageEdit);
        cursor.close();
        loadButton = (Button) findViewById(R.id.editPhoto1);
        loadButton.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.SaveProfile) {
            System.out.println("plz2 "+filePath);
            String img;
            //System.out.println("plz "+selectedImage.toString());
            img = filePath;
            String newName = editName.getText().toString();
            String newAbout = editAbout.getText().toString();
            String updateName = "update table_plant set name ='"+newName+"' where _id="+id_plant;
            String updateAbout = "update table_plant set about ='"+newAbout+"' where _id="+id_plant;
            String updateImage = "update table_plant set image ='"+img+"' where _id="+id_plant;
            mSqLiteDatabase.execSQL(updateName);
            mSqLiteDatabase.execSQL(updateAbout);
            mSqLiteDatabase.execSQL(updateImage);
            mSqLiteDatabase.close();
            System.out.println(updateName+ updateAbout+ updateImage);
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
                img = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            image1 = (ImageView)findViewById(R.id.editImage);
            image1.setImageBitmap(img);
            System.out.println("original "+selectedImage.toString());
            Intent intentImg = new Intent(getApplicationContext(), edit.class);
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
    public void editProfile(View view){

    }

}
