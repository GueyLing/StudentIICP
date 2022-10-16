package com.example.studentiicp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class WaitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String dayOfMonth="", month="", year="", hourOfDay="", minute="";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        TextView date = findViewById(R.id.textView4);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            dayOfMonth = extras.getString("dayOfMonth");
            month = extras.getString("month");
            year = extras.getString("year");
            hourOfDay = extras.getString("hourOfDay");
            minute = extras.getString("minute");
        }
        String formattedDate = dayOfMonth+"/"+month+"/"+year+"   "+hourOfDay+":"+minute;
        date.setText(formattedDate);
    }
}