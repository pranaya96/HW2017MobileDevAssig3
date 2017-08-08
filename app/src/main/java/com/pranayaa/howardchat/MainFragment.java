package com.pranayaa.howardchat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by pranayaa on 8/7/17.
 */

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        // Get references to List views to display database data.
        final ListView messagesListView = v.findViewById(R.id.all_messages);

        //setup messagesListView
        MessageSource.get(getContext()).getMessages(new MessageSource.MessageListener() {

            @Override
            public void onMessageReceived(List<Message> messageList) {
                if(getActivity()!=null){
                    MessageArrayAdapter adapter= new MessageArrayAdapter(getActivity(), messageList);
                    messagesListView.setAdapter(adapter);
                    // Whenever we set a new adapter (which usually scrolls to the top of the contents), scroll to the bottom of the contents.
                    messagesListView.setSelection(adapter.getCount() - 1);
                }

            }
        });
        //get the editText
        final EditText messageEditText = (EditText) v.findViewById(R.id.send_message_text);
        messageEditText.setHint("enter message");




        Button button = v.findViewById(R.id.send_button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                Log.i("PA", "The user is: " + user);
//                Log.i("PA", "The user Name Display is: " + user.getDisplayName());
//                Log.i("PA", "The user ID is: " + user.getUid());

                if (user == null) {
                    Toast.makeText(getContext(), "Can't send message, not logged in", Toast.LENGTH_SHORT);
                    return;
                }else{
                    String messageText = messageEditText.getText().toString();
                    //Log.i("PA", "The message is: " + messageText);
                    Message message = new Message(messageText, user.getDisplayName(), user.getUid());
                    MessageSource.get(getContext()).sendMessage(message);
                    messageEditText.setText("");

                }
            }
        });
        return v;
    }



    private class MessageArrayAdapter extends BaseAdapter {
        protected Context mContext;
        protected List<Message> mMessageList;
        protected LayoutInflater mLayoutInflater;
        public MessageArrayAdapter(Context context, List<Message> messageList) {
            mContext = context;
            mMessageList = messageList;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mMessageList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Message message = mMessageList.get(position);
            View rowView = mLayoutInflater.inflate(R.layout.list_item_message, parent, false);

            TextView title = rowView.findViewById(R.id.userName_text_view);
            title.setText(message.getUserName());

            TextView subtitle = rowView.findViewById(R.id.userMessage_text_view);
            subtitle.setText(message.getUserMessage());

            return rowView;
        }
    }














    }
