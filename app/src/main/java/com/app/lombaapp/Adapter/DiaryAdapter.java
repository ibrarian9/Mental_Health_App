package com.app.lombaapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lombaapp.AddDiary;

import com.app.lombaapp.Model.Diary;
import com.app.lombaapp.R;
import com.app.lombaapp.Util.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DiaryAdapter extends FirestoreRecyclerAdapter<Diary, DiaryAdapter.MyViewHolder> {

    Context context;
    public DiaryAdapter(@NonNull FirestoreRecyclerOptions<Diary> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DiaryAdapter.MyViewHolder holder, int position, @NonNull Diary model) {
        holder.tvTitle.setText(model.getTitle());
        holder.tvText.setText(model.getContent());
        holder.tvWaktu.setText(Utility.timestampToString(model.getTimestamp()));

        holder.rl.setOnClickListener(v -> {
            Intent i = new Intent(context, AddDiary.class);
            i.putExtra("title", model.getTitle());
            i.putExtra("content", model.getContent());
            String docId = this.getSnapshots().getSnapshot(position).getId();
            i.putExtra("docId", docId);
            context.startActivity(i);
        });
    }

    @NonNull
    @Override
    public DiaryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_diary, parent, false);
        return new MyViewHolder(v);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl;
        TextView tvTitle, tvText, tvWaktu;
        public MyViewHolder(@NonNull View v) {
            super(v);
            rl = v.findViewById(R.id.rl1);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvText = v.findViewById(R.id.tvText);
            tvWaktu = v.findViewById(R.id.tvWaktu);
        }
    }
}
