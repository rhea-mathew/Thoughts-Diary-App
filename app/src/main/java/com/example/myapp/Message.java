package com.example.myapp;


public class Message {

    public String mood, thoughts, timestamp, user_uid;

    public Message () {

    }

    public Message(String mood, String thoughts, String timestamp, String user_uid) {
        this.mood = mood;
        this.thoughts = thoughts;
        this.timestamp = timestamp;
        this.user_uid = user_uid;
    }

    public String getMood() {
        return mood;
    }
    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getThoughts() {
        return thoughts;
    }
    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }


    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public String getUser_uid() {
        return user_uid;
    }
    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

}