package com.example.qthjen.sqlitesaveimage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainAdd extends AppCompatActivity {

    private EditText etItems, etDescription;
    private ImageView ivCamera, ivFile, ivImage;
    private Button btAdd, btCancel;
    final int REQUEST_CODE_CAMERA = 1;
    final int REQUEST_CODE_FOLDER = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        FindView();

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                /** hỏi quyền người dùng **/
                ActivityCompat.requestPermissions(MainAdd.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);

            }
        });

        ivFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, REQUEST_CODE_FOLDER);
                /** hỏi quyền người dùng **/
                ActivityCompat.requestPermissions(MainAdd.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_FOLDER);

            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /** chuyển data image view sang byte[] **/
                BitmapDrawable bitmapDrawable = (BitmapDrawable) ivImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                /** convert bitmap sang byte với định dạng png chất lượng == 100 **/
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                byte[] imagee = byteArray.toByteArray();

                MainActivity.dataBase.INSERT_ITEMS(
                        etItems.getText().toString().trim(),
                        etDescription.getText().toString().trim(),
                        imagee);

                Toast.makeText(MainAdd.this, "Done", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainAdd.this, MainActivity.class));

            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainAdd.this, MainActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ( requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivImage.setImageBitmap(bitmap);

        }

        if ( requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ivImage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void FindView() {

        etItems       = (EditText)  findViewById(R.id.etItems);
        etDescription = (EditText)  findViewById(R.id.etDescription);
        ivCamera      = (ImageView) findViewById(R.id.ivCamera);
        ivFile        = (ImageView) findViewById(R.id.ivFile);
        ivImage       = (ImageView) findViewById(R.id.ivImage);
        btAdd         = (Button)    findViewById(R.id.btAdd);
        btCancel      = (Button)    findViewById(R.id.btCancel);

    }

    /** bắt sự kiện alow deny khi hỏi quyền người dùng **/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch ( requestCode ) {

            case REQUEST_CODE_CAMERA:
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Alow
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                } else { // Deny
                    Toast.makeText(MainAdd.this, "You have not given permission", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CODE_FOLDER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_FOLDER);
                } else {
                    Toast.makeText(MainAdd.this, "You have not given permission", Toast.LENGTH_SHORT).show();
                }
                break;

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
