package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Data";

    private static final String KEY1 = "mood";
    private static final String KEY2 = "thoughts";
    private static final String KEY3 = "timestamp";
    private static final String KEY4 = "user_uid";

    private static final String CHANNEL_ID = "6";

    private NotificationManagerCompat notificationManagerCompat;

    private FirebaseAnalytics mFirebaseAnalytics;
    private CollectionReference myCollection;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myCollection = db.collection("messages");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        createNotificationChannel();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManagerCompat = NotificationManagerCompat.from(this);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "name", importance);
            channel.setDescription("description");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this

    }

    private void sendNotification() {
        Intent intent = new Intent(MainActivity.this, Wall.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("You've successfully submitted your thoughts!")
                .setContentText("Click to see your most recent thoughts.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(Color.CYAN)
                .build();

        notificationManagerCompat.notify(1, notification);

    }

    public void submit(View view) {
        mFirebaseAnalytics.logEvent("button_clicked", null);
        EditText text1 = findViewById(R.id.editText1);
        EditText text2 = findViewById(R.id.editText2);
        String mood = text1.getText().toString().trim();
        String thoughts = text2.getText().toString().trim();

        if (mood.isEmpty() || thoughts.isEmpty()) {
            return;
        }

        SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
        String timestamp = date.format(new Date());

        Map<String, Object> data = new HashMap<>();
        data.put(KEY1, mood);
        data.put(KEY2, thoughts);
        data.put(KEY3, timestamp);
        data.put(KEY4, mAuth.getCurrentUser().getUid());

        myCollection.add(data) //replaces old stuff
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(MainActivity.this, "Message added! Check your notifications.", Toast.LENGTH_LONG).show();
                        sendNotification();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        db.collection("messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void signOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, Login.class));
    }



}