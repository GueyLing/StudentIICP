package com.example.studentiicp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class ElectionActivity extends AppCompatActivity {

    RadioGroup radioGroup1, radioGroup2;
    Button submit;
    RadioButton radio1, radio2, radio3, radio4;
    String id = "", title = "", president_candidate1="", president_candidate2="", vice_president_candidate1="", vice_president_candidate2="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("user_events");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getString("id");
            title = extras.getString("title");
            president_candidate1 = extras.getString("president_candidate1");
            president_candidate2 = extras.getString("president_candidate2");
            vice_president_candidate1 = extras.getString("vice_president_candidate1");
            vice_president_candidate2 = extras.getString("vice_president_candidate2");
        }

        radio1 = findViewById(R.id.checkbox);
        radio2 = findViewById(R.id.checkbox2);
        radio3 = findViewById(R.id.checkbox3);
        radio4 = findViewById(R.id.checkbox4);

        radio1.setText(president_candidate1);
        radio2.setText(president_candidate2);
        radio3.setText(vice_president_candidate1);
        radio4.setText(vice_president_candidate2);

        radioGroup1 = findViewById(R.id.radioGroup);
        radioGroup2 = findViewById(R.id.radioGroup2);


        String finalMyid = id;
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        submit = findViewById(R.id.button2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap hashMap = new HashMap();
                hashMap.put(finalMyid, true);

                ref.child(currentFirebaseUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(ElectionActivity.this, "Voted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ElectionActivity.this, StudentHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

                int radioId = radioGroup1.getCheckedRadioButtonId();
                switch(radioId ){
                    case R.id.checkbox:
                        DatabaseReference election_ref = FirebaseDatabase.getInstance().getReference().child("election_event").child(finalMyid).child("p1_count");
                        election_ref.setValue(ServerValue.increment(1));
                        break;
                    case R.id.checkbox2:
                        DatabaseReference election_ref2 = FirebaseDatabase.getInstance().getReference().child("election_event").child(finalMyid).child("p2_count");
                        election_ref2.setValue(ServerValue.increment(1));
                        break;
                }

                int radioId2 = radioGroup2.getCheckedRadioButtonId();
                switch(radioId2 ){
                    case R.id.checkbox3:
                        DatabaseReference election_ref3 = FirebaseDatabase.getInstance().getReference().child("election_event").child(finalMyid).child("vp1_count");
                        election_ref3.setValue(ServerValue.increment(1));
                        break;
                    case R.id.checkbox4:
                        DatabaseReference election_ref4 = FirebaseDatabase.getInstance().getReference().child("election_event").child(finalMyid).child("vp2_count");
                        election_ref4.setValue(ServerValue.increment(1));
                        break;
                }

            }
        });
    }
}