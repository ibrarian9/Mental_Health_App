package com.app.lombaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.lombaapp.Model.Diary;
import com.app.lombaapp.Util.Utility;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class AddDiary extends AppCompatActivity {

    EditText edTitle, edText;
    ImageView ivDone;
    Button delBtn;
    String title, content, docId;
    TextView pageTitle;
    DocumentReference documentReference;
    boolean isEditMode = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        edTitle = findViewById(R.id.edTitle);
        edText = findViewById(R.id.edText);
        ivDone = findViewById(R.id.ivDone);
        delBtn = findViewById(R.id.delBtn);
        pageTitle = findViewById(R.id.pageTitle);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()){
            isEditMode = true;
        }
        edTitle.setText(title);
        edText.setText(content);
        if (isEditMode){
            pageTitle.setText("Edit your Diary");
            delBtn.setVisibility(View.VISIBLE);
        }

        ivDone.setOnClickListener(v -> saveDiary());
        delBtn.setOnClickListener(v -> deleteDiaryFromFirebase());
    }

    private void saveDiary() {
        String noteTitle = edTitle.getText().toString();
        String noteText = edText.getText().toString();
        if (noteTitle.isEmpty()) {
            edTitle.setError("Title Is Required");
        }else if (noteText.isEmpty()) {
            edTitle.setError("Note Is Required");
        }
        Diary diary = new Diary();
        diary.setTitle(noteTitle);
        diary.setContent(noteText);
        diary.setTimestamp(Timestamp.now());

        setSaveDiaryToFirebase(diary);
    }
    private void setSaveDiaryToFirebase(Diary diary) {

        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForDiary().document(docId);
        } else {
            documentReference = Utility.getCollectionReferenceForDiary().document();
        }

        documentReference.set(diary).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               Utility.showToast(AddDiary.this, "Diary Added Succesfully");
               finish();
           } else {
               Utility.showToast(AddDiary.this, "Failed Added Diary");
               finish();
           }
        });
    }

    private void deleteDiaryFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForDiary().document(docId);
        documentReference.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Utility.showToast(AddDiary.this, "Diary deleted succesfully");
                finish();
            } else {
                Utility.showToast(AddDiary.this, "Failed while deleting diary");
                finish();
            }
        });
    }

}