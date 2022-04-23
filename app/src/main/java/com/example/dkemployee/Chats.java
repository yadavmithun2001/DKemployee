package com.example.dkemployee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.dkemployee.Adapters.MessageAdapter;
import com.example.dkemployee.Models.ChatModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Chats extends AppCompatActivity {

    TextView name;
    ImageView pfpic;

    CardView sendmsg;
    ImageView back;
    EditText typeamessage;
    RecyclerView chatrecycle;
    ChatModel chatModel;
    ArrayList<ChatModel> list = new ArrayList<>();
    String senderRoom,recieverRoom;
    MessageAdapter messageAdapter;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        name = findViewById(R.id.name);
        pfpic = findViewById(R.id.pfimage);
        name.setText(getIntent().getStringExtra("name"));
        Glide.with(this)
                .load(getIntent().getStringExtra("pfpic"))
                .placeholder(R.drawable.img)
                .into(pfpic);

        back = findViewById(R.id.imageView3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sendmsg = findViewById(R.id.sendmessage);
        typeamessage = findViewById(R.id.typeamessage);

        chatrecycle = findViewById(R.id.chat_recycle);
        messageAdapter = new MessageAdapter(list,this);
        chatrecycle.setAdapter(messageAdapter);
        chatrecycle.setLayoutManager(new LinearLayoutManager(this));
        chatrecycle.scrollToPosition(list.size()-1);

        database = FirebaseDatabase.getInstance();
        String recieveruid = getIntent().getStringExtra("uid");
        String senderuid = FirebaseAuth.getInstance().getUid();
        senderRoom = senderuid + recieveruid;
        recieverRoom = recieveruid + senderuid;

        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                            list.add(chatModel);
                        }
                        messageAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String localTime = date.format(currentLocalTime);

        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeamessage.getText().toString().isEmpty()){
                    FancyToast.makeText(Chats.this,"Please enter a message",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else {
                    String messagetxt = typeamessage.getText().toString();
                    ChatModel chatModel = new ChatModel(messagetxt,senderuid,localTime);
                    list.add(new ChatModel(messagetxt,senderuid,localTime));

                    typeamessage.setText("");
                    database.getReference().child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .push()
                            .setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            database.getReference().child("chats")
                                    .child(recieverRoom)
                                    .child("messages")
                                    .push()
                                    .setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });
                        }
                    });

                }


            }
        });





    }
}