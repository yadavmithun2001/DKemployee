package com.example.dkemployee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dkemployee.Adapters.SalaryAdapter;
import com.example.dkemployee.Adapters.TrainingAdpater;
import com.example.dkemployee.Models.SalaryModel;
import com.example.dkemployee.Models.TrainingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Calendar;

public class TrainingStatus extends AppCompatActivity {
    ImageView back;
    CardView add_salary,dialog_close,add;
    Dialog dialog;
    EditText editmonth,editSalary;
    RecyclerView salary_recycle;
    TrainingAdpater trainingAdpater;
    ArrayList<TrainingModel> list;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressBar progressBar;
    TextView nodata,selectdate;
    private DatePickerDialog datePickerDialog;
    Switch marksas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_status);
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
        trainingAdpater = new TrainingAdpater(list,TrainingStatus.this);
        salary_recycle.setAdapter(trainingAdpater);
        salary_recycle.setLayoutManager(new LinearLayoutManager(TrainingStatus.this));
        progressBar = findViewById(R.id.progressBar3);
        nodata = findViewById(R.id.nodata);

        database.getReference()
                .child("trainingdetails")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            TrainingModel trainingModel = dataSnapshot.getValue(TrainingModel.class);
                            list.add(trainingModel);
                        }
                        trainingAdpater.notifyDataSetChanged();
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

        dialog = new Dialog(TrainingStatus.this);
        dialog.setContentView(R.layout.training_dialog);
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
        selectdate = dialog.findViewById(R.id.selectdate);
        marksas = dialog.findViewById(R.id.switch1);
        add = dialog.findViewById(R.id.add);

        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _showdialog();
            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editmonth.getText().toString().isEmpty()){
                    FancyToast.makeText(TrainingStatus.this,"Please enter Training Name",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else if(selectdate.getText().equals("Select Date")){
                    FancyToast.makeText(TrainingStatus.this,"Please select Training date",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }else {
                    String status = "";
                    if(marksas.isChecked()){
                        status = "done";
                    }else {
                        status = "notdone";
                    }
                    list.add(new TrainingModel(editmonth.getText().toString(),editSalary.getText().toString(),status));
                    TrainingModel trainingModel = new TrainingModel(editmonth.getText().toString(),selectdate.getText().toString(),status);
                    trainingAdpater.notifyDataSetChanged();
                    database.getReference()
                            .child("trainingdetails")
                            .child(auth.getUid())
                            .child(selectdate.getText().toString())
                            .setValue(trainingModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FancyToast.makeText(TrainingStatus.this,"Training Details added",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                            }else {
                                FancyToast.makeText(TrainingStatus.this,task.getException().getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
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
    void _showdialog(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(month,day, year);
                selectdate.setText(date);
                selectdate.setVisibility(View.VISIBLE);
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
    private String makeDateString(int month,int day, int year)
    {
        return getMonthFormat(month) + " - " + day + " - "+ year;
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
}