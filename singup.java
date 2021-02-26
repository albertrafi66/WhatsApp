package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.Models.user;
import com.example.whatsapp.databinding.ActivitySingupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class singup extends AppCompatActivity {
    ActivitySingupBinding binding; /* To enable buildBinding Features insteade of findViewById insert
    buildFeatures{viewBinding true;} in buid.gradle(Module) */
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    ProgressDialog pd; // User Complete hoya porjonto ei dialogbox show korbe
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingupBinding.inflate(getLayoutInflater()); // Ata insert korte hobe for binding Features
        setContentView(binding.getRoot()); // Atau change korte hobe for binding Features
        getSupportActionBar().hide();
        auth= FirebaseAuth.getInstance();  // For Signing proccess using email and pass world
        database = FirebaseDatabase.getInstance(); // For save user given data in real time database
        pd = new ProgressDialog(singup.this);
        pd.setTitle("Creating Account");
        pd.setMessage("All data is proccessing....");
        binding.sig.setOnClickListener(new View.OnClickListener() // Power of data binding
        {
            @Override
            public void onClick(View v) {
                //For empty edittext
                if (binding.user.getText().toString().isEmpty()){
                    binding.user.setError("Enter your name");
                    return;
                }
                if (binding.email.getText().toString().isEmpty()){
                    binding.email.setError("Enter email");
                    return;
                }
                if (binding.pass.getText().toString().isEmpty()){
                    binding.pass.setError("Enter password");
                    return;
                }




                pd.show();
                    // Pass two parameter Email and Password to create user
                    // Right Click on com.example.whatsapp to create package. Then create Java class in new package. This class is use for users Information. (addOnCompleteListener) listener use korar jonno ei java class er dorkar
                    //Enable email in Firebase -> Authentication -> Sign in method
                    auth.createUserWithEmailAndPassword(binding.email.getText().toString(),binding.pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) { // task e authentication er sob result thake(Firebase authenticatin e ja ja hoy)



                            pd.dismiss();
                            if(task.isSuccessful()){
                                // Use user class for pass data **import java class**
                                user u1=new user(binding.user.getText().toString(),binding.email.getText().toString(),binding.pass.getText().toString());
                                // Collect individual user id. Use this Id to collect user info in different Nodes
                                String id=task.getResult().getUser().getUid();
                                // Main node er under e User name er child create hobe er Under e Id er chile create hobe then Id er under e Value store hobe
                                database.getReference().child("User").child(id).setValue(u1);

                                Toast.makeText(singup.this,"User created succesfully",Toast.LENGTH_SHORT).show();


                            }
                            else{
                                Toast.makeText(singup.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show(); //task e exception o thake
                            }
                        }
                    });// (addOnCompleteListener) Ei porjonto code korle Sign Up kora jabe abong Firebase a data record o hoye jabe.
                    //Now save user info (Username,Email,Password) in real time data base (Firebase)


                }
            



        });
        binding.alreadyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5=new Intent(singup.this,Signin.class);
                startActivity(i5);
            }
        });


    }
}