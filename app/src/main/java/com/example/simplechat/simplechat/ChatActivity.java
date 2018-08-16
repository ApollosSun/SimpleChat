package com.example.simplechat.simplechat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    final private String chanelId = "7CxtsNJSFPgvJLqW";
    private String roomName;
    private User newUser;

    private EditText editText;
    private Scaledrone scaledrone;
    private MessageAdapter messageAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getArguments(getIntent().getExtras());
        editText = findViewById(R.id.text_field);

        messageAdapter = new MessageAdapter(this);
        listView = (ListView) findViewById(R.id.message_list);
        listView.setAdapter(messageAdapter);

        scaledrone = new Scaledrone(chanelId, newUser);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                scaledrone.subscribe(roomName, new RoomListener() {
                    @Override
                    public void onOpen(Room room) {
                        Log.i(LOG_TAG, "The room is opened");
                    }

                    @Override
                    public void onOpenFailure(Room room, Exception ex) {
                        Log.i(LOG_TAG, "Failed to subscribe to room: " + ex.getMessage());
                    }

                    @Override
                    public void onMessage(Room room, JsonNode json, Member member) {
                        final ObjectMapper mapper = new ObjectMapper();

                        try {

                            final User user = mapper.treeToValue(member.getClientData(), User.class);
                            boolean belongsToMe = member.getId().equals(scaledrone.getClientID());
                            final Message message = new Message(json.asText(), user, belongsToMe);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    messageAdapter.add(message);
                                    listView.setSelection(listView.getCount() - 1);
                                }
                            });

                        } catch (JsonProcessingException e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onOpenFailure(Exception ex) {
                Log.i(LOG_TAG, "Failed to open connection: " + ex.getMessage());
            }

            @Override
            public void onFailure(Exception ex) {
                Log.i(LOG_TAG, "Unexcpected failure: " + ex.getMessage());
                ex.printStackTrace();
            }

            @Override
            public void onClosed(String reason) {
                Log.i(LOG_TAG, "Connection closed: " + reason);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ArrayList<Message> messages = messageAdapter.getMessages();
        outState.putParcelableArrayList("Messages", messages);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        ArrayList<Message> messages = savedInstanceState.getParcelableArrayList("Messages");
        messageAdapter.add(messages);
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void sendMessage (View view) {
        String message = editText.getText().toString();
        if (message.length() > 0){
            scaledrone.publish(roomName, message);
            editText.getText().clear();
        }
    }

    void getArguments (Bundle arguments) {

        if (arguments.get("room").toString().isEmpty()){
            roomName = "observable-default-room";
        } else {
            roomName = "observable-" + arguments.get("room").toString();
        }

        if (arguments.get("user").toString().isEmpty()){
            newUser = new User("Default-user");
        } else {
            newUser = new User(arguments.get("user").toString());
        }
    }

}
