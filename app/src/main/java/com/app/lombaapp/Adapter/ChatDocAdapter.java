package com.app.lombaapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lombaapp.Model.DocChat;
import com.app.lombaapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ChatDocAdapter extends RecyclerView.Adapter<ChatDocAdapter.MyViewHolder> {

    private List<DocChat> list;
    private Context context;

    public ChatDocAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void add(DocChat chat) {
        list.add(chat);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatDocAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_doc, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatDocAdapter.MyViewHolder holder, int position) {
        DocChat chat = list.get(position);

        if (chat.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.leftChat.setVisibility(View.GONE);
            holder.rightChat.setVisibility(View.VISIBLE);
            holder.rightTv.setText(chat.getChat());
        } else {
            holder.rightChat.setVisibility(View.GONE);
            holder.leftChat.setVisibility(View.VISIBLE);
            holder.leftTv.setText(chat.getChat());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftChat, rightChat;
        TextView leftTv, rightTv;

        public MyViewHolder(@NonNull View v) {
            super(v);
            leftChat = v.findViewById(R.id.leftChatView);
            rightChat = v.findViewById(R.id.rightChatView);
            leftTv = v.findViewById(R.id.leftTv);
            rightTv = v.findViewById(R.id.rightTv);
        }
    }
}
