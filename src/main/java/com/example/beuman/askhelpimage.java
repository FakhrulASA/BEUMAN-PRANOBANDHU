package com.example.beuman;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class askhelpimage extends AppCompatActivity {
    private ImageView img;
    private Uri ur=null;
    private Button add,next;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ProgressBar p;
    private FirebaseFirestore firebaseFirestore;
    private  String user_id;
    private String donation="0",post="0",help="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askhelpimage);
        img=(ImageView)findViewById(R.id.picture);
        add=(Button)findViewById(R.id.add);
        next=(Button)findViewById(R.id.next);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        user_id=firebaseAuth.getCurrentUser().getUid();
        Random random = new Random();
        int randomNumber = random.nextInt(100-1) + 65;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(ur!=null){
                    StorageReference impage_path=storageReference.child("post_images").child(user_id);
                    impage_path.putFile(ur).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri url = urlTask.getResult();

                            Map<String,String> usermap= new HashMap<>();


                            usermap.put("image",url.toString());
                            firebaseFirestore.collection("postimage").document(user_id).set(usermap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())

                                            {

                                                Toasty.success(askhelpimage.this, "Photo stored, continue next", Toast.LENGTH_SHORT, true).show();
                                                startActivity(new Intent(askhelpimage.this,sendhelp.class));

                                                finish();

                                            }
                                            else
                                            {
                                                Toast.makeText(askhelpimage.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                Toasty.error(askhelpimage.this, task.getException().getMessage(), Toast.LENGTH_SHORT, true).show();

                                            }
                                        }
                                    });
                            // Toast.makeText(profile.this,url.toString(), Toast.LENGTH_SHORT).show();



                        }
                    });


                }
                else
                    {
                        Toasty.error(askhelpimage.this, "Please select a photo!", Toast.LENGTH_SHORT, true).show();
                    }


            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(askhelpimage.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(askhelpimage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    else{
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(askhelpimage.this);
                    }
                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ur = result.getUri();
                img.setImageURI(ur);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
