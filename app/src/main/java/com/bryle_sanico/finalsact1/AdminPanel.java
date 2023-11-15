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

        dbHelper = new SQLiteDB(this); // Initialize database helper

        ListView listView = findViewById(R.id.listView);

        ArrayList<String> dataList = getDataFromDB(); // Retrieve data from the database

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter); // Set the adapter for the ListView
    }

    private ArrayList<String> getDataFromDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> dataList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " + SQLiteDB.COLUMN_ID + ", " + SQLiteDB.COLUMN_FIRST_NAME + ", " +
                SQLiteDB.COLUMN_MIDDLE_NAME + ", " + SQLiteDB.COLUMN_LAST_NAME + " FROM " + SQLiteDB.TABLE_NAME, null);

        if (cursor != null) {
            int idColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_ID);
            int firstNameColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_FIRST_NAME);
            int middleNameColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_MIDDLE_NAME);
            int lastNameColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_LAST_NAME);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                String firstName = cursor.getString(firstNameColumnIndex);
                String middleName = cursor.getString(middleNameColumnIndex);
                String lastName = cursor.getString(lastNameColumnIndex);

                String data =   id + "\t\t\t" + firstName + " " + middleName + " " + lastName;
                dataList.add(data);
            }

            cursor.close();
        }

        return dataList;
    }
}
