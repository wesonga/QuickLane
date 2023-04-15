package com.example.quicklane;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class PartialAction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partial_action);

        ImageButton map =  findViewById(R.id.imageButton);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("https://www.google.com/maps/");
                mapIntent.setData(data);
                startActivity(Intent.createChooser(mapIntent,"Launching Map"));
            }
        });

    }
}