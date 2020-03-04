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
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    private Button exit,profile,post,donate,ask,event,pubhelp,feed;
    private FirebaseAuth firebaseAuth;
    private Dialog dd;
    private Uri ur = null;
    private CircleImageView ppic;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private LinearLayout helpa,shelp,evenh,prhelp,donh,main;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private TextView name,date,donation,pos,help;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ppic = (CircleImageView) findViewById(R.id.profile_image);
        name=(TextView)findViewById(R.id.name);
        date = (TextView) findViewById(R.id.date);
        post=(Button)findViewById(R.id.post);
        exit=(Button)findViewById(R.id.exiT);
        pubhelp=(Button)findViewById(R.id.phelp);
        helpa=(LinearLayout)findViewById(R.id.helpwant);
        main=(LinearLayout)findViewById(R.id.ful);
        donh=(LinearLayout)findViewById(R.id.sendmoneyl);
        shelp=(LinearLayout)findViewById(R.id.sendhelpl);
        evenh=(LinearLayout)findViewById(R.id.eventhelpl);
        prhelp=(LinearLayout)findViewById(R.id.pubhelpl);
        ask=(Button)findViewById(R.id.button3);
        donate=(Button)findViewById(R.id.donate5);
        event=(Button)findViewById(R.id.event);
        feed=(Button)findViewById(R.id.feed);
        dd =new Dialog(this);
        donation=(TextView)findViewById(R.id.donations);
        pos=(TextView)findViewById(R.id.posts);
        help=(TextView)findViewById(R.id.help);

        profile=(Button)findViewById(R.id.profile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        user_id = firebaseAuth.getCurrentUser().getUid();
        Calendar c = Calendar.getInstance();
        this.mHandler = new Handler();
        m_Runnable.run();
        SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM");
        String formattedDate = df.format(c.getTime());
        date.setText(formattedDate);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,mainprofile.class));
            }
        });
        shelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,askhelp.class));
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,askhelp.class));
            }
        });
        prhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Public.class));
            }
        });
        pubhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Public.class));
            }
        });
        donh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,donattion.class));
            }
        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,donattion.class));
            }
        });
        helpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,askhelpimage.class));
            }
        });
        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,askhelpimage.class));
            }
        });
        evenh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,eventstart.class));
            }
        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,eventstart.class));
            }
        });
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,pending.class));


            }
        });
        if(!isConnected(MainActivity.this)){
            buildDialog(MainActivity.this).show();
        }
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.setContentView(R.layout.exitpop);
                dd.show();
                Button out =(Button)dd.findViewById(R.id.exit);
                Button close=(Button)dd.findViewById(R.id.close);
                Button about=(Button)dd.findViewById(R.id.about);
                out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this,signIN.class));
                        finish();
                    }
                });
                about.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
                        openURL.setData(Uri.parse("https://sites.google.com/diu.edu.bd/beuman/beuman"));
                        startActivity(openURL);

                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.exit(0);

                    }
                });

            }
        });

   profile.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           startActivity(new Intent(MainActivity.this,mainprofile.class));
       }
   });

   ppic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,mainprofile.class)); }
   });


        firebaseFirestore.collection("Users").document(user_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String username = task.getResult().getString("username");
                                String image = task.getResult().getString("image");
                                String don = task.getResult().getString("donation");
                                String ps = task.getResult().getString("post");
                                 String hlp = task.getResult().getString("help");
                                ur = Uri.parse(image);
                                name.setText(username);
                                  donation.setText(don);
                                  pos.setText(ps);
                                 help.setText(hlp);

                                Glide.with(MainActivity.this).load(image).into(ppic);
                            }
                        } else {
                            Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            if(!isConnected(MainActivity.this)){
                buildDialog(MainActivity.this).show();
            }
            firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    String username = task.getResult().getString("username");
                                    String image = task.getResult().getString("image");
                                    String don = task.getResult().getString("donation");
                                    String ps = task.getResult().getString("post");
                                    String hlp = task.getResult().getString("help");
                                    ur = Uri.parse(image);
                                    name.setText(username);
                                    donation.setText(don);
                                    pos.setText(ps);
                                    help.setText(hlp);

                                    Glide.with(MainActivity.this).load(image).into(ppic);
                                }
                            } else {
                                Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            MainActivity.this.mHandler.postDelayed(m_Runnable,15000);
        }

    };
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
                startActivity(new Intent(MainActivity.this,splash.class));
            }
        });

        return builder;
    }
}
