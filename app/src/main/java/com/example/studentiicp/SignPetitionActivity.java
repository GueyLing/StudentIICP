package com.example.studentiicp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class SignPetitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String title = "", description="", count = "", myid = "";
        Boolean expired = null;
        DatabaseReference ref;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_petition);
        TextView name = findViewById(R.id.textView4);
        TextView desc = findViewById(R.id.textView);
        TextView petition_no = findViewById(R.id.textView5);
        Button button = findViewById(R.id.button);

        ref = FirebaseDatabase.getInstance().getReference().child("user_events");

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            title = extras.getString("title");
            description = extras.getString("description");
            count = extras.getString("count");
            expired = extras.getBoolean("expired");
            myid = extras.getString("id");
        }
        name.setText(title);
        desc.setText(description);
        petition_no.setText(count);

        if (expired == false){
            button.setText("Sign this petition");
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
                DatabaseReference petition_ref = FirebaseDatabase.getInstance().getReference().child("petition_event").child(finalMyid).child("petition_no");
                petition_ref.setValue(ServerValue.increment(1));
                ref.child(currentFirebaseUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(SignPetitionActivity.this, "Signed successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignPetitionActivity.this, StudentHomeActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}