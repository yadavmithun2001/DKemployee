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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity {
    TextView text_register,forgot_password;
    CardView login;
    EditText editEmail,editPassword;
    ProgressBar pg;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        text_register = findViewById(R.id.text_register);
        forgot_password = findViewById(R.id.forgotpassword);

        auth = FirebaseAuth.getInstance();

        text_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Registration.class);
                startActivity(intent);
                finish();
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });

        editEmail = findViewById(R.id.editemail);
        editPassword = findViewById(R.id.editpassword);
        pg = findViewById(R.id.progressBar2);


        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _login(editEmail.getText().toString(),editPassword.getText().toString());
            }
        });

    }
    void _login(String email,String password){
       if(email.isEmpty()){
           FancyToast.makeText(Login.this,"Email can't be Empty",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
       }else if(password.isEmpty()){
           FancyToast.makeText(Login.this,"Password can't be Empty",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
       }else if(password.length() < 6){
           FancyToast.makeText(Login.this,"Password should contain 6 character",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
       }
       else {
           login.setVisibility(View.INVISIBLE);
           pg.setVisibility(View.VISIBLE);
           auth.signInWithEmailAndPassword(email,password)
                   .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()){
                               FancyToast.makeText(Login.this,"Logged in Successfully",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                               Intent intent = new Intent(Login.this,HomePage.class);
                               startActivity(intent);
                               finish();
                               pg.setVisibility(View.GONE);
                               login.setVisibility(View.VISIBLE);
                           }else {
                               FancyToast.makeText(Login.this,task.getException().getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                               pg.setVisibility(View.GONE);
                               login.setVisibility(View.VISIBLE);
                           }
                       }
                   });
       }
    }
}