package com.bryle_sanico.finalsact1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class GuestProfile extends AppCompatActivity {

    private String fullName;
    private int userID;
    private String firstName, middleName, lastName, age, contact, email, username, password, status, type;
    // Add variables for each column's data
    private SQLiteDB dbHelper;
    private Button btnApprove, btnReject, btnDelete;
    private String userType; // Declare the variable in the class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_profile);

        dbHelper = new SQLiteDB(this); // Initialize Database

        btnApprove = findViewById(R.id.btnApprove);
        btnReject = findViewById(R.id.btnReject);
        btnDelete = findViewById(R.id.btnDelete);

        // Check if admim, if Yes then btnDelete will be visible
        checkUserType();
        if(userType.equals("Yes")){
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }
        TextView txtUserID = findViewById(R.id.txtUserID);
        EditText inputFullName = findViewById(R.id.inputFullName);
        EditText inputAge = findViewById(R.id.inputAge);
        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputContact = findViewById(R.id.inputContactNo);
        EditText inputUsername = findViewById(R.id.inputUsername);
        EditText inputType = findViewById(R.id.inputType);
        EditText inputStatus = findViewById(R.id.inputStatus);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            String userId = intent.getStringExtra("userId");
            userID = Integer.parseInt(userId);

            // Fetch record from the database using the userID
            getUserDataFromDatabase();

            // Display the fetched data
            txtUserID.setText("User ID: " + userID);
            inputFullName.setText(fullName);
            inputEmail.setText(email);
            inputContact.setText(contact);
            inputAge.setText(age);
            inputUsername.setText(username);
            inputType.setText(type);
            inputStatus.setText(status);

            // Check the status value from the EditText
            if (inputStatus.getText().toString().equals("For Approval")) {
                // If the status is "For Approval", set visibility of buttons to VISIBLE
                btnApprove.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
            } else {
                // If the status is different, hide the buttons
                btnApprove.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
            }
        }

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(userID, "Approved");
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(userID, "Rejected");
            }
        });

    }

    private void checkUserType() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            userType = intent.getStringExtra("userType");
        }
    }


    private void updateStatus(int userId, String newStatus) {
        // Update the status in the database
        boolean isUpdated = dbHelper.updateUserStatus(userId, newStatus);

        if (isUpdated) {
            // Update successful
            String statusMessage = "Status updated to " + newStatus;
            Toast.makeText(GuestProfile.this, statusMessage, Toast.LENGTH_SHORT).show();
            // Hide buttons after status update
            btnApprove.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);

            // Navigate back to AdminPanel activity
            Intent intent = new Intent(GuestProfile.this, AdminPanel.class);
            startActivity(intent);
            finish();
        } else {
            // Update failed
            Toast.makeText(GuestProfile.this, "Failed to update status", Toast.LENGTH_SHORT).show();
        }
    }
    private void getUserDataFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                SQLiteDB.TABLE_NAME,
                null,
                SQLiteDB.COLUMN_ID + " = ?",
                new String[]{String.valueOf(userID)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int firstNameColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_FIRST_NAME);
            int middleNameColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_MIDDLE_NAME);
            int lastNameColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_LAST_NAME);
            int ageColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_AGE);
            int contactColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_CONTACT);
            int emailColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_EMAIL);
            int usernameColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_USERNAME);
            int statusColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_STATUS);
            int typeColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_TYPE);
            // Add other column indices for each column

            // Check if the column index is valid (-1 indicates column not found)
            if (firstNameColumnIndex != -1 && middleNameColumnIndex != -1 && lastNameColumnIndex != -1) {
                firstName = cursor.getString(firstNameColumnIndex);
                middleName = cursor.getString(middleNameColumnIndex);
                lastName = cursor.getString(lastNameColumnIndex);
                age = cursor.getString(ageColumnIndex);
                contact = cursor.getString(contactColumnIndex);
                email = cursor.getString(emailColumnIndex);
                username = cursor.getString(usernameColumnIndex);
                status = cursor.getString(statusColumnIndex);
                type = cursor.getString(typeColumnIndex);
                // Retrieve other column values using respective indices

                fullName = firstName + " " + middleName + " " + lastName;

                cursor.close();
            } else {
                // Handle case where column index is -1 (column not found)
                // Log an error, show a message, or handle it according to your app logic
            }
        }
    }
}