package com.example.beuman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class mainprofile extends AppCompatActivity {
    private CircleImageView ppic;
    private Uri ur = null;
    private TextView name, pass,email,d,p,h;
    private Button save;
    private EditText address;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainprofile);
        ppic = (CircleImageView) findViewById(R.id.propic);
        ppic.setEnabled(false);
        ppic.setFocusable(false);
        name = (TextView) findViewById(R.id.username);
        d = (TextView) findViewById(R.id.don);
        p = (TextView) findViewById(R.id.pos);
        h = (TextView) findViewById(R.id.hel);
        name.setEnabled(false);
        name.setFocusable(false);
        pass = (TextView) findViewById(R.id.number);
        pass.setEnabled(false);
        pass.setFocusable(false);
        email = (TextView) findViewById(R.id.email);
        email.setFocusable(false);
        email.setEnabled(false);
        address = (EditText) findViewById(R.id.address);
        address.setFocusable(true);
        address.setEnabled(true);
        save = (Button) findViewById(R.id.save);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        user_id = firebaseAuth.getCurrentUser().getUid();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainprofile.this,editprofile.class));
            }
        });
        firebaseFirestore.collection("Users").document(user_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String username = task.getResult().getString("username");
                                String contact = task.getResult().getString("contact");
                                String addressX = task.getResult().getString("address");
                                String ema = task.getResult().getString("email");
                                String image = task.getResult().getString("image");
                                String don = task.getResult().getString("donation");
                                String ps = task.getResult().getString("post");
                                String hlp = task.getResult().getString("help");
                                ur = Uri.parse(image);
                                name.setText(username);
                                pass.setText(contact);
                                address.setText(addressX);
                                email.setText(ema);
                                d.setText(don);
                                p.setText(ps);
                                h.setText(hlp);
                                Glide.with(mainprofile.this).load(image).into(ppic);
                            }
                        } else {
                            Toast.makeText(mainprofile.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
