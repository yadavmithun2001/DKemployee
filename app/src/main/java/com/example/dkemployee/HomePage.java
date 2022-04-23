package com.example.dkemployee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dkemployee.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class HomePage extends AppCompatActivity {

    CardView card_calender,card_salary,card_performance,card_training,card_company,card_message,card_profile;
    TextView welcomename;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    UserModel userModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        welcomename = findViewById(R.id.welcomename);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference()
                .child("employeedata")
                .child(Objects.requireNonNull(auth.getUid()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModel = snapshot.getValue(UserModel.class);
                        welcomename.setText("Welcome "+userModel.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        card_calender = findViewById(R.id.card_calender);
        card_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,Calender.class);
                startActivity(intent);
            }
        });

        card_salary = findViewById(R.id.card_salarydetails);
        card_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,Salarydetails.class);
                startActivity(intent);
            }
        });

        card_performance = findViewById(R.id.card_performancedata);
        card_performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,PerformanceData.class);
                startActivity(intent);
            }
        });

        card_training = findViewById(R.id.card_training);
        card_training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,TrainingStatus.class);
                startActivity(intent);
            }
        });

        card_company = findViewById(R.id.card_company);
        card_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,CompanyDetails.class);
                startActivity(intent);
            }
        });

        card_message = findViewById(R.id.card_message);
        card_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,Messages.class);
                startActivity(intent);
            }
        });

        card_profile = findViewById(R.id.card_profile);
        card_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,Profile.class);
                startActivity(intent);
            }
        });

    }
}