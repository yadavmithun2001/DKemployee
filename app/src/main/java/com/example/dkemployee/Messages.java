package com.example.dkemployee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dkemployee.Adapters.ChatAdapter;
import com.example.dkemployee.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Messages extends AppCompatActivity {
    ImageView back;
    RecyclerView recycle_chat;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    UserModel userModel;
    ArrayList<UserModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        back = findViewById(R.id.imageView3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recycle_chat = findViewById(R.id.recycle_chat);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        list = new ArrayList<>();
        firebaseDatabase.getReference()
                .child("employeedata")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            userModel = dataSnapshot.getValue(UserModel.class);
                            list.add(userModel);
                        }

                        ChatAdapter chatAdapter = new ChatAdapter(list,Messages.this);
                        recycle_chat.setAdapter(chatAdapter);
                        recycle_chat.setLayoutManager(new LinearLayoutManager(Messages.this));
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






    }
}