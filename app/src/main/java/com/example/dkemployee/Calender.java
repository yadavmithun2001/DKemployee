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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.dkemployee.Adapters.HolidayAdapter;
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

public class Calender extends AppCompatActivity {
    ImageView back;
    CalendarView calendarView;
    CardView dialog_close,add;
    Dialog dialog;
    EditText editmonth,editSalary;
    RecyclerView holiday_recycle;
    HolidayAdapter holidayAdapter;
    ArrayList<HolidayModel> list;
    FirebaseAuth auth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        back = findViewById(R.id.imageView3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        calendarView = findViewById(R.id.calendar);


        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        holiday_recycle = findViewById(R.id.recycle_holiday);
        holidayAdapter = new HolidayAdapter(list,Calender.this);
        holiday_recycle.setAdapter(holidayAdapter);
        holiday_recycle.setLayoutManager(new LinearLayoutManager(Calender.this));

        database.getReference()
                .child("holidaydetails")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            HolidayModel holidayModel = dataSnapshot.getValue(HolidayModel.class);
                            list.add(holidayModel);
                        }
                        holidayAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





        dialog = new Dialog(Calender.this);
        dialog.setContentView(R.layout.calender_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
                dialog.show();
                editmonth = dialog.findViewById(R.id.editmonth);
                editmonth.setText(Date);
                editSalary = dialog.findViewById(R.id.editsalary);
                add = dialog.findViewById(R.id.add);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(editmonth.getText().toString().isEmpty()){
                            FancyToast.makeText(Calender.this,"Please enter Date",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                        }else if(editSalary.getText().toString().isEmpty()){
                            FancyToast.makeText(Calender.this,"Please enter Holiday details",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                        }else {
                            list.add(new HolidayModel(editmonth.getText().toString(),editSalary.getText().toString()));
                            HolidayModel holidayModel = new HolidayModel(editmonth.getText().toString(),editSalary.getText().toString());
                            holidayAdapter.notifyDataSetChanged();
                            database.getReference()
                                    .child("holidaydetails")
                                    .child(auth.getUid())
                                    .child(editmonth.getText().toString())
                                    .setValue(holidayModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FancyToast.makeText(Calender.this,"Holiday Details added",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                                        dialog.dismiss();
                                    }else {
                                        FancyToast.makeText(Calender.this,task.getException().getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
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
        });

        dialog_close = dialog.findViewById(R.id.close);
        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });





    }
}