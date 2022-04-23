package com.example.dkemployee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

public class EnterOTP extends AppCompatActivity {

    EditText et1,et2,et3,et4;
    TextView email_text;
    CardView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        otpnext();
        email_text = findViewById(R.id.forgotpassword);
        email_text.setText(getIntent().getStringExtra("email"));
        next = findViewById(R.id.continue_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FancyToast.makeText(EnterOTP.this,"Enterd OTP is invalid",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
            }
        });


    }
    public void otpnext(){
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et1.getText().toString().isEmpty()){
                    et2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!et1.getText().toString().isEmpty()){
                    et2.requestFocus();
                }
            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et2.getText().toString().isEmpty()){
                    et3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!et2.getText().toString().isEmpty()){
                    et3.requestFocus();
                }else {
                    et1.requestFocus();
                }
            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et3.getText().toString().isEmpty()){
                    et4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!et3.getText().toString().isEmpty()){
                    et4.requestFocus();
                }else {
                    et2.requestFocus();
                }
            }
        });
        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!et4.getText().toString().isEmpty()){
                }else {
                    et3.requestFocus();
                }
            }
        });
    }
}