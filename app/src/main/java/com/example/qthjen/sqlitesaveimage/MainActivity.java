package com.example.qthjen.sqlitesaveimage;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btAdd;
    public static DataBase dataBase;
    private ArrayList<Items> listItem;
    private CustomList customList;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAdd = (Button) findViewById(R.id.btAdd);
        lv = (ListView) findViewById(R.id.lv);

        dataBase = new DataBase(MainActivity.this, "manager.sqlite", null, 1);

        dataBase.QueryData("CREATE TABLE IF NOT EXISTS Items(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR, Description VARCHAR, Image BLOB)");

        listItem = new ArrayList<Items>();
        customList = new CustomList(MainActivity.this, R.layout.custom_list, listItem);

        Cursor cursor = dataBase.GetData("SELECT * FROM Items");
        while ( cursor.moveToNext() ) {
            listItem.add(new Items(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3)));
        }

        customList.notifyDataSetChanged();
        lv.setAdapter(customList);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainAdd.class));
            }
        });

    }
}
