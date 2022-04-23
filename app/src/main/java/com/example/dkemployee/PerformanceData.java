package com.example.dkemployee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dkemployee.Adapters.PerformanceAdapter;
import com.example.dkemployee.Models.CompanyModel;
import com.example.dkemployee.Models.HolidayModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

public class PerformanceData extends AppCompatActivity {
    ImageView back;
    CardView add_salary,dialog_close,add;
    Dialog dialog;
    EditText editmonth,editSalary;
    RecyclerView company_recycle;
    PerformanceAdapter performanceAdapter;
    ArrayList<HolidayModel> list;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressBar progressBar;
    TextView nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_data);
        back = findViewById(R.id.imageView3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        list = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        company_recycle = findViewById(R.id.performance_recyle);
        performanceAdapter = new PerformanceAdapter(list,PerformanceData.this);
        company_recycle.setAdapter(performanceAdapter);
        company_recycle.setLayoutManager(new LinearLayoutManager(PerformanceData.this));
        progressBar = findViewById(R.id.progressBar3);
        nodata = findViewById(R.id.nodata);

        database.getReference()
                .child("performance")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            HolidayModel holidayModel = dataSnapshot.getValue(HolidayModel.class);
                            list.add(holidayModel);
                        }
                        performanceAdapter.notifyDataSetChanged();
                        nodata.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        if(list.size() == 0){
                            nodata.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





        dialog = new Dialog(PerformanceData.this);
        dialog.setContentView(R.layout.performance_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        add_salary = findViewById(R.id.card_add_salary);
        add_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        dialog_close = dialog.findViewById(R.id.close);
        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        editmonth = dialog.findViewById(R.id.editmonth);
        editSalary = dialog.findViewById(R.id.editsalary);
        add = dialog.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editmonth.getText().toString().isEmpty()){
                    FancyToast.makeText(PerformanceData.this,"Please enter Performance topis",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(editSalary.getText().toString().isEmpty()){
                    FancyToast.makeText(PerformanceData.this,"Please enter Data",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else {

                    HolidayModel holidayModel = new HolidayModel(editmonth.getText().toString(),editSalary.getText().toString());
                    performanceAdapter.notifyDataSetChanged();
                    database.getReference()
                            .child("performance")
                            .child(auth.getUid())
                            .child(editmonth.getText().toString())
                            .setValue(holidayModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FancyToast.makeText(PerformanceData.this,"Company Details added",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                            }else {
                                FancyToast.makeText(PerformanceData.this,task.getException().getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                            }
                        }
                    });
                    editmonth.setText("");
                    editSalary.setText("");
                    editmonth.requestFocus();
                    list.clear();

                }
            }
        });

    }
}