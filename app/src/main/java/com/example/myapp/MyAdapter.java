package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Message> messageArrayList;

    public MyAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.message, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Message message = messageArrayList.get(position);
        holder.mood.setText(message.mood);
        holder.thoughts.setText(message.thoughts);
        System.out.println(message.mood + "  " +  message.thoughts);

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mood;
        TextView thoughts;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mood = itemView.findViewById(R.id.mood);
            thoughts = itemView.findViewById(R.id.thoughts);

        }
    }
}

