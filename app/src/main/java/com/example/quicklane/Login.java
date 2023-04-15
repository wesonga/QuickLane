package com.example.quicklane;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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


public class Login extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quicklane-eb596-default-rtdb.firebaseio.com");
    public static final String usernameprofile = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = findViewById(R.id.input_username);
        final EditText password = findViewById(R.id.input_password);
        final Button loginBtn = findViewById(R.id.login_btn);
        final Button registerBtn = findViewById(R.id.register_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernameTxt = username.getText().toString();
                final String passwordTxt = password.getText().toString();

                if (usernameTxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(Login.this, "Please Enter your Email Address", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(usernameTxt)){
                                final String getPassword = snapshot.child(usernameTxt).child("password").getValue(String.class);

                                if(getPassword.equals(passwordTxt)){
                                    Toast.makeText(Login.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    intent.putExtra(usernameprofile, usernameTxt);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //open Register activity
                startActivity(new Intent(Login.this, Register.class));
            }
        });

    }
}