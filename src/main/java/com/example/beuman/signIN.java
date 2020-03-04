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

public class signIN extends AppCompatActivity {
   private Button signIN,reg,forgot;
   private EditText email,pass;
   private ProgressBar pb;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firebaseAuth =FirebaseAuth.getInstance();
        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);
        signIN =(Button)findViewById(R.id.signin);
        reg =(Button)findViewById(R.id.reg);
        forgot =(Button)findViewById(R.id.forgot);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        signIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                if(email.length()==0 || pass.length()==0)
                {   pb.setVisibility(View.INVISIBLE);
                    Toasty.error(signIN.this,"Email/Password cannot be empty!", Toast.LENGTH_SHORT, true).show();

                }
                else
                {


                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(),pass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {

                                        if(firebaseAuth.getCurrentUser().isEmailVerified())
                                        {
                                           Intent intent = new Intent(signIN.this,MainActivity.class);
                                            pb.setVisibility(View.INVISIBLE);

                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();


                                        }
                                        else
                                        {


                                            Toasty.info(signIN.this,"Verify your email address first", Toast.LENGTH_SHORT, true).show();
                                            pb.setVisibility(View.INVISIBLE);
                                        }

                                    }
                                    else
                                    {
                                        pb.setVisibility(View.INVISIBLE);
                                        Toasty.warning(signIN.this, task.getException().getMessage(), Toast.LENGTH_SHORT, true).show();

                                    }
                                }
                            });
                }

            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signIN.this,reg.class));
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signIN.this,getIN.class));
            }
        });
    }
}
