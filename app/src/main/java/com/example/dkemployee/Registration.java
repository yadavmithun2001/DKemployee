package com.example.dkemployee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dkemployee.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Registration extends AppCompatActivity {
    TextView text_login;
    EditText editName,editEmail,editMobile,editPassword;
    CardView register;
    ProgressBar pg;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        text_login =findViewById(R.id.text_login);
        text_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(Registration.this,HomePage.class);
            startActivity(intent);
            finish();
        }

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        editName = findViewById(R.id.editname);
        editEmail = findViewById(R.id.editemail);
        editMobile = findViewById(R.id.editmobile);
        editPassword = findViewById(R.id.editpassword);

        register = findViewById(R.id.register);
        pg = findViewById(R.id.progressBar2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _register(editName.getText().toString(),editEmail.getText().toString(),editMobile.getText().toString(),editPassword.getText().toString());
            }
        });

    }
    void _register(String name,String email,String mobile,String password){
        if(name.isEmpty()){
            FancyToast.makeText(Registration.this,"Name can't be Empty",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }else if(email.isEmpty()){
            FancyToast.makeText(Registration.this,"Email can't be Empty",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }else if(mobile.isEmpty()){
            FancyToast.makeText(Registration.this,"Mobile Number can't be Empty",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }else if(mobile.length() < 10){
            FancyToast.makeText(Registration.this,"Enter a valid mobile number",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }else if(password.isEmpty()){
            FancyToast.makeText(Registration.this,"Password can't be Empty",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }else if(password.length() < 6){
            FancyToast.makeText(Registration.this,"Password Should contain 6 characters",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
        }else {
            register.setVisibility(View.INVISIBLE);
            pg.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                UserModel userModel = new UserModel(auth.getUid(),name,email,mobile,"noimage");
                                firebaseDatabase.getReference()
                                        .child("employeedata")
                                        .child(auth.getUid())
                                        .setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            auth.signInWithEmailAndPassword(email,password)
                                                    .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if(task.isSuccessful()){
                                                                FancyToast.makeText(Registration.this,"Employee Registered Successfully",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                                                                Intent intent = new Intent(Registration.this,HomePage.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }else {
                                                                FancyToast.makeText(Registration.this,task.getException().getMessage(),FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                                                                pg.setVisibility(View.GONE);
                                                                register.setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                    });

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        FancyToast.makeText(Registration.this,e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                                        pg.setVisibility(View.GONE);
                                        register.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FancyToast.makeText(Registration.this,e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                    pg.setVisibility(View.GONE);
                    register.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}