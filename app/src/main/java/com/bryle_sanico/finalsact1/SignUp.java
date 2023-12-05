package com.bryle_sanico.finalsact1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private EditText inputFname, inputMname, inputLname, inputAge, inputContactNo, inputEmail, inputUsername, inputPassword, inputCPassword;
    private Button btnSignup,btnEdit;

    private LinearLayout confirmPass;
    private SQLiteDB databaseHelper;
    private int userID;
    private String firstName, middleName, lastName, age, contact, email, username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        databaseHelper = new SQLiteDB(this);

        inputFname = findViewById(R.id.inputFname);
        inputMname = findViewById(R.id.inputMname);
        inputLname = findViewById(R.id.inputLname);
        inputAge = findViewById(R.id.inputAge);
        inputContactNo = findViewById(R.id.inputContactNo);
        inputEmail = findViewById(R.id.inputEmail);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        inputCPassword = findViewById(R.id.inputCPassword);
        confirmPass = findViewById(R.id.confirm_password);
        btnSignup = findViewById(R.id.btnSignup);
        btnEdit = findViewById(R.id.btnUpdate);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId") && intent.hasExtra("requestType")) {
            String requestType = intent.getStringExtra("requestType");
            userID = intent.getIntExtra("userId", -1);

            if (requestType != null && requestType.equals("editUser")) {
                getUserDataFromDatabase();
                // Set retrieved data to EditText fields
                inputFname.setText(firstName);
                inputMname.setText(middleName);
                inputLname.setText(lastName);
                inputEmail.setText(email);
                inputContactNo.setText(contact);
                inputAge.setText(age);
                inputUsername.setText(username);
                inputPassword.setText(password);
                btnSignup.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                confirmPass.setVisibility(View.VISIBLE);

            }
        }
        btnSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addUserToDatabase();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPass = inputPassword.getText().toString().trim();
                String inputCPass = inputCPassword.getText().toString().trim();

                if (!inputPass.equals(inputCPass)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                } else {
                    // Passwords match, proceed with the update
                    updateUserInDatabase(userID);
                }
            }
        });

    }


    private void updateUserInDatabase(int userId) {
        // Retrieve updated data from EditText fields
        String updatedFirstName = inputFname.getText().toString().trim();
        String updatedMiddleName = inputMname.getText().toString().trim();
        String updatedLastName = inputLname.getText().toString().trim();
        String updatedAge = inputAge.getText().toString().trim();
        String updatedContact = inputContactNo.getText().toString().trim();
        String updatedEmail = inputEmail.getText().toString().trim();
        String updatedUsername = inputUsername.getText().toString().trim();
        String updatedPassword = inputPassword.getText().toString().trim();

        // Update the database with the new data
        boolean isUpdated = databaseHelper.updateUser(userId, updatedFirstName, updatedMiddleName,
                updatedLastName, Integer.parseInt(updatedAge), updatedContact, updatedEmail,
                updatedUsername, updatedPassword);

        if (isUpdated) {
            Toast.makeText(getApplicationContext(), "User data updated!", Toast.LENGTH_SHORT).show();
            // Send back the updated user data to GuestProfile activity
            sendUpdatedDataToGuestProfile();
        } else {
            Toast.makeText(getApplicationContext(), "Failed to update user data!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendUpdatedDataToGuestProfile() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("fullName", inputFname.getText().toString().trim() + " " +
                inputMname.getText().toString().trim() + " " +
                inputLname.getText().toString().trim());
        resultIntent.putExtra("email", inputEmail.getText().toString().trim());
        resultIntent.putExtra("contact", inputContactNo.getText().toString().trim());
        resultIntent.putExtra("age", inputAge.getText().toString().trim());
        resultIntent.putExtra("username", inputUsername.getText().toString().trim());
        // Add other data as needed...

        setResult(RESULT_OK, resultIntent);
        finish();
    }


    private void addUserToDatabase() {
        String firstName = inputFname.getText().toString().trim();
        String middleName = inputMname.getText().toString().trim();
        String lastName = inputLname.getText().toString().trim();
        String age = inputAge.getText().toString().trim();
        String contactNo = inputContactNo.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String username = inputUsername.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (!firstName.isEmpty() && !middleName.isEmpty() && !lastName.isEmpty() && !age.isEmpty()
                && !contactNo.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {

            int intAge = Integer.parseInt(age);

            boolean isAdded = databaseHelper.addUser(firstName, middleName, lastName,
                    intAge, contactNo, email, username, password);

            if (isAdded) {
                Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
                navigateToLoginActivity(); // Go back to the login activity
            } else {
                Toast.makeText(getApplicationContext(), "Failed to Register!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please fill in all fields!", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(SignUp.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear activity stack
        startActivity(intent);
        finish(); // Finish the current activity
    }

    private void getUserDataFromDatabase() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
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
            int passwordColumnIndex = cursor.getColumnIndex(SQLiteDB.COLUMN_PASSWORD);

            if (firstNameColumnIndex != -1 && middleNameColumnIndex != -1 && lastNameColumnIndex != -1) {
                firstName = cursor.getString(firstNameColumnIndex);
                middleName = cursor.getString(middleNameColumnIndex);
                lastName = cursor.getString(lastNameColumnIndex);
                age = cursor.getString(ageColumnIndex);
                contact = cursor.getString(contactColumnIndex);
                email = cursor.getString(emailColumnIndex);
                username = cursor.getString(usernameColumnIndex);
                password = cursor.getString(passwordColumnIndex);

                cursor.close();
            } else {
                Toast.makeText(SignUp.this, "Column not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



