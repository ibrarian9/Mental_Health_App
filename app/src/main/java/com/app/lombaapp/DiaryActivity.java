package com.app.lombaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.app.lombaapp.Adapter.DiaryAdapter;
import com.app.lombaapp.Model.Diary;
import com.app.lombaapp.Util.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class DiaryActivity extends AppCompatActivity {

    DiaryAdapter diaryAdapter;
    FloatingActionButton addDiaryBtn;
    RecyclerView rv;
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        back = findViewById(R.id.btnBack);
        back.setOnClickListener(view -> startActivity(new Intent(this, Menu.class)));

        addDiaryBtn = findViewById(R.id.addBtn);
        rv = findViewById(R.id.rvDiary);

        addDiaryBtn.setOnClickListener(v -> startActivity(new Intent(this, AddDiary.class)));
        setUpRv();
    }

    void setUpRv() {
        Query query = Utility.getCollectionReferenceForDiary().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Diary> options = new FirestoreRecyclerOptions.Builder<Diary>()
                .setQuery(query, Diary.class).build();
        rv.setLayoutManager(new LinearLayoutManager(this));
        diaryAdapter = new DiaryAdapter(options, this);
        rv.setAdapter(diaryAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        diaryAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        diaryAdapter.stopListening();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        diaryAdapter.notifyDataSetChanged();
    }
}