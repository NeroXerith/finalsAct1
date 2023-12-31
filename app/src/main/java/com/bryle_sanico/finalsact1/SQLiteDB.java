package com.bryle_sanico.finalsact1;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SQLiteDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "registered_users";
    private static final int DATABASE_VERSION = 1;
    private Context mContext;

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
        mContext = context;
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
                COLUMN_STATUS + " TEXT DEFAULT 'For Approval', " +
                COLUMN_TYPE + " TEXT DEFAULT 'Normal'" +
                ")";
        db.execSQL(createTableQuery);

        // Inserting the first record with specific values
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FIRST_NAME, "Bryle");
        contentValues.put(COLUMN_MIDDLE_NAME, "Turbanada");
        contentValues.put(COLUMN_LAST_NAME, "Sanico");
        contentValues.put(COLUMN_AGE, 18);
        contentValues.put(COLUMN_CONTACT, "09279709414");
        contentValues.put(COLUMN_EMAIL, "astar8820@gmail.com");
        contentValues.put(COLUMN_USERNAME, "Xerith");
        contentValues.put(COLUMN_PASSWORD, "admin123");
        contentValues.put(COLUMN_STATUS, "Approved");
        contentValues.put(COLUMN_TYPE, "Admin");

        // Insert the first record into the database
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            // Failed to insert initial record
            Toast.makeText(mContext, "Failed to insert initial record", Toast.LENGTH_SHORT).show();
        } else {
            // Initial record inserted successfully
            Toast.makeText(mContext, "Initial record inserted successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addUser(String firstName, String middleName, String lastName,
                           int age, String contact, String email, String username, String password) {
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

        // Check if the username already exists in the database
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=?", new String[]{username});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // Username already exists
        }

        cursor.close();

        // Insert new user data into the database
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }


    public boolean updateUserStatus(int userId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, newStatus);

        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = { String.valueOf(userId) };

        int count = db.update(TABLE_NAME, values, selection, selectionArgs);

        return count > 0;
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

    public boolean updateUser(int userId, String updatedFirstName, String updatedMiddleName,
                              String updatedLastName, int updatedAge, String updatedContact,
                              String updatedEmail, String updatedUsername, String updatedPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the updated username already exists in the database
        boolean isUsernameAvailable = isUsernameAvailable(userId, updatedUsername);
        if (!isUsernameAvailable) {
            Toast.makeText(mContext, "Username already exists!", Toast.LENGTH_SHORT).show();
            return false; // Username already exists, prevent update
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, updatedFirstName);
        values.put(COLUMN_MIDDLE_NAME, updatedMiddleName);
        values.put(COLUMN_LAST_NAME, updatedLastName);
        values.put(COLUMN_AGE, updatedAge);
        values.put(COLUMN_CONTACT, updatedContact);
        values.put(COLUMN_EMAIL, updatedEmail);
        values.put(COLUMN_USERNAME, updatedUsername);
        values.put(COLUMN_PASSWORD, updatedPassword);

        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(userId)};

        int count = db.update(TABLE_NAME, values, selection, selectionArgs);

        return count > 0;
    }

    private boolean isUsernameAvailable(int userId, String updatedUsername) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                        COLUMN_USERNAME + " = ? AND " + COLUMN_ID + " != ?",
                new String[]{updatedUsername, String.valueOf(userId)});

        boolean isAvailable = cursor.getCount() <= 0;

        cursor.close();
        return isAvailable;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define the table name where you store user data
        String tableName = TABLE_NAME;

        // Define the condition for deletion
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        // Perform the deletion and check the result
        int deletedRows = db.delete(tableName, selection, selectionArgs);
        return deletedRows > 0;
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

    public String getUserType(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_TYPE + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
        String userType = "";

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_TYPE);
            if (columnIndex != -1) {
                userType = cursor.getString(columnIndex);
            } else {
                userType = "Type column not found";
            }
        }
        cursor.close();
        return userType;
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1; // Initialize with a default value

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_ID);
            if (columnIndex != -1) {
                userId = cursor.getInt(columnIndex);
            } // No need for an else clause here if you handle errors differently
        }
        cursor.close();
        return userId;
    }

}
