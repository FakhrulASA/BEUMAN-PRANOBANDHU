package com.example.beuman;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class eventstart extends AppCompatActivity {
    ProgressBar pbar;
    private  String user_id;
    private Dialog dd,d2;
    private EditText amount,number,trx,type,quan,note;
    private TextView name,des,date,link,details,organizer;
    private Button send,goods,faq,pop;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ProgressBar pb;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventstart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        amount=(EditText)findViewById(R.id.amount);
        number=(EditText)findViewById(R.id.number);
        trx =(EditText)findViewById(R.id.trx);
        type=(EditText)findViewById(R.id.type);
        quan=(EditText)findViewById(R.id.quantity);
        note=(EditText)findViewById(R.id.note);
        firebaseAuth = FirebaseAuth.getInstance();
        name=(TextView)findViewById(R.id.name);
        pbar =(ProgressBar)findViewById(R.id.pbarx);
        send=(Button)findViewById(R.id.sendM);
        des=(TextView)findViewById(R.id.descriptionz);
        date=(TextView)findViewById(R.id.van);
        details=(TextView)findViewById(R.id.bkash);
        organizer=(TextView)findViewById(R.id.organizer);
        link=(TextView)findViewById(R.id.link);
        dd =new Dialog(this);
        pbar.setVisibility(View.INVISIBLE);
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        user_id=firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("data").document("event").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().exists())
                            {
                                String names = task.getResult().getString("name");
                                String dess = task.getResult().getString("description");
                                String organizations= task.getResult().getString("org");
                                String vans= task.getResult().getString("date");
                                String links= task.getResult().getString("link");
                                String bkashs= task.getResult().getString("transaction");


                                name.setText(names);
                                des.setText("বর্ণনাঃ "+dess);
                                organizer.setText("সংগঠক: "+organizations);
                                date.setText("ইভেন্ট সময়: "+vans);
                                details.setText("লেনদেন বিবরণী: "+bkashs);
                                link.setText("ইভেন্ট লিঙ্ক: "+links);



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
                donations.put("commodities name",type.getText().toString().trim());
                donations.put("quantity",quan.getText().toString().trim());
                donations.put("note",note.getText().toString().trim());
                donations.put("userid",user_id);
                if(number.getText().toString().trim().length()==0||amount.getText().toString().trim().length()==0||trx.getText().toString().trim().length()==0)
                {  pbar.setVisibility(View.INVISIBLE);
                    Toasty.error(eventstart.this, "Fields cannot be empty!", Toast.LENGTH_SHORT, true).show();

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
                                        startActivity(new Intent(eventstart.this, MainActivity.class));
                                        finish();


                                    }
                                });
                            } else {
                                pbar.setVisibility(View.INVISIBLE);
                                Toast.makeText(eventstart.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }
}
