package com.bryle_sanico.finalsact1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GuestProfile extends AppCompatActivity {

    private String fullName;
    private int userID;
    private String firstName, middleName, lastName, age, contact, email, username, password, status, type;
    private SQLiteDB dbHelper;
    private Button btnApprove, btnReject, btnDelete;
    private String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_profile);

        dbHelper = new SQLiteDB(this); // Initialize Database

        btnApprove = findViewById(R.id.btnApprove);
        btnReject = findViewById(R.id.btnReject);
        btnDelete = findViewById(R.id.btnDelete);

        checkUserType();

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

            getUserDataFromDatabase();

            txtUserID.setText("User ID: " + userID);
            inputFullName.setText(fullName);
            inputEmail.setText(email);
            inputContact.setText(contact);
            inputAge.setText(age);
            inputUsername.setText(username);
            inputType.setText(type);
            inputStatus.setText(status);

            if (inputStatus.getText().toString().equals("For Approval")) {
                btnApprove.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
            } else {
                btnApprove.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
            }
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(userID);
            }
        });

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
        if (intent != null && intent.hasExtra("userId") && intent.hasExtra("userType")) {
            userType = intent.getStringExtra("userType");
        }

        if (userType.equals("Admin")) {
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void updateStatus(int userId, String newStatus) {
        boolean isUpdated = dbHelper.updateUserStatus(userId, newStatus);

        if (isUpdated) {
            String statusMessage = "Status updated to " + newStatus;
            Toast.makeText(GuestProfile.this, statusMessage, Toast.LENGTH_SHORT).show();
            btnApprove.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);

            Intent intent = new Intent();
            intent.putExtra("refreshAdminPanel", true); // Set the refresh flag to true
            setResult(RESULT_OK, intent); // Set result to trigger refresh in AdminPanel

            finish();
        } else {
            Toast.makeText(GuestProfile.this, "Failed to update status", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteUser(int userId) {
        boolean isDeleted = dbHelper.deleteUser(userId);

        if (isDeleted) {
            String deleteMessage = "User record deleted";
            Toast.makeText(GuestProfile.this, deleteMessage, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.putExtra("refreshAdminPanel", true); // Set the refresh flag to true
            setResult(RESULT_OK, intent); // Set result to trigger refresh in AdminPanel

            finish();
        } else {
            Toast.makeText(GuestProfile.this, "Failed to delete record", Toast.LENGTH_SHORT).show();
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

                fullName = firstName + " " + middleName + " " + lastName;

                cursor.close();
            } else {
                Toast.makeText(GuestProfile.this, "Column not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
