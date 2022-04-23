package com.example.dkemployee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dkemployee.Adapters.SalaryAdapter;
import com.example.dkemployee.Models.SalaryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Salarydetails extends AppCompatActivity {
    ImageView back;
    CardView add_salary,dialog_close,add;
    Dialog dialog;
    EditText editmonth,editSalary,edittaxocde;
    RecyclerView salary_recycle;
    SalaryAdapter salaryAdapter;
    ArrayList<SalaryModel> list;
    FirebaseAuth auth;
    SalaryModel salaryModel;
    FirebaseDatabase database;
    ProgressBar progressBar;
    TextView nodata;
    CalendarView calendarView;
    private DatePickerDialog datePickerDialog;
    TextView selectmonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salarydetails);
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
        salary_recycle = findViewById(R.id.salary_recyle);
        salaryAdapter = new SalaryAdapter(list,Salarydetails.this);
        salary_recycle.setAdapter(salaryAdapter);
        salary_recycle.setLayoutManager(new LinearLayoutManager(Salarydetails.this));
        progressBar = findViewById(R.id.progressBar3);
        nodata = findViewById(R.id.nodata);

        database.getReference()
                .child("salarydetails")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            salaryModel = dataSnapshot.getValue(SalaryModel.class);
                            list.add(salaryModel);
                        }
                        salaryAdapter.notifyDataSetChanged();
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





        dialog = new Dialog(Salarydetails.this);
        dialog.setContentView(R.layout.salary_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        selectmonth = dialog.findViewById(R.id.textView6);
        calendarView = dialog.findViewById(R.id.calendar);

        selectmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.setVisibility(View.VISIBLE);
            }
        });

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

        calendarView = dialog.findViewById(R.id.calendar);
        editmonth = dialog.findViewById(R.id.editmonth);
        edittaxocde = dialog.findViewById(R.id.edittaxcode);

        selectmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _showdialog();
            }
        });
        editSalary = dialog.findViewById(R.id.editsalary);

        DatePickerDialog dialog2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dialog2 = new DatePickerDialog(Salarydetails.this);
        }
        add = dialog.findViewById(R.id.add);
        DatePickerDialog finalDialog = dialog2;
        finalDialog.setContentView(R.layout.calender_dialog);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectmonth.getText().equals("Select Month")){
                    FancyToast.makeText(Salarydetails.this,"Please select a month",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(editSalary.getText().toString().isEmpty()){
                    FancyToast.makeText(Salarydetails.this,"Please enter amount",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(edittaxocde.getText().toString().isEmpty()){
                    FancyToast.makeText(Salarydetails.this,"Please enter tax code",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }

                else {
                    SalaryModel salaryModel = new SalaryModel(selectmonth.getText().toString(),editSalary.getText().toString(),edittaxocde.getText().toString());
                    salaryAdapter.notifyDataSetChanged();
                   database.getReference()
                            .child("salarydetails")
                            .child(auth.getUid())
                            .child(selectmonth.getText().toString())
                            .setValue(salaryModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FancyToast.makeText(Salarydetails.this,"Salary Details added",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                            }else {
                                FancyToast.makeText(Salarydetails.this,task.getException().getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                            }
                        }
                    });
                    editSalary.setText("");
                    edittaxocde.setText("");
                    editSalary.requestFocus();
                    list.clear();
                }
            }
        });




    }
    void _showdialog(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(month, year);
                selectmonth.setText(date);
                selectmonth.setVisibility(View.VISIBLE);
            }

        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.show();
    }
    private String makeDateString(int month, int year)
    {
        return getMonthFormat(month) + " - "  + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JANUARY";
        if(month == 2)
            return "FEBRUARY";
        if(month == 3)
            return "MARCH";
        if(month == 4)
            return "APRIL";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUNE";
        if(month == 7)
            return "JULY";
        if(month == 8)
            return "AUGUST";
        if(month == 9)
            return "SEPTEMBER";
        if(month == 10)
            return "OCTOBER";
        if(month == 11)
            return "NOVEMBER";
        if(month == 12)
            return "DECEMBER";

        //default should never happen
        return "JANUARY";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}