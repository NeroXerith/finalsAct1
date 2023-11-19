package com.bryle_sanico.finalsact1;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private SQLiteDB dbHelper;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new SQLiteDB(this);

        EditText inputUser = findViewById(R.id.inputUser);
        EditText inputPass = findViewById(R.id.inputPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignup = findViewById(R.id.btnSignup);
        Button btnTest = findViewById(R.id.btnTest);
        Button btnEmptyDatabase = findViewById(R.id.btnEmptyDatabase);

        btnSignup.setOnClickListener(v -> openSignUpActivity());
        btnTest.setOnClickListener(v -> openAdminPanelActivity());
        btnEmptyDatabase.setOnClickListener(v -> {

            emptyDatabase();
        });

        btnLogin.setOnClickListener(v -> {
            String username = inputUser.getText().toString().trim();
            String password = inputPass.getText().toString().trim();

            loginProcess(username, password);
        });
    }


    private void loginProcess(String username, String password) {
        if (!username.isEmpty() && !password.isEmpty()) {
            boolean isValidUser = validateUser(username, password);

            if (isValidUser) {
                // Retrieve user ID from the database
                int retrievedUserId = dbHelper.getUserId(username);

                // Update userId variable
                userId = String.valueOf(retrievedUserId);

                String userStatus = dbHelper.getUserStatus(username);

                if ("Approved".equalsIgnoreCase(userStatus)) {
                    String userType = dbHelper.getUserType(username);

                    if ("Admin".equalsIgnoreCase(userType)) {
                        openAdminPanelActivity();
                    } else {
                        openGuestProfileActivity();
                    }
                    finish();
                } else if ("For Approval".equalsIgnoreCase(userStatus)) {
                    Toast.makeText(Login.this, "Your account is still for approval.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "Invalid user status", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Login.this, "Invalid credentials.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Login.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateUser(String username, String password) {
        return dbHelper.isValidUser(username, password);
    }

    private String getUserStatus(String username) {
        return dbHelper.getUserStatus(username);
    }

    private void openAdminPanelActivity() {
        Intent intent = new Intent(Login.this, AdminPanel.class);
        intent.putExtra("isAdmin", "Yes");
        startActivity(intent);
    }

    private void openGuestProfileActivity() {
        Intent intent = new Intent(Login.this, GuestProfile.class);
        intent.putExtra("userId", userId); // Pass only the user ID
        startActivity(intent);
    }

    private void openSignUpActivity() {
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
    }

    private void emptyDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Drop the existing table
        db.execSQL("DROP TABLE IF EXISTS " + SQLiteDB.TABLE_NAME);

        // Recreate the table with the same schema
        String createTableQuery = "CREATE TABLE " + SQLiteDB.TABLE_NAME + " (" +
                SQLiteDB.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SQLiteDB.COLUMN_FIRST_NAME + " TEXT, " +
                SQLiteDB.COLUMN_MIDDLE_NAME + " TEXT, " +
                SQLiteDB.COLUMN_LAST_NAME + " TEXT, " +
                SQLiteDB.COLUMN_AGE + " INTEGER, " +
                SQLiteDB.COLUMN_CONTACT + " TEXT, " +
                SQLiteDB.COLUMN_EMAIL + " TEXT, " +
                SQLiteDB.COLUMN_USERNAME + " TEXT, " +
                SQLiteDB.COLUMN_PASSWORD + " TEXT, " +
                SQLiteDB.COLUMN_STATUS + " TEXT DEFAULT 'For Approval', " +
                SQLiteDB.COLUMN_TYPE + " TEXT DEFAULT 'Normal'" +
                ")";

        db.execSQL(createTableQuery);

        // Optional: Notify user or perform any other action
        Toast.makeText(this, "Database table has been reset", Toast.LENGTH_SHORT).show();
    }
}
