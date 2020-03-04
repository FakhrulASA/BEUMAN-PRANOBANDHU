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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class event extends AppCompatActivity {
    ProgressBar pbar;
    private  String user_id;
    private Dialog dd,d2;
    private EditText amount,number,trx;
    private TextView aname , pass, address,email,adescription;
    private Button send,goods,faq,pop;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ProgressBar pb;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        aname=(TextView)findViewById(R.id.ename);
        adescription=(TextView)findViewById(R.id.des);
        goods=(Button)findViewById(R.id.goods);
        pb=(ProgressBar)findViewById(R.id.pbar);
        faq=(Button)findViewById(R.id.faq);
        pbar =(ProgressBar)findViewById(R.id.pbar);
        amount=(EditText)findViewById(R.id.amount);
        number=(EditText)findViewById(R.id.number);
        trx =(EditText)findViewById(R.id.trx);
        send=(Button)findViewById(R.id.sendM);
        firebaseAuth = FirebaseAuth.getInstance();
        dd =new Dialog(this);
        pbar =(ProgressBar)findViewById(R.id.pbar);
        pbar.setVisibility(View.INVISIBLE);
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        user_id=firebaseAuth.getCurrentUser().getUid();
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d2 =new Dialog(event.this);
                d2.setContentView(R.layout.eventpop);
                d2.show();
            }
        });
        goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(event.this,goods.class));
            }
        });
        firebaseFirestore.collection("data").document("event").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().exists())
                            {
                                String name = task.getResult().getString("name");
                                String des = task.getResult().getString("description");


                                aname.setText(name);
                                adescription.setText(des);



                            }
                        }
                        else
                        {

                        }
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
                {  pbar.setVisibility(View.INVISIBLE);
                    Toasty.error(event.this, "Fields cannot be empty!", Toast.LENGTH_SHORT, true).show();

                }
                else
                {
                    firebaseFirestore.collection("Event Donation").add(donations).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                amount.setText("");
                                number.setText("");
                                trx.setText("");
                                pop = (Button) dd.findViewById(R.id.popback);
                                dd.setContentView(R.layout.pop);
                                pbar.setVisibility(View.INVISIBLE);
                                dd.show();

                                pop = (Button) dd.findViewById(R.id.popback);
                                pop.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pbar.setVisibility(View.INVISIBLE);
                                        startActivity(new Intent(event.this, MainActivity.class));
                                        finish();


                                    }
                                });
                            } else {
                                pbar.setVisibility(View.INVISIBLE);
                                Toast.makeText(event.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

    }
}
