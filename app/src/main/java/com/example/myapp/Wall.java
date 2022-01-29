package com.example.myapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Wall extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Message> messageArrayList;
    private MyAdapter myAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        messageArrayList = new ArrayList<Message>();
        myAdapter = new MyAdapter(Wall.this, messageArrayList);

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();

    }

    private void EventChangeListener() {
        mAuth = FirebaseAuth.getInstance();
        String user_uid = mAuth.getCurrentUser().getUid();
        db.collection("messages").orderBy("timestamp", Query.Direction.DESCENDING).limit(10).whereEqualTo("user_uid", user_uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore error.", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc: value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                messageArrayList.add(dc.getDocument().toObject(Message.class));

                            }
                            myAdapter.notifyDataSetChanged();
                         }
                    }
                });
    }

    public void previous(View view) {
        startActivity(new Intent(Wall.this, MainActivity.class));
    }
}
