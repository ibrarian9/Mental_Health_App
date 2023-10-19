package com.app.lombaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lombaapp.Adapter.ChatDocAdapter;
import com.app.lombaapp.Model.DocChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatDoc extends AppCompatActivity {

    TextView tvName;
    EditText edText;
    ImageView ivSend, profile;
    RecyclerView rv;
    ChatDocAdapter docAdapter;
    String senderRoom, receiverRoom, receiverId;
    DatabaseReference rSender, rReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_doc);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        ivSend = findViewById(R.id.ivSend);
        edText = findViewById(R.id.edText);
        profile = findViewById(R.id.profile);

        String role = getIntent().getStringExtra("role");
        assert role != null;
        if (role.equals("user")){
            profile.setImageResource(R.drawable.user_profile);
        } else {
            profile.setImageResource(R.drawable.profile_doc);
        }

        receiverId = getIntent().getStringExtra("uid");
        senderRoom = FirebaseAuth.getInstance().getUid() + receiverId;
        receiverRoom = receiverId + FirebaseAuth.getInstance().getUid();

        String nama = getIntent().getStringExtra("nama");
        tvName = findViewById(R.id.tvNameDoc);
        tvName.setText(nama);

        rv = findViewById(R.id.rvChatDoc);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        docAdapter = new ChatDocAdapter(this);
        rv.setAdapter(docAdapter);

        rSender = FirebaseDatabase.getInstance().getReference("chat").child(senderRoom);
        rReceiver = FirebaseDatabase.getInstance().getReference("chat").child(receiverRoom);

        rSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                docAdapter.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DocChat chat = dataSnapshot.getValue(DocChat.class);
                    docAdapter.add(chat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ivSend.setOnClickListener(v -> {
            String getChat = edText.getText().toString();
            // get current timestamp
            String chatId = String.valueOf(System.currentTimeMillis()).substring(0, 10);

            if (getChat.equals("")){
                Toast.makeText(this,"Ketik Sesuatu...", Toast.LENGTH_SHORT).show();
            } else {
                DocChat chat = new DocChat(chatId, FirebaseAuth.getInstance().getUid(), getChat);
                docAdapter.add(chat);
                rSender.child(chatId).setValue(chat);
                rReceiver.child(chatId).setValue(chat);
            }
        });
    }
}