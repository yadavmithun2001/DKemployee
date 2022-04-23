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

import com.example.dkemployee.Models.SalaryModel;
import com.example.dkemployee.R;
import com.example.dkemployee.Salarydetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

public class SalaryAdapter extends RecyclerView.Adapter<SalaryAdapter.viewholder> {
    ArrayList<SalaryModel> list = new ArrayList<>();
    Context context;

    public SalaryAdapter(ArrayList<SalaryModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.samplee_salary_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, @SuppressLint("RecyclerView") int position) {
       SalaryModel salaryModel = list.get(position);
       holder.month.setText(salaryModel.getMonth());
       holder.salary.setText("Salary: "+salaryModel.getAmount());
       holder.taxcode.setText("TaxCode: "+salaryModel.getTaxcode());

       Dialog dialog = new Dialog(context);
       dialog.setContentView(R.layout.salary_dialog_edit);
       dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
       EditText editmonth = dialog.findViewById(R.id.editmonth);
       EditText editsalary = dialog.findViewById(R.id.editsalary);
       CardView save = dialog.findViewById(R.id.add);
        CardView close = dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

       holder.edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               editmonth.setText(salaryModel.getMonth());
               editsalary.setText(salaryModel.getAmount());
               dialog.show();
               save.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       salaryModel.setMonth(editmonth.getText().toString());
                       salaryModel.setAmount(editsalary.getText().toString());
                       FirebaseDatabase.getInstance().getReference()
                               .child("salarydetails")
                               .child(FirebaseAuth.getInstance().getUid())
                               .child(editmonth.getText().toString())
                               .setValue(salaryModel);
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
                       .child("salarydetails")
                       .child(FirebaseAuth.getInstance().getUid())
                       .child(salaryModel.getMonth())
                       .setValue(null);
               FancyToast.makeText(context,"Salary removed",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
               list.clear();
           }
       });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView month,salary,taxcode;
        CardView edit,delete;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            month = itemView.findViewById(R.id.month);
            salary = itemView.findViewById(R.id.salary);
            taxcode = itemView.findViewById(R.id.taxcode);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
