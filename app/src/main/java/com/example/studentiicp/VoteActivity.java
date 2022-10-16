package com.example.studentiicp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class VoteActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton finalRadioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String title = "", option1 = "", option2 = "", option3 = "", myid = "";
        Boolean expired = null;

        DatabaseReference ref;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        TextView name = findViewById(R.id.textView4);
        RadioButton option_1 = findViewById(R.id.checkbox);
        RadioButton option_2 = findViewById(R.id.checkbox2);
        RadioButton option_3 = findViewById(R.id.checkbox3);
        Button button = findViewById(R.id.button);
        radioGroup = findViewById(R.id.radioGroup);

        ref = FirebaseDatabase.getInstance().getReference().child("user_events");

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            title = extras.getString("title");
            option1 = extras.getString("option1");
            option2 = extras.getString("option2");
            option3 = extras.getString("option3");
            expired = extras.getBoolean("expired");
            myid = extras.getString("id");
        }

        name.setText(title);
        option_1.setText(option1);
        option_2.setText(option2);
        option_3.setText(option3);

        if (expired == false){
            button.setText("Submit");
        }else{
            button.setText("Expired");
            button.setEnabled(false);
        }

        String finalMyid = myid;
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap hashMap = new HashMap();
                hashMap.put(finalMyid, true);

                ref.child(currentFirebaseUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(VoteActivity.this, "Voted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VoteActivity.this, StudentHomeActivity.class);
                        startActivity(intent);
                    }
                });
                int radioId = radioGroup.getCheckedRadioButtonId();
                switch(radioId ){
                    case R.id.checkbox:
                        DatabaseReference poll_ref = FirebaseDatabase.getInstance().getReference().child("poll_event").child(finalMyid).child("option1_count");
                        poll_ref.setValue(ServerValue.increment(1));
                        break;
                    case R.id.checkbox2:
                        DatabaseReference poll_ref2 = FirebaseDatabase.getInstance().getReference().child("poll_event").child(finalMyid).child("option2_count");
                        poll_ref2.setValue(ServerValue.increment(1));
                        break;
                    case R.id.checkbox3:
                        DatabaseReference poll_ref3 = FirebaseDatabase.getInstance().getReference().child("poll_event").child(finalMyid).child("option3_count");
                        poll_ref3.setValue(ServerValue.increment(1));
                        break;
                }
            }
        });

    }

}