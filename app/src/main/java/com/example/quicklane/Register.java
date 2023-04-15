package com.example.quicklane;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Register extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quicklane-eb596-default-rtdb.firebaseio.com");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText username = findViewById(R.id.registerUsername);
        final EditText email = findViewById(R.id.registerEmail);
        final EditText numberPlate = findViewById(R.id.registerPlate);
        final EditText permitNumber = findViewById(R.id.registerPermitNumber);
        final EditText password = findViewById(R.id.registerPassword);
        final EditText confirmPassword = findViewById(R.id.registerConfirmPassword);

        final Button signupBtn = findViewById(R.id.signup_button);
        final TextView loginRegisterBtn = findViewById(R.id.login_register_btn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String usernameTxt = username.getText().toString();
                final String emailTxt = email.getText().toString();
                final String numberPlateTxt = numberPlate.getText().toString();
                final String permitNumberText = permitNumber.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String confirmPasswordTxt = confirmPassword.getText().toString();

                if(usernameTxt.isEmpty() || emailTxt.isEmpty() || numberPlateTxt.isEmpty() || permitNumberText.isEmpty() || passwordTxt.isEmpty() || confirmPasswordTxt.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }

                else if (!passwordTxt.equals(confirmPasswordTxt)){
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }

                else {
                     databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             if(snapshot.hasChild(usernameTxt)){
                                 Toast.makeText(Register.this, "User is already registered!", Toast.LENGTH_SHORT).show();
                             }
                             else{
                                 databaseReference.child("users").child(usernameTxt).child("email").setValue(emailTxt);
                                 databaseReference.child("users").child(usernameTxt).child("numberPlate").setValue(numberPlateTxt);
                                 databaseReference.child("users").child(usernameTxt).child("permitNumber").setValue(permitNumberText);
                                 databaseReference.child("users").child(usernameTxt).child("password").setValue(passwordTxt);

                                 Toast.makeText(Register.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                                 finish();
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });
                }
            }
        });

        loginRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}