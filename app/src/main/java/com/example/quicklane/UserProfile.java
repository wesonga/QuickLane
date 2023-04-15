package com.example.quicklane;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile extends AppCompatActivity {


    TextView usernameTv, emailTv, plateNumberTv, permitNumberTv, fronterusername, fronteremail;
    String email;
    TextView backHome;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String usernameprofilenow = intent.getStringExtra(MainActivity.verifyuser);

        fronterusername = findViewById(R.id.frontusername);
        fronteremail = findViewById(R.id.frontemail);
        usernameTv = findViewById(R.id.usernameText);
        emailTv = findViewById(R.id.emailText);
        plateNumberTv = findViewById(R.id.plateNumberText);
        permitNumberTv = findViewById(R.id.permitNumberText);
        backHome = findViewById(R.id.back_home);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(usernameprofilenow).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                fronteremail.setText(String.valueOf(dataSnapshot.child("email").getValue()));
                fronterusername.setText("@"+usernameprofilenow);
                usernameTv.setText(usernameprofilenow);
                emailTv.setText(String.valueOf(dataSnapshot.child("email").getValue()));
                plateNumberTv.setText(String.valueOf(dataSnapshot.child("numberPlate").getValue()));
                permitNumberTv.setText(String.valueOf(dataSnapshot.child("permitNumber").getValue()));

            }
        });


        logoutButton = findViewById(R.id.logout);
        Intent in = getIntent();
        String string = in.getStringExtra("message");
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
                builder.setTitle("Logout").
                        setMessage("You sure, that you want to logout?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                Intent i = new Intent(getApplicationContext(),
                                        Login.class);
                                startActivity(i);
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });



    }




}