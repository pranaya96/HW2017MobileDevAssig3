package com.pranayaa.howardchat;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by pranayaa on 8/7/17.
 */

public class Message {
    private String mUserMessage;
    private String mUserName;
    private String mUserId;

    public Message(String userMessage, String userName, String userId){
        mUserMessage = userMessage;
        mUserName = userName;
        mUserId = userId;
    }

    public Message(DataSnapshot messageSnapshot){
        mUserMessage =  messageSnapshot.child("content").getValue(String.class);
        mUserName = messageSnapshot.child("fromUserName").getValue(String.class);
        mUserId = messageSnapshot.child("fromUserId").getValue(String.class);
    }

    public String getUserMessage() {
        return mUserMessage;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getUserId() {
        return mUserId;
    }
}
