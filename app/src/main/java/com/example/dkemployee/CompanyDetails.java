package com.example.dkemployee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dkemployee.Models.CompanyModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

public class CompanyDetails extends AppCompatActivity {
    ImageView back;
    CardView add_salary,dialog_close,add;
    Dialog dialog;
    EditText editmonth,editSalary,editEmail,editLandline;
    CompanyModel companyModel;
    FirebaseAuth auth;
    FirebaseDatabase database;
    CardView edit_company;
    TextView company_name,company_address,company_email,company_Landline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
        back = findViewById(R.id.imageView3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        company_name = findViewById(R.id.txt_company);
        company_address = findViewById(R.id.txt_address);
        company_email = findViewById(R.id.txt_email);
        company_Landline = findViewById(R.id.txt_landline);


        database.getReference()
                .child("companydetails")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        CompanyModel companyModel = snapshot.getValue(CompanyModel.class);
                        company_name.setText(companyModel.getName()+"");
                        company_address.setText(companyModel.getAddress()+"");
                        company_email.setText(companyModel.getEmail()+"");
                        company_Landline.setText(companyModel.getLandlinenumber()+"");
                        if(company_name.getText().equals("null")){
                            company_name.setText("No data found");
                            company_address.setText("No data found");
                            company_email.setText("No data found");
                            company_Landline.setText("No data found");
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





        dialog = new Dialog(CompanyDetails.this);
        dialog.setContentView(R.layout.company_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        editmonth = dialog.findViewById(R.id.editmonth);
        editSalary = dialog.findViewById(R.id.editsalary);
        editEmail = dialog.findViewById(R.id.edit_email);
        editLandline = dialog.findViewById(R.id.editLandline);
        add = dialog.findViewById(R.id.add);

        add_salary = findViewById(R.id.edit_company);
        add_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                editmonth.setText(company_name.getText().toString());
                editSalary.setText(company_address.getText().toString());
                editEmail.setText(company_email.getText().toString());
                editLandline.setText(company_Landline.getText().toString());
            }
        });
        dialog_close = dialog.findViewById(R.id.close);
        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editmonth.getText().toString().isEmpty()){
                    FancyToast.makeText(CompanyDetails.this,"Please enter topic",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(editSalary.getText().toString().isEmpty()){
                    FancyToast.makeText(CompanyDetails.this,"Please enter Data",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else {

                    CompanyModel companyModel = new CompanyModel(editmonth.getText().toString(),editSalary.getText().toString(),editEmail.getText().toString(),editLandline.getText().toString());
                    database.getReference()
                            .child("companydetails")
                            .child(auth.getUid())
                            .setValue(companyModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FancyToast.makeText(CompanyDetails.this,"Company Details Updated",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                                dialog.dismiss();
                            }else {
                                FancyToast.makeText(CompanyDetails.this,task.getException().getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                            }
                        }
                    });
                }
            }
        });




    }




}