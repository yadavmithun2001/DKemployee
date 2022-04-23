package com.example.dkemployee.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dkemployee.Chats;
import com.example.dkemployee.MainActivity;
import com.example.dkemployee.Models.UserModel;
import com.example.dkemployee.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.viewholder> {
    List<UserModel> list = new ArrayList<>();
    Context context;

    public ChatAdapter(List<UserModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_user_chat,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.viewholder holder, int position) {
        UserModel userModel = list.get(position);
        holder.name.setText(userModel.getName());

        Glide.with(context)
                .load(userModel.getPf_url())
                .placeholder(R.drawable.img)
                .into(holder.pfpic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chats.class);
                intent.putExtra("name",userModel.getName());
                intent.putExtra("pfpic",userModel.getPf_url());
                intent.putExtra("uid", userModel.getUid());
                context.startActivity(intent);
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String recieveruid = userModel.getUid();
        String senderuid = FirebaseAuth.getInstance().getUid();
        String senderRoom = senderuid + recieveruid;

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.deletemessage_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btn_yes,btn_no;
        btn_yes = dialog.findViewById(R.id.btn_yes);
        btn_no = dialog.findViewById(R.id.btn_no);

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.getReference()
                        .child("chats")
                        .child(senderRoom)
                        .child("messages").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Chats deleted", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialog.show();
                return false;
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView pfpic;
        TextView name;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            pfpic = itemView.findViewById(R.id.pfimage);
            name = itemView.findViewById(R.id.name);
        }
    }
}
