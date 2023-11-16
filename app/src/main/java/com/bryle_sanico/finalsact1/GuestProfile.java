package com.bryle_sanico.finalsact1;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
public class GuestProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_profile);

        TextView txtFullName = findViewById(R.id.txtFullName);
        EditText inputAge = findViewById(R.id.inputAge);
        EditText inputContactNo = findViewById(R.id.inputContactNo);
        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputUsername = findViewById(R.id.inputUsername);
        EditText inputPassword = findViewById(R.id.inputPassword);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userData")) {
            String userData = intent.getStringExtra("userData");

            // Split the userData by a delimiter (assuming it is separated by a specific character)
            String[] userDataParts = userData.split(";"); // Change the delimiter to match your data structure

            // Set the values to respective TextViews and EditTexts
            if (userDataParts.length >= 1) { // Assuming userData contains at least 6 parts
                txtFullName.setText(userDataParts[0]); // Assuming first part is full name

            }
        }

    }
}
