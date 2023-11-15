package com.bryle_sanico.finalsact1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find the btnSignup button by its ID
        Button btnSignup = findViewById(R.id.btnSignup);

        // Set an OnClickListener for the btnSignup button
        btnSignup.setOnClickListener(v -> {
            // Call a function to open the SignUp activity
            openSignUpActivity();
        });
    }

    // Function to open the SignUp activity
    private void openSignUpActivity() {
        // Create an Intent to open the SignUp activity
        Intent intent = new Intent(this, SignUp.class);

        // Start the SignUp activity
        startActivity(intent);
    }
}