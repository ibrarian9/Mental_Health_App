package com.app.lombaapp.Model;

public class DocChat {
    private String msgId, senderId, chat;

    public DocChat(String msgId, String senderId, String chat) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.chat = chat;
    }

    public DocChat() {

    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
