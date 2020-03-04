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

public class goods extends AppCompatActivity {

    private  String user_id;
    ProgressBar pbar;
    private Dialog dd,d2;
    private EditText type,quan,note;
    private Button send,pop;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        pbar=(ProgressBar)findViewById(R.id.postbar);
        pbar.setVisibility(View.INVISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        user_id=firebaseAuth.getCurrentUser().getUid();
        type=(EditText)findViewById(R.id.type);
        quan=(EditText)findViewById(R.id.quantity);
        note=(EditText)findViewById(R.id.note);
        send=(Button)findViewById(R.id.send);
        pbar=(ProgressBar)findViewById(R.id.postbar);
        pbar.setVisibility(View.INVISIBLE);
        dd =new Dialog(this);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                final Map<String,String> donations= new HashMap<>();
                donations.put("type",type.getText().toString().trim());
                donations.put("quantity",quan.getText().toString().trim());
                donations.put("amount",note.getText().toString().trim());

                donations.put("userid",user_id);
                if(type.getText().toString().trim().length()==0||quan.getText().toString().trim().length()==0||note.getText().toString().trim().length()==0)
                {
                    Toasty.error(goods.this, "Fields cannot be empty!", Toast.LENGTH_SHORT, true).show();
                    pbar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    firebaseFirestore.collection("Event Money Donation").add(donations).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                type.setText("");
                                quan.setText("");
                                note.setText("");

                                dd.setContentView(R.layout.pop);
                                dd.show();
                                pbar.setVisibility(View.INVISIBLE);
                                pop = (Button) dd.findViewById(R.id.popback);
                                pop.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(goods.this, MainActivity.class));
                                        finish();

                                    }
                                });
                            } else {
                                Toast.makeText(goods.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                pbar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }
}
