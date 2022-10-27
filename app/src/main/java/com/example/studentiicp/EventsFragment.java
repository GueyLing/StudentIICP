package com.example.studentiicp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database, new_database;
    MyAdapter myAdapter;
    ArrayList<EventTitle> list;
    MyAdapter.RecyclerViewClickListener listener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewStatistics.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        recyclerView = rootView.findViewById(R.id.eventList);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("events");
        new_database = FirebaseDatabase.getInstance().getReference("user_events").child(currentFirebaseUser.getUid());
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        setOnClickListener();
        list = new ArrayList<>();
        ArrayList<String> myList= new ArrayList<>();
        //this should put line90
        myAdapter = new MyAdapter(getActivity(),list, listener, 1);
        recyclerView.setAdapter(myAdapter);

        new_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if (dataSnapshot.getValue().toString().equals("false"))
                    myList.add(dataSnapshot.getKey().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    EventTitle event = dataSnapshot.getValue(EventTitle.class);
                    int dayOfMonth = event.getDayOfMonth();
                    int month = event.getMonth();
                    int year = event.getYear();
                    int hourOfDay = event.getHourOfDay();
                    int minute = event.getMinute();
                    String newMonth, newDay;
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    String getCurrentDateTime = sdf.format(c.getTime());
                    if (month<10){
                        newMonth = '0'+String.valueOf(month);
                    }else{
                        newMonth = String.valueOf(month);
                    }

                    if (dayOfMonth<10){
                        newDay = '0'+String.valueOf(dayOfMonth);
                    }else{
                        newDay = String.valueOf(dayOfMonth);
                    }

                    String getMyTime = year+ "/" +newMonth+'/'+newDay+' '+hourOfDay+':'+minute;
                    if (myList.contains(event.getId())){
                        if (getCurrentDateTime.compareTo(getMyTime) < 0)
                        {
                            list.add(event);
                        }

                    }

                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return rootView;
    }

    private void setOnClickListener() {

        listener = new MyAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {

                if(list.get(position).getType().equals("petition")){
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("petition_event").child(list.get(position).getId());
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = list.get(position).getId();
                            String petitionTitle= snapshot.child("petitionTitle").getValue().toString();
                            String description= snapshot.child("description").getValue().toString();
                            String petition_no= snapshot.child("petition_no").getValue().toString();
                            String dayOfMonth= snapshot.child("dayOfMonth").getValue().toString();
                            String month= snapshot.child("month").getValue().toString();
                            String year= snapshot.child("year").getValue().toString();
                            String hourOfDay= snapshot.child("hourOfDay").getValue().toString();
                            String minute= snapshot.child("minute").getValue().toString();
                            methodToProcess(id, petitionTitle, description, petition_no, dayOfMonth, month, year, hourOfDay, minute);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                else if(list.get(position).getType().equals("election")){
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("election_event").child(list.get(position).getId());
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = list.get(position).getId();
                            String electionTitle= snapshot.child("electionTitle").getValue().toString();
                            String president_candidate1= snapshot.child("president_candidate1").getValue().toString();
                            String president_candidate2= snapshot.child("president_candidate2").getValue().toString();
                            String vice_president_candidate1= snapshot.child("vice_president_candidate1").getValue().toString();
                            String vice_president_candidate2= snapshot.child("vice_president_candidate2").getValue().toString();
                            String p1_count= snapshot.child("p1_count").getValue().toString();
                            String p2_count= snapshot.child("p2_count").getValue().toString();
                            String vp1_count= snapshot.child("vp1_count").getValue().toString();
                            String vp2_count= snapshot.child("vp2_count").getValue().toString();
                            String dayOfMonth= snapshot.child("dayOfMonth").getValue().toString();
                            String month= snapshot.child("month").getValue().toString();
                            String year= snapshot.child("year").getValue().toString();
                            String hourOfDay= snapshot.child("hourOfDay").getValue().toString();
                            String minute= snapshot.child("minute").getValue().toString();
                            methodToProcess2(id, electionTitle, president_candidate1, president_candidate2,vice_president_candidate1,vice_president_candidate2,p1_count,p2_count,vp1_count, vp2_count, dayOfMonth, month, year, hourOfDay, minute );

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poll_event").child(list.get(position).getId());
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = list.get(position).getId();
                            String pollTitle= snapshot.child("pollTitle").getValue().toString();
                            String option1= snapshot.child("option1").getValue().toString();
                            String option2= snapshot.child("option2").getValue().toString();
                            String option3= snapshot.child("option3").getValue().toString();
                            String option1_count= snapshot.child("option1_count").getValue().toString();
                            String option2_count= snapshot.child("option2_count").getValue().toString();
                            String option3_count= snapshot.child("option3_count").getValue().toString();
                            String dayOfMonth= snapshot.child("dayOfMonth").getValue().toString();
                            String month= snapshot.child("month").getValue().toString();
                            String year= snapshot.child("year").getValue().toString();
                            String hourOfDay= snapshot.child("hourOfDay").getValue().toString();
                            String minute= snapshot.child("minute").getValue().toString();
                            methodToProcess3(id, pollTitle, option1, option2, option3, option1_count,option2_count,option3_count,dayOfMonth,month,year,hourOfDay, minute);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }

            }
        };
    }

    private void methodToProcess3(String id, String pollTitle, String option1, String option2, String option3, String option1_count, String option2_count, String option3_count, String dayOfMonth, String month, String year, String hourOfDay, String minute) {
        Boolean expired = false;
        //Get Current Date Time
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String getCurrentDateTime = sdf.format(c.getTime());
        int i=Integer.parseInt(month);
        if (i<10){
            month = '0'+String.valueOf(i);
        }else{
            month = String.valueOf(i);
        }

        int j=Integer.parseInt(dayOfMonth);
        if (j<10){
            dayOfMonth = '0'+String.valueOf(j);
        }else{
            dayOfMonth = String.valueOf(j);
        }

        String getMyTime = year+'/'+month+'/'+dayOfMonth+' '+hourOfDay+':'+minute;
        if (getCurrentDateTime.compareTo(getMyTime) < 0)
        {
            expired = false;
        }
        else
        {
            expired = true;
        }
        Intent intent = new Intent(getActivity(), VoteActivity.class);
        intent.putExtra("title", pollTitle);
        intent.putExtra("option1", option1);
        intent.putExtra("option2", option2);
        intent.putExtra("option3", option3);
        intent.putExtra("option1_count", option1_count);
        intent.putExtra("option2_count", option2_count);
        intent.putExtra("option3_count", option3_count);
        intent.putExtra("expired", expired);
        intent.putExtra("id", id);
        startActivity(intent);
    }


    private void methodToProcess(String id, String petitionTitle, String description, String petition_no, String dayOfMonth, String month, String year, String hourOfDay, String minute) {

        Boolean expired = false;
        //Get Current Date Time
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String getCurrentDateTime = sdf.format(c.getTime());
        int i=Integer.parseInt(month);
        if (i<10){
            month = '0'+String.valueOf(i);
        }else{
            month = String.valueOf(i);
        }

        int j=Integer.parseInt(dayOfMonth);
        if (j<10){
            dayOfMonth = '0'+String.valueOf(j);
        }else{
            dayOfMonth = String.valueOf(j);
        }

        String getMyTime = year+'/'+month+'/'+dayOfMonth+' '+hourOfDay+':'+minute;
        if (getCurrentDateTime.compareTo(getMyTime) < 0)
        {
            expired = false;
        }
        else
        {
            expired = true;
        }

        Intent intent = new Intent(getActivity(), SignPetitionActivity.class);
        intent.putExtra("title", petitionTitle);
        intent.putExtra("description", description);
        intent.putExtra("count", petition_no);
        intent.putExtra("expired", expired);
        intent.putExtra("id", id);

        startActivity(intent);

    }

    private void methodToProcess2(String id, String electionTitle, String president_candidate1, String president_candidate2, String vice_president_candidate1, String vice_president_candidate2, String p1_count, String p2_count, String vp1_count, String vp2_count, String dayOfMonth, String month, String year, String hourOfDay, String minute) {
        Boolean expired = false;
        //Get Current Date Time
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String getCurrentDateTime = sdf.format(c.getTime());
        int i=Integer.parseInt(month);
        if (i<10){
            month = '0'+String.valueOf(i);
        }else{
            month = String.valueOf(i);
        }

        int j=Integer.parseInt(dayOfMonth);
        if (j<10){
            dayOfMonth = '0'+String.valueOf(j);
        }else{
            dayOfMonth = String.valueOf(j);
        }

        String getMyTime = year+'/'+month+'/'+dayOfMonth+' '+hourOfDay+':'+minute;
        if (getCurrentDateTime.compareTo(getMyTime) < 0)
        {
            expired = false;
            Intent intent = new Intent(getActivity(), FaceDetectionActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("title", electionTitle);
            intent.putExtra("president_candidate1", president_candidate1);
            intent.putExtra("president_candidate2", president_candidate2);
            intent.putExtra("vice_president_candidate1", vice_president_candidate1);
            intent.putExtra("vice_president_candidate2", vice_president_candidate2);
            startActivity(intent);
        }
        else
        {
            expired = true;
            Intent intent = new Intent(getActivity(), ResultElectionActivity.class);
            intent.putExtra("title", electionTitle);
            intent.putExtra("president_candidate1", president_candidate1);
            intent.putExtra("president_candidate2", president_candidate2);
            intent.putExtra("vice_president_candidate1", vice_president_candidate1);
            intent.putExtra("vice_president_candidate2", vice_president_candidate2);
            intent.putExtra("p1_count", p1_count);
            intent.putExtra("p2_count", p2_count);
            intent.putExtra("vp1_count", vp1_count);
            intent.putExtra("vp2_count", vp2_count);

            startActivity(intent);
        }


    }
}