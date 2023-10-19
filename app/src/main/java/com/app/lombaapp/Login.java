package com.app.lombaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText edName, edPassword;
    Button btnLogin;
    ProgressBar pb;
    TextView btnRegist;
    String email, password;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(getApplicationContext(), Menu.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        mAuth = FirebaseAuth.getInstance();
        edName = findViewById(R.id.edName);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        pb = findViewById(R.id.pb);
        btnRegist = findViewById(R.id.btnRegis);

        btnRegist.setOnClickListener(v -> startActivity(new Intent(this, SignUp.class)));

        btnLogin.setOnClickListener(v -> {
            email = String.valueOf(edName.getText());
            password = String.valueOf(edPassword.getText());

            if (TextUtils.isEmpty(email)){
                NotifEmail();
            } else if (TextUtils.isEmpty(password)){
                NotifPassword();
            } else {
                pb.setVisibility(View.VISIBLE);
                AuthWithEmailAndPass();
            }
        });
    }

    private void AuthWithEmailAndPass() {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Menu.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                    }
                    pb.setVisibility(View.GONE);
                });
    }

    private void NotifPassword() {
        Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
    }
    private void NotifEmail() {
        Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
    }
}