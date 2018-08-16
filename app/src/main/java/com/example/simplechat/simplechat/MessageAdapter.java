package com.example.simplechat.simplechat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {

    private ArrayList<Message> messages = new ArrayList<>();
    private Context context;

    public MessageAdapter (Context context){
        this.context = context;
    }

    public void add (Message message){
        this.messages.add(message);
        notifyDataSetChanged();
    }

    public void add (ArrayList<Message> messages){
        for (Message m : messages){
            this.messages.add(m);
            notifyDataSetChanged();
        }
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(position);

        if (message.isBelongsToMe()){

            convertView = layoutInflater.inflate(R.layout.sample_my_message, null);
            holder.messageField = convertView.findViewById(R.id.message_field);
            holder.timeField = convertView.findViewById(R.id.time_field);
            convertView.setTag(holder);

            holder.messageField.setText(message.getMessage());
            holder.timeField.setText(message.getCurrentTime());

        } else {

            convertView = layoutInflater.inflate(R.layout.sample_other_message, null);
            holder.name = convertView.findViewById(R.id.name_field);
            holder.messageField = convertView.findViewById(R.id.message_field);
            holder.timeField = convertView.findViewById(R.id.time_field);
            convertView.setTag(holder);

            holder.name.setText(message.getUser().getName());
            holder.messageField.setText(message.getMessage());
            holder.timeField.setText(message.getCurrentTime());

        }

        return convertView;
    }

    class MessageViewHolder {
        public TextView name;
        public TextView messageField;
        public TextView timeField;
    }

}
