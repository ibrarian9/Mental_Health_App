package com.app.lombaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lombaapp.Model.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    EditText edName, edEmail, edPassword;
    ProgressBar pb;
    Button btnSign;
    FirebaseAuth mAuth;
    TextView btnLogin;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Menu.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        edName = findViewById(R.id.edName);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnSign = findViewById(R.id.btnSign);
        btnLogin = findViewById(R.id.btnLogin);
        pb = findViewById(R.id.pb);

        btnLogin.setOnClickListener(v -> startActivity(new Intent(this, Login.class)));

        btnSign.setOnClickListener(v -> {
            String nama, email, password;

            nama = String.valueOf(edName.getText());
            email = String.valueOf(edEmail.getText());
            password = String.valueOf(edPassword.getText());

            if (TextUtils.isEmpty(nama)){
                Toast.makeText(SignUp.this, "Enter Nama", Toast.LENGTH_LONG).show();
                edName.requestFocus();
            } else if (TextUtils.isEmpty(password)){
                Toast.makeText(SignUp.this, "Enter password", Toast.LENGTH_LONG).show();
                edPassword.requestFocus();
            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(SignUp.this, "Enter Email", Toast.LENGTH_LONG).show();
                edEmail.requestFocus();
            } else {
                pb.setVisibility(View.VISIBLE);
                registerUser(nama, email, password);
            }
        });
    }
    private void registerUser(String nama, String email, String password) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, task -> {
            if (task.isSuccessful()) {

                // Get Uid from Auth Database
                FirebaseUser fUser = mAuth.getCurrentUser();
                assert fUser != null;
                String uid = fUser.getUid();
                String role = "user";

                UserDetails user = new UserDetails(uid, nama, email, password, role);

                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
                dbReference.child(fUser.getUid()).setValue(user).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(SignUp.this, "Account create", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUp.this, Menu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, "Account Registered Failed", Toast.LENGTH_SHORT).show();
                        Log.d("Register", "registerUser: Error");
                        pb.setVisibility(View.GONE);
                    }
                });
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthUserCollisionException e) {
                    edEmail.setError("Email is Already registered");
                } catch (Exception e) {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.GONE);
            }
        });
    }
}