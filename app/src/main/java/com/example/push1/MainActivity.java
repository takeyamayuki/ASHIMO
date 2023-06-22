package com.example.push1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private TextView messageView;
    private BroadcastReceiver messageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("TOPICNAME");
        setContentView(R.layout.activity_main);
        messageView=findViewById(R.id.textView);
        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                messageView.setText(message);
            }
        };

        // デバイストークンの取得
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isComplete()){
                    String token = task.getResult();
                    Log.i("FIREBASE", "[CALLBACK] Token = " + token);
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Register the receiver
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(messageReceiver, new IntentFilter(MyFirebaseMessagingService.INTENT_FILTER));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }
}