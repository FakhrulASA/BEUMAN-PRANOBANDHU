package com.example.beuman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class donattion extends AppCompatActivity {
    private  String user_id;
    ProgressBar pbar;
    private Dialog dd,d2;
    private EditText amount,number,trx;
    private Button save,add,send,pop,faq;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ProgressBar p;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donattion);
        p =(ProgressBar)findViewById(R.id.pbar);
        amount=(EditText)findViewById(R.id.amount);
        number=(EditText)findViewById(R.id.number);
        trx =(EditText)findViewById(R.id.trx);
        send=(Button)findViewById(R.id.sendM);
        faq=(Button)findViewById(R.id.faq);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        user_id=firebaseAuth.getCurrentUser().getUid();
        pbar =(ProgressBar)findViewById(R.id.pbar);
        pbar.setVisibility(View.INVISIBLE);
        dd =new Dialog(this);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d2 =new Dialog(donattion.this);
                d2.setContentView(R.layout.donationpop);
                d2.show();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                final Map<String,String> donations= new HashMap<>();
                donations.put("number",number.getText().toString().trim());
                donations.put("amount",amount.getText().toString().trim());
                donations.put("TRX ID",trx.getText().toString().trim());

                donations.put("userid",user_id);
                if(number.getText().toString().trim().length()==0||amount.getText().toString().trim().length()==0||trx.getText().toString().trim().length()==0)
                {

                    Toasty.error(donattion.this, "Fields cannot be empty!", Toast.LENGTH_SHORT, true).show();
                    pbar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    firebaseFirestore.collection("Donations").add(donations).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                amount.setText("");
                                number.setText("");
                                trx.setText("");

                                dd.setContentView(R.layout.pop);
                                dd.show();
                                pbar.setVisibility(View.INVISIBLE);
                                pop = (Button) dd.findViewById(R.id.popback);
                                pop.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(donattion.this, MainActivity.class));
                                        finish();

                                    }
                                });
                            } else {
                                Toast.makeText(donattion.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                pbar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }
}
