package com.app.lombaapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lombaapp.Model.BotChat;
import com.app.lombaapp.R;

import java.util.List;

public class ChatBotAdapter extends RecyclerView.Adapter<ChatBotAdapter.ViewHolder> {

    private List<BotChat> chatBotList;

    public ChatBotAdapter(List<BotChat> chatBotList) {
        this.chatBotList = chatBotList;
    }

    @NonNull
    @Override
    public ChatBotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bot = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_bot, null);
        return new ViewHolder(bot);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBotAdapter.ViewHolder holder, int position) {
        BotChat botChat = chatBotList.get(position);
        if (botChat.getSentBy().equals(BotChat.SENT_BY_ME)) {
            holder.leftChat.setVisibility(View.GONE);
            holder.rightChat.setVisibility(View.VISIBLE);
            holder.rightTv.setText(botChat.getMessage());
        } else {
            holder.rightChat.setVisibility(View.GONE);
            holder.leftChat.setVisibility(View.VISIBLE);
            holder.leftTv.setText(botChat.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chatBotList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftChat, rightChat;
        TextView leftTv, rightTv;
        public ViewHolder(@NonNull View v) {
            super(v);
            leftChat = v.findViewById(R.id.leftChatView);
            rightChat = v.findViewById(R.id.rightChatView);
            leftTv = v.findViewById(R.id.leftTv);
            rightTv = v.findViewById(R.id.rightTv);
        }
    }
}
