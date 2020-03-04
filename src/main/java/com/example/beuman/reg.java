package com.example.beuman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class reg extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private EditText email,pass,cpass;
    private Button reg,sin;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        email=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.pass);
        cpass=(EditText)findViewById(R.id.conpass);
        reg=(Button)findViewById(R.id.register);
        sin=(Button)findViewById(R.id.sin);
        firebaseAuth = FirebaseAuth.getInstance();
        pb=(ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                if(email.length()==0 || pass.length()==0)
                {
                    pb.setVisibility(View.INVISIBLE);

                    Toasty.error(reg.this, "Email/Password field cannot be empty", Toast.LENGTH_SHORT, true).show();
                }
                else if(!pass.getText().toString().equals(cpass.getText().toString()))

                {

                    pb.setVisibility(View.INVISIBLE);
                    Toasty.error(reg.this, "Password not matched!", Toast.LENGTH_SHORT, true).show();



                }
                else
                {
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {


                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            pb.setVisibility(View.INVISIBLE);
                                            Toasty.info(reg.this, "Registered Successfully! Please verify you email address for login", Toast.LENGTH_SHORT, true).show();
                                            Intent intent = new Intent(reg.this,signIN.class);

                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                            startActivity(intent);
                                            finish();


                                        }
                                        else
                                        {

                                            pb.setVisibility(View.INVISIBLE);
                                            Toasty.warning(reg.this, task.getException().getMessage(), Toast.LENGTH_SHORT, true).show();

                                        }


                                    }
                                });


                            }
                            else
                            {
                                pb.setVisibility(View.INVISIBLE);
                                Toasty.warning(reg.this, task.getException().getMessage(), Toast.LENGTH_SHORT, true).show();

                            }
                        }
                    });
                }

            }
        });
        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(reg.this,signIN.class));
            }
        });
    }
}
