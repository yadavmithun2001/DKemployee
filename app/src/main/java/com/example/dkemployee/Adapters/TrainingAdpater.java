package com.example.dkemployee.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dkemployee.Models.TrainingModel;
import com.example.dkemployee.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

public class TrainingAdpater extends RecyclerView.Adapter<TrainingAdpater.viewholder> {
    ArrayList<TrainingModel> list = new ArrayList<>();
    Context context;

    public TrainingAdpater(ArrayList<TrainingModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.samplee_training_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, @SuppressLint("RecyclerView") int position) {
        TrainingModel trainingModel = list.get(position);
        holder.month.setText(trainingModel.getTraining_name());
        holder.salary.setText(trainingModel.getTraining_date());

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.training_dialog_edit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        EditText editmonth = dialog.findViewById(R.id.editmonth);
        EditText editsalary = dialog.findViewById(R.id.editsalary);
        CardView save = dialog.findViewById(R.id.add);
        Switch markas = dialog.findViewById(R.id.switch1);
        CardView close = dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if(trainingModel.getStatus().equals("done")){
            holder.tick.setVisibility(View.VISIBLE);
        }else {
            holder.close.setVisibility(View.VISIBLE);
        }

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editmonth.setText(trainingModel.getTraining_name());
                editsalary.setText(trainingModel.getTraining_date());
                if(trainingModel.getStatus().equals("done")){
                    markas.setChecked(true);
                }else {
                    markas.setChecked(false);
                }
                dialog.show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        trainingModel.setTraining_name(editmonth.getText().toString());
                        trainingModel.setTraining_date(editsalary.getText().toString());
                        if(markas.isChecked()){
                            trainingModel.setStatus("done");
                            holder.close.setVisibility(View.GONE);
                            holder.tick.setVisibility(View.VISIBLE);
                        }else {
                            trainingModel.setStatus("notdone");
                            holder.tick.setVisibility(View.GONE);
                            holder.close.setVisibility(View.VISIBLE);
                        }
                        FirebaseDatabase.getInstance().getReference()
                                .child("trainingdetails")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(editmonth.getText().toString())
                                .setValue(trainingModel);
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
                        .child("trainingdetails")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(trainingModel.getTraining_name())
                        .setValue(null);
                FancyToast.makeText(context,"Training removed",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
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
        CardView edit,delete,tick,close;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            month = itemView.findViewById(R.id.month);
            salary = itemView.findViewById(R.id.salary);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            tick = itemView.findViewById(R.id.tick);
            close = itemView.findViewById(R.id.close);
        }
    }
}
