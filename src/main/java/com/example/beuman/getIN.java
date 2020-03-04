package com.example.beuman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class getIN extends AppCompatActivity {
private Button reset;
private EditText email;
private  FirebaseAuth firebaseAuth;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_in);
        reset=(Button)findViewById(R.id.register);
        email=(EditText)findViewById(R.id.email);
        firebaseAuth = FirebaseAuth.getInstance();
        pb=(ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                String mail=email.getText().toString();
                if(TextUtils.isEmpty(mail))
                {
                    pb.setVisibility(View.INVISIBLE);
                    Toast.makeText(getIN.this,"This field cannot be empty.Enter your email address.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                   firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                              if(task.isSuccessful())
                              {
                                  pb.setVisibility(View.INVISIBLE);
                                  Toast.makeText(getIN.this,"Reset request have been sent to your email",Toast.LENGTH_SHORT).show();
                                  startActivity(new Intent(getIN.this,signIN.class));
                              }
                              else{
                                  String error=task.getException().getMessage();
                                  pb.setVisibility(View.INVISIBLE);
                                  Toast.makeText(getIN.this,error,Toast.LENGTH_SHORT).show();
                              }
                       }
                   });
                }
            }
        });
    }

}
