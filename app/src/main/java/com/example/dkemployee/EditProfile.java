package com.example.dkemployee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.dkemployee.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

public class EditProfile extends AppCompatActivity {
    ImageView back;
    FirebaseAuth auth;
    FirebaseDatabase database;
    UserModel userModel;
    EditText editName,editEmail,editMobile;
    CardView save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        back = findViewById(R.id.imageView3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editMobile = findViewById(R.id.editMobile);

        database.getReference()
                .child("employeedata")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModel = snapshot.getValue(UserModel.class);
                        editName.setText(userModel.getName()+"");
                        editEmail.setText(userModel.getEmail()+"");
                        editMobile.setText(userModel.getMobile()+"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _saveUserDetails(editName.getText().toString(),editEmail.getText().toString(),editMobile.getText().toString());
            }
        });

    }
    void _saveUserDetails(String name,String email,String mobile){
        if(mobile.length() < 10){
            FancyToast.makeText(EditProfile.this,"Please enter valid phone number",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }else {
            userModel.setName(name);
            userModel.setEmail(email);
            userModel.setMobile(mobile);
            database.getReference()
                    .child("employeedata")
                    .child(auth.getUid())
                    .setValue(userModel).addOnCompleteListener(EditProfile.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        FancyToast.makeText(EditProfile.this,"Your details updated successfully",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                        Intent intent = new Intent(EditProfile.this,Profile.class);
                        startActivity(intent);
                        finish();
                    }else {
                        FancyToast.makeText(EditProfile.this,task.getException().getMessage(),FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                    }
                }
            });
        }

    }
}