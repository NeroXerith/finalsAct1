package com.bryle_sanico.finalsact1;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private SQLiteDB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new SQLiteDB(this);

        EditText inputUser = findViewById(R.id.inputUser);
        EditText inputPass = findViewById(R.id.inputPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(v -> openSignUpActivity());

        btnLogin.setOnClickListener(v -> {
            String username = inputUser.getText().toString().trim();
            String password = inputPass.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                boolean isValidUser = validateUser(username, password);

                if (isValidUser) {
                    String userStatus = getUserStatus(username);

                    if ("admin".equalsIgnoreCase(userStatus)) {
                        openAdminPanelActivity();
                    } else if ("normal".equalsIgnoreCase(userStatus)) {
                        openGuestProfileActivity();
                    } else {
                        Toast.makeText(Login.this, "Invalid user status", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Login.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateUser(String username, String password) {
        // Perform validation against your database, here's a placeholder
        // You should implement your own logic to check the username and password in the database
        // Return true if the user is valid, otherwise return false
        // You might need to add a COLUMN_PASSWORD in your Users table to store passwords
        // You can use a query similar to this:
        // SELECT * FROM Users WHERE username = ? AND password = ?
        // You should hash and securely store passwords in a real scenario
        return dbHelper.isValidUser(username, password);
    }

    private String getUserStatus(String username) {
        // Get user status from the database based on the username
        // Implement your logic to retrieve user status from the database
        return dbHelper.getUserStatus(username);
    }

    private void openAdminPanelActivity() {
        Intent intent = new Intent(Login.this, AdminPanel.class);
        startActivity(intent);
    }

    private void openGuestProfileActivity() {
        Intent intent = new Intent(Login.this, GuestProfile.class);
        startActivity(intent);
    }

    private void openSignUpActivity() {
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
    }
}
