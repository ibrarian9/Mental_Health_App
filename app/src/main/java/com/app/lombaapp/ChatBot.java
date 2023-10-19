package com.app.lombaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.lombaapp.Adapter.ChatBotAdapter;
import com.app.lombaapp.Model.BotChat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatBot extends AppCompatActivity {

    RecyclerView rv;
    EditText edChat;
    ImageView btnSend, poto;
    List<BotChat> botChatList;
    ChatBotAdapter chatBotAdapter;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        botChatList = new ArrayList<>();

        rv = findViewById(R.id.rvChatbot);
        edChat = findViewById(R.id.pesan);
        btnSend = findViewById(R.id.kirim);
        poto = findViewById(R.id.profile);

        // Set Poto
        poto.setImageResource(R.drawable.profile_bot);

        // Setup Recycler View
        chatBotAdapter = new ChatBotAdapter(botChatList);
        rv.setAdapter(chatBotAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        rv.setLayoutManager(llm);

        btnSend.setOnClickListener(v -> {
            String question = edChat.getText().toString().trim();

            if (question.equals("")){
                Toast.makeText(this,"Ketik Sesuatu...", Toast.LENGTH_SHORT).show();
            } else {
                addToChat(question,BotChat.SENT_BY_ME);
                edChat.setText("");
                callAPI(question);
            }
        });
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            botChatList.add(new BotChat(message, sentBy));
            chatBotAdapter.notifyDataSetChanged();
            rv.smoothScrollToPosition(chatBotAdapter.getItemCount());
        });
    }

    void addResponse(String response) {
        botChatList.remove(botChatList.size()-1);
        addToChat(response,BotChat.SENT_BY_BOT);
    }

    void callAPI(String question){
        //okhttp
        botChatList.add(new BotChat("Typing...",BotChat.SENT_BY_BOT));

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "text-davinci-003");
            jsonBody.put("prompt", question);
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body().toString());
                }
            }
        });
    }
}