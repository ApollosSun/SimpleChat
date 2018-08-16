package com.example.simplechat.simplechat;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message implements Parcelable{

    private String message;
    private User user;
    private boolean belongsToCurrentUser;
    private String currentTime;

    public Message (String message, User user, boolean belongsToCurrentUser){
        this.message = message;
        this.user = user;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.currentTime = getTime();
    }

    protected Message(Parcel in) {
        this.message = in.readString();
        this.user = new User(in.readString());
        this.belongsToCurrentUser = in.readByte() != 0;
        this.currentTime = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(user.getName());
        dest.writeByte((byte) (belongsToCurrentUser ? 1 : 0));
        dest.writeString(currentTime);
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public boolean isBelongsToMe() {
        return belongsToCurrentUser;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    private String getTime(){
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("HH:mm");
        String date = df.format(currentTime);
        return date;
    }
}
