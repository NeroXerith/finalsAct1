package com.bryle_sanico.finalsact1;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    EditText inputFname, inputMname, inputLname, inputAge, inputContactNo, inputEmail, inputUsername, inputPassword;
    Button btnSignup;
    SQLiteDB databaseHelper;

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

        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserToDatabase();
            }
        });
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
                && !contactNo.isEmpty() && !email.isEmpty() && !username.isEmpty()) {

            int intAge = Integer.parseInt(age);

            boolean isAdded = databaseHelper.addUser(firstName, middleName, lastName,
                    intAge, contactNo, email, username, password, getApplicationContext());

            if (isAdded) {
                Toast.makeText(getApplicationContext(), "Registered Sucessfully!", Toast.LENGTH_SHORT).show();
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
}
