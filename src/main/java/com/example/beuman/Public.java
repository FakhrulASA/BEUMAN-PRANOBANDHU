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

public class Public extends AppCompatActivity {
    ProgressBar pbar;
    private  String user_id;
    private Dialog dd,d2;
    private EditText number,det,amount;
    private TextView name,add,web,bkash,bank,bname;
    private Button send,goods,pop,faq;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);
        pbar =(ProgressBar)findViewById(R.id.postbar);
        pbar.setVisibility(View.INVISIBLE);
        faq=(Button)findViewById(R.id.faq);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        user_id=firebaseAuth.getCurrentUser().getUid();
        name=(TextView)findViewById(R.id.pname);
        add=(TextView)findViewById(R.id.address);
        web=(TextView)findViewById(R.id.web);
        bkash=(TextView)findViewById(R.id.bkash);
        bank=(TextView)findViewById(R.id.bank);
        bname=(TextView)findViewById(R.id.bankname);
        number=(EditText)findViewById(R.id.number);
        det=(EditText)findViewById(R.id.trx);
        amount=(EditText)findViewById(R.id.amount);
        send=(Button)findViewById(R.id.sendz);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d2 =new Dialog(Public.this);
                d2.setContentView(R.layout.pubdonationpop);
                d2.show();
            }
        });
        firebaseFirestore.collection("data").document("pubdonation").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().exists())
                            {
                                String namez = task.getResult().getString("name");
                                String addz = task.getResult().getString("address");
                                String webz = task.getResult().getString("website");
                                String bkashz = task.getResult().getString("bkash");
                                String bankz= task.getResult().getString("bank");
                                String bankn= task.getResult().getString("bankname");
                                name.setText("Project: "+namez);
                                add.setText("Address: "+addz);
                                web.setText("Website: "+webz);
                                bkash.setText("Bkash Number: "+bkashz);
                                bname.setText("Bank Name: "+bankn);
                                bank.setText("Bank Acc: "+bankz);



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
                donations.put("TRX ID",det.getText().toString().trim());

                donations.put("userid",user_id);
                if(number.getText().toString().trim().length()==0||amount.getText().toString().trim().length()==0||det.getText().toString().trim().length()==0)
                {
                    Toasty.error(Public.this, "Fields cannot be empty!", Toast.LENGTH_SHORT, true).show();
                    pbar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    firebaseFirestore.collection("Public Donations").add(donations).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                amount.setText("");
                                number.setText("");
                                det.setText("");

                                dd =new Dialog(Public.this);
                                dd.setContentView(R.layout.pop);
                                dd.show();
                                pbar.setVisibility(View.INVISIBLE);
                                pop = (Button) dd.findViewById(R.id.popback);
                                pop.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Public.this, MainActivity.class));
                                        finish();

                                    }
                                });
                            } else {
                                Toast.makeText(Public.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                pbar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });

    }
}
