package com.bryle_sanico.finalsact1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SQLiteDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "registered_user";
    private static final int DATABASE_VERSION = 1;

    static final String TABLE_NAME = "Users";
    static final String COLUMN_ID = "id";
    static final String COLUMN_FIRST_NAME = "first_name";
    static final String COLUMN_MIDDLE_NAME = "middle_name";
    static final String COLUMN_LAST_NAME = "last_name";
    static final String COLUMN_AGE = "age";
    static final String COLUMN_CONTACT = "contact_number";
    static final String COLUMN_EMAIL = "email";
    static final String COLUMN_USERNAME = "username";
    static final String COLUMN_PASSWORD = "password";
    static final String COLUMN_STATUS = "status";
    static final String COLUMN_TYPE = "type";

    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_MIDDLE_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_CONTACT + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_STATUS + " TEXT DEFAULT 'On Process', " +
                COLUMN_TYPE + " TEXT DEFAULT 'normal'" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addUser(String firstName, String middleName, String lastName,
                           int age, String contact, String email, String username, String password, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FIRST_NAME, firstName);
        contentValues.put(COLUMN_MIDDLE_NAME, middleName);
        contentValues.put(COLUMN_LAST_NAME, lastName);
        contentValues.put(COLUMN_AGE, age);
        contentValues.put(COLUMN_CONTACT, contact);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=?", new String[]{username});
        if (cursor.getCount() > 0) {
            cursor.close();
            Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            cursor.close();
            long result = db.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        }
    }

    public boolean isValidUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    public String getUserStatus(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_STATUS + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
        String status = "";

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_STATUS);
            if (columnIndex != -1) { // Check if the column index is valid
                status = cursor.getString(columnIndex);
            } else {
                status = "Status column not found";
            }
        }
        cursor.close();
        return status;
    }

}
