package com.example.dkemployee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dkemployee.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Profile extends AppCompatActivity {
    ImageView back,pfimage;
    CardView edit_profile,logout;
    TextView pfname,pfemail,pfmobile,pfempoyee;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    UserModel userModel;
    FloatingActionButton update_image;
    Uri selectedimage;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        back = findViewById(R.id.imageView3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        pfname = findViewById(R.id.pfname);
        pfemail = findViewById(R.id.pfemail);
        pfmobile = findViewById(R.id.pfmobile);
        pfempoyee = findViewById(R.id.pfemployee);
        pfempoyee.setText(auth.getUid());

        pfimage = findViewById(R.id.pfimage);
        update_image = findViewById(R.id.updatepfimage);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading Profile Picture");
        dialog.setMessage("Please Wait");

        database.getReference().
                child("employeedata")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModel = snapshot.getValue(UserModel.class);
                        pfname.setText(userModel.getName()+"");
                        pfemail.setText(userModel.getEmail()+"");
                        pfmobile.setText("+44 "+userModel.getMobile());
                        Glide.with(getApplicationContext())
                                .load(userModel.getPf_url())
                                .placeholder(R.drawable.img)
                                .into(pfimage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        FancyToast.makeText(Profile.this,"Something went wrong",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                    }
                });



        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                FancyToast.makeText(Profile.this,"User logged out successfully",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                Intent intent = new Intent(Profile.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
        edit_profile = findViewById(R.id.edit_profile);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,EditProfile.class);
                startActivity(intent);
                finish();
            }
        });
        update_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,45);
            }
        }
        );

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(data.getData() != null){
                pfimage.setImageURI(data.getData());
                selectedimage = data.getData();
                dialog.show();
                StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());
                reference.putFile(selectedimage)
                        .addOnCompleteListener(Profile.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            userModel.setPf_url(uri.toString());
                                            database.getReference()
                                                    .child("employeedata")
                                                    .child(auth.getUid())
                                                    .setValue(userModel);
                                            FancyToast.makeText(Profile.this,"Profile Picture uploaded ",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                                            dialog.dismiss();
                                        }
                                    });
                                }else {
                                    FancyToast.makeText(Profile.this,task.getException().getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                                    dialog.dismiss();
                                }
                            }
                        });

            }
        }
    }
}