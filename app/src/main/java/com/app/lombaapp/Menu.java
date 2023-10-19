package com.app.lombaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity {

    DatabaseReference reference;
    ImageView chatDoc, chatBot, diary, foto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        String uid = FirebaseAuth.getInstance().getUid();
        assert uid != null;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    foto = findViewById(R.id.foto);
                    String role = snapshot.child("role").getValue(String.class);
                    assert role != null;
                    if (role.equals("user")){
                        foto.setImageResource(R.drawable.user_profile);
                    } else {
                        foto.setImageResource(R.drawable.profile_doc);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chatDoc = findViewById(R.id.ivDoc);
        chatDoc.setOnClickListener(view -> startActivity(new Intent(Menu.this, DoctorList.class)));

        chatBot = findViewById(R.id.ivBot);
        chatBot.setOnClickListener(view -> startActivity(new Intent(Menu.this, ChatBot.class)));

        diary = findViewById(R.id.ivDiary);
        diary.setOnClickListener(view -> startActivity(new Intent(Menu.this, DiaryActivity.class)));

        BottomNavigationView bottomNavigationView = findViewById(R.id.botNavbar);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, Profile.class));
                return true;
            } else if (itemId == R.id.nav_about) {
                startActivity(new Intent(this, About.class));
                return true;
            }
            return false;
        });
    }
}