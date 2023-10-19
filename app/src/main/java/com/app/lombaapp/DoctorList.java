package com.app.lombaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.app.lombaapp.Adapter.DoctorAdapter;
import com.app.lombaapp.Model.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorList extends AppCompatActivity {

    ArrayList<UserDetails> list = new ArrayList<>();
    RecyclerView rv;
    DoctorAdapter docAdapter;
    TextView btnBack;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> startActivity(new Intent(this, Menu.class)));

        docAdapter = new DoctorAdapter(this, list);
        rv = findViewById(R.id.rvDoc);
        rv.setAdapter(docAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        String uid = FirebaseAuth.getInstance().getUid();
        assert uid != null;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String roleUser = snapshot.child("role").getValue(String.class);
                    if (roleUser != null){
                        if (roleUser.equals("user")){
                            GetListDoctor();
                        } else {
                            GetListUser();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetListUser() {
        Query listUser = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("role")
                .equalTo("user");
        listUser.addValueEventListener(valueEventListener);
    }

    private void GetListDoctor() {
        Query listDoc = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("role")
                .equalTo("doctor");
        listDoc.addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            docAdapter.clear();

            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String uid = dataSnapshot.getKey();
                assert uid != null;
                if (!uid.equals(FirebaseAuth.getInstance().getUid())) {
                    UserDetails uDetail = dataSnapshot.getValue(UserDetails.class);
                    list.add(uDetail);
                }
                docAdapter.notifyDataSetChanged();
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}