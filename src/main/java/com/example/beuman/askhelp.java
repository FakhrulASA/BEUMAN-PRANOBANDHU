package com.example.beuman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class askhelp extends AppCompatActivity {


    private  String user_id;
    ProgressBar pbar;
    private Dialog dd,d2;
    private EditText qt,des,note;
    private TextView name , pass, address,email;
    private Button save,add,send,pop,faq;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ProgressBar p;
    private FirebaseDatabase root;


    private AutoCompleteTextView type;
    private FirebaseFirestore firebaseFirestore;
    private String[] TYPE = new String[]{"Wear","Foods","Fruits","Medicine","Accommodation","Cooked Food","Lrft over(Ceremony)","Uncooked food","Rice","Jakat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askhelp);
        p =(ProgressBar)findViewById(R.id.pbar);
        name =(TextView)findViewById(R.id.name);
        pass =(TextView) findViewById(R.id.contact);
        address =(TextView) findViewById(R.id.address);
        qt=(EditText)findViewById(R.id.quantity);
        des=(EditText)findViewById(R.id.description);
        note=(EditText)findViewById(R.id.note);
        faq=(Button)findViewById(R.id.faq);
        type =(AutoCompleteTextView)findViewById(R.id.helptype);
        send=(Button)findViewById(R.id.send);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,TYPE);
        type.setAdapter(adapter);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        user_id=firebaseAuth.getCurrentUser().getUid();
        pbar =(ProgressBar)findViewById(R.id.postbar);
        pbar.setVisibility(View.INVISIBLE);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d2 =new Dialog(askhelp.this);
                d2.setContentView(R.layout.faq1);
                d2.show();
            }
        });


        if(!isConnected(askhelp.this)){
            buildDialog(askhelp.this).show();
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                Map<String,String> posts= new HashMap<>();
                posts.put("type",type.getText().toString().trim());
                posts.put("description",des.getText().toString().trim());
                posts.put("quantity",qt.getText().toString().trim());
                posts.put("note",note.getText().toString().trim());
                posts.put("userid",user_id);
                posts.put("staus","Pending");
                posts.put("reached","pending");
                if(note.getText().toString().trim().length()==0||qt.getText().toString().trim().length()==0||des.getText().toString().trim().length()==0||type.getText().toString().trim().length()==0)
                {
                    Toasty.error(askhelp.this, "Fields cannot be empty!", Toast.LENGTH_SHORT, true).show();
                    pbar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    firebaseFirestore.collection("Help_posts").add(posts).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                type.setText("");
                                qt.setText("");
                                des.setText("");
                                note.setText("");
                                dd =new Dialog(askhelp.this);
                                dd.setContentView(R.layout.pop);
                                dd.show();
                                pbar.setVisibility(View.INVISIBLE);
                                pop = (Button) dd.findViewById(R.id.popback);
                                pop.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(askhelp.this, MainActivity.class));
                                        finish();

                                    }
                                });
                            } else {

                                Toasty.warning(askhelp.this, task.getException().getMessage(), Toast.LENGTH_SHORT, true).show();
                                pbar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
        firebaseFirestore.collection("Users").document(user_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().exists())
                            {
                                String username = task.getResult().getString("username");
                                String contact = task.getResult().getString("contact");
                                String addressX= task.getResult().getString("address");
                                String emaila = task.getResult().getString("email");

                                name.setText("আপনার নাম: "+username);
                                pass.setText("যোগাযোগ নম্বর: "+contact);
                                address.setText("আপনার ঠিকানা: "+addressX);


                            }
                        }
                        else
                        {

                        }
                    }
                });


    }
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null &&wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No internet connection!");
        builder.setMessage("You need to be connected to wifi/mobile data in order to continue. Connect to wifi/mobile data and restart.");

        builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(askhelp.this,splash.class));
            }
        });

        return builder;
    }
}
