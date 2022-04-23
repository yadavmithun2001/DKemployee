package com.example.dkemployee.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dkemployee.Models.HolidayModel;
import com.example.dkemployee.Models.SalaryModel;
import com.example.dkemployee.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.viewholder> {

    ArrayList<HolidayModel> list = new ArrayList<>();
    Context context;

    public HolidayAdapter(ArrayList<HolidayModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolidayAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.samplee_holiday_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayAdapter.viewholder holder, @SuppressLint("RecyclerView") int position) {
        HolidayModel holidayModel = list.get(position);
        holder.month.setText(holidayModel.getDate());
        holder.salary.setText(holidayModel.getHoliday());

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.calender_dialog_edit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        EditText editmonth = dialog.findViewById(R.id.editmonth);
        EditText editsalary = dialog.findViewById(R.id.editsalary);
        CardView save = dialog.findViewById(R.id.add);
        CardView close = dialog.findViewById(R.id.close);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editmonth.setText(holidayModel.getDate());
                editsalary.setText(holidayModel.getHoliday());
                dialog.show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holidayModel.setDate(editmonth.getText().toString());
                        holidayModel.setHoliday(editsalary.getText().toString());
                        FirebaseDatabase.getInstance().getReference()
                                .child("holidaydetails")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(editmonth.getText().toString())
                                .setValue(holidayModel);
                        list.clear();
                        dialog.dismiss();
                    }
                });
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                list.remove(position);
                FirebaseDatabase.getInstance().getReference()
                        .child("holidaydetails")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(holidayModel.getDate())
                        .setValue(null);
                FancyToast.makeText(context,"Calender details removed",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                list.clear();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView month,salary;
        CardView edit,delete;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            month = itemView.findViewById(R.id.month);
            salary = itemView.findViewById(R.id.salary);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }

}
