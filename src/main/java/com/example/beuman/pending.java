package com.example.beuman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class pending extends AppCompatActivity {
    private ListView listView;
    private EditText namex,address,age,reason,time;
    private DatabaseReference reference;
    private FirebaseRecyclerOptions<postdata>options;
    private FirebaseRecyclerAdapter<postdata,MyViewHolder>adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        reference=FirebaseDatabase.getInstance().getReference().child("posts");
        recyclerView=findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        options=new FirebaseRecyclerOptions.Builder<postdata>().setQuery(reference,postdata.class).build();
        adapter=new FirebaseRecyclerAdapter<postdata, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull postdata postdata) {
                 myViewHolder.name.setText(postdata.getName());
                myViewHolder.address.setText("বর্তমান ঠিকানা: "+postdata.getAddress());
                myViewHolder.needs.setText("চাহিদা : "+postdata.getNeeds());
               // myViewHolder.age.setText(postdata.getAge());
                 myViewHolder.reason.setText(postdata.getReason());
                myViewHolder.time.setText("জরুরি অবস্থা: "+postdata.getTime());
                myViewHolder.type.setText("কারণ: "+postdata.getAge());


            }

            @NonNull
            @Override
               public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist,parent,false);
                return new MyViewHolder(v);

            }
           // @Override
           // public int getItemCount(){return namex.length();}
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}