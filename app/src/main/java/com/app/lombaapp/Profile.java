package com.app.lombaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.lombaapp.Model.UserDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btnSignOut;
    TextView tvName, tvEmail, tvRole;
    ImageView poto;
    FirebaseUser user;
    FirebaseDatabase fData;
    DatabaseReference dReference;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        mAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();
        dReference = fData.getReference("Users");

        btnSignOut = findViewById(R.id.btnSignOut);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvRole = findViewById(R.id.tvRole);
        poto = findViewById(R.id.ivProfile);

        user = mAuth.getCurrentUser();

        if (user == null) {
            Intent i = new Intent(Profile.this, Login.class);
            startActivity(i);
            finish();
        } else {
            tvEmail.setText("Email : " + user.getEmail());
            showUserProfile(user);
        }

        btnSignOut.setOnClickListener(v -> {
            mAuth.signOut();
            Intent i = new Intent(Profile.this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(i);
            finish();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bNavbar);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, Menu.class));
                return true;
            } else  if (itemId == R.id.nav_profile){
                return true;
            } else if (itemId == R.id.nav_about) {
                startActivity(new Intent(this, About.class));
                return true;
            }
            return false;
        });
    }

    private void showUserProfile(FirebaseUser user) {
        String userId = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails uDetail = snapshot.getValue(UserDetails.class);
                if (uDetail != null){
                    String name = uDetail.getUserName();
                    String role = uDetail.getRole();
                    tvName.setText("Nama : " + name);
                    tvRole.setText("Role : " + role);
                    if (role.equals("user")){
                        poto.setImageResource(R.drawable.user_profile);
                    } else {
                        poto.setImageResource(R.drawable.profile_doc);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}