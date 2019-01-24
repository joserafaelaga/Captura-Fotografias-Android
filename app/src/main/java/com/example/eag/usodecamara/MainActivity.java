package com.example.eag.usodecamara;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagen = (ImageView) findViewById(R.id.imagen);

        //Pedimos permisos

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},1000);
        }
    }

    String mCurrentPhotoPath;

    //Método para crear archivo temporal de imagen

    private File createImageFile() throws IOException{

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "Backup" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName,".jpg",storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;

    }

    static final int REQUEST_TAKE_PHOTO = 1;

    public void tomarFoto(View view){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Validar si se ha tomado la fotografía previamente

        if(takePictureIntent.resolveActivity(getPackageManager()) != null){

            //Crear archivo de imagen

            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Si se ha creado el archivo correctamente hace lo siguiente

            if(photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);

                //Tomar fotografía

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri.toString());

                //Lanzamos nuestro Intent
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }


        }

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    protected void onActivityResult (int requestCode, int resultCode, Intent data){

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();

            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imagen.setImageBitmap(imageBitmap);
        }

    }

}
