package com.pranayaa.howardchat;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pranayaa on 8/7/17.
 */

public class MessageSource {
    public interface MessageListener {
        void onMessageReceived(List<Message> messageList);
    }

    private static MessageSource sMessageSource;

    private Context mContext;

    public static MessageSource get(Context context) {
        if (sMessageSource == null) {
            sMessageSource = new MessageSource(context);
        }
        return sMessageSource;
    }

    private MessageSource(Context context) {
        mContext = context;
    }

    public void getMessages(final MessageListener messageListener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference messageRef = database.getReference("messages");
        Query lastSomeMessages = messageRef.limitToLast(60);
        lastSomeMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Message> messages = new ArrayList<>();
                Iterable<DataSnapshot> messagesSnaphots = dataSnapshot.getChildren();
                for (DataSnapshot mySnapshot: messagesSnaphots){
                    if(mySnapshot.child("fromUserId").getValue() != null){
                        Message myText = new Message(mySnapshot);
                        messages.add(myText);
                    }
                }
                messageListener.onMessageReceived(messages);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    public void sendMessage(Message message) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference messageRef = database.getReference("messages");

        DatabaseReference newPingRef = messageRef.push();
        Map<String, Object> messageValMap = new HashMap<>();

        messageValMap.put("content", message.getUserMessage());
        messageValMap.put("fromUserName", message.getUserName());
        messageValMap.put("fromUserId", message.getUserId());


        newPingRef.setValue(messageValMap);
    }
}
