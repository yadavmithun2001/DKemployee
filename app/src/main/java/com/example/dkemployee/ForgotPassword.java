package com.example.dkemployee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

public class ForgotPassword extends AppCompatActivity {
    TextView text_register;
    CardView btn_continue;
    EditText editEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        editEmail = findViewById(R.id.editemail);
        text_register = findViewById(R.id.text_register);
        text_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassword.this,Registration.class);
                startActivity(intent);
                finish();
            }
        });

        btn_continue = findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editEmail.getText().toString().isEmpty()){
                    FancyToast.makeText(ForgotPassword.this,"Please enter email id",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else {
                    Intent intent = new Intent(ForgotPassword.this,EnterOTP.class);
                    intent.putExtra("email",editEmail.getText().toString());
                    startActivity(intent);
                }


            }
        });




    }
}