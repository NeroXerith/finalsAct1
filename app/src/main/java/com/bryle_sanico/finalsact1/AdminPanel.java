package com.bryle_sanico.finalsact1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminPanel extends AppCompatActivity {

    private SQLiteDB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        dbHelper = new SQLiteDB(this); // Initialize your database helper

        ListView listView = findViewById(R.id.listView);

        ArrayList<String> dataList = getDataFromDB(); // Retrieve data from the database

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter); // Set the adapter for the ListView
    }

    private ArrayList<String> getDataFromDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> dataList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " + SQLiteDB.COLUMN_FIRST_NAME + " FROM " + SQLiteDB.TABLE_NAME, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_FIRST_NAME);

            if (columnIndex != -1) {
                while (cursor.moveToNext()) {
                    String firstName = cursor.getString(columnIndex);
                    dataList.add(firstName);
                }
            } else {
                // Handle the case where the column index is -1 (column not found)
                // You can log an error, show a message, or handle it accordingly
            }

            cursor.close();
        }

        return dataList;
    }
}