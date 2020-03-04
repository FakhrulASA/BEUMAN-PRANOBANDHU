package com.example.beuman;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class editprofile extends AppCompatActivity implements LocationListener {
    private CircleImageView ppic;
    private Uri ur=null;
    private EditText name , pass, address,email;
    private Button save,add;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ProgressBar p;
    private FirebaseFirestore firebaseFirestore;
    private  String user_id,user_loaction,latlong;
    private String donation="0",post="0",help="0";
    private final int GPS_REQUEST = 100;

    private static final String TAG = "MainActivity";
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        ppic=(CircleImageView)findViewById(R.id.propic);
        name =(EditText)findViewById(R.id.username);
        pass =(EditText)findViewById(R.id.number);
        address =(EditText)findViewById(R.id.address);
        email =(EditText)findViewById(R.id.email);
        save =(Button)findViewById(R.id.save);
        add =(Button)findViewById(R.id.addlocation);
        p =(ProgressBar)findViewById(R.id.pbar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        user_id=firebaseAuth.getCurrentUser().getUid();
        p.setVisibility(View.INVISIBLE);
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
                                String image= task.getResult().getString("image");

                                donation= task.getResult().getString("donation");
                                post= task.getResult().getString("post");
                                help= task.getResult().getString("help");
                                ur = Uri.parse(image);
                                name.setText(username);
                                pass.setText(contact);
                                address.setText(addressX);
                                email.setText(emaila);
                               Glide.with(editprofile.this).load(image).into(ppic);
                            }
                        }
                        else
                        {

                        }
                    }
                });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(editprofile.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, GPS_REQUEST);


                }else{
                    getLocation();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                p.setVisibility(View.VISIBLE);
                String userName= name.getText().toString().trim();
                String contacts= pass.getText().toString().trim();
                String userAddress= address.getText().toString().trim();
                String useremail= email.getText().toString().trim();
                if(name.length()==0||pass.length()==0||address.length()==0||email.length()==0)
                {
                    p.setVisibility(View.INVISIBLE);
                    Toasty.error(editprofile.this, "Fields cannot be empty!", Toast.LENGTH_SHORT, true).show();
                }
                else {


                    if (ur != null) {
                        StorageReference impage_path = storageReference.child("profile_images").child(user_id);
                        impage_path.putFile(ur).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                Uri url = urlTask.getResult();

                                Map<String, String> usermap = new HashMap<>();
                                usermap.put("username", name.getText().toString().trim());
                                usermap.put("contact", pass.getText().toString().trim());
                                usermap.put("address", address.getText().toString().trim());
                                usermap.put("email", email.getText().toString().trim());
                                usermap.put("donation", donation);
                                usermap.put("post", post);
                                usermap.put("help", help);
                                usermap.put("image", url.toString());

                                firebaseFirestore.collection("Users").document(user_id).set(usermap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    p.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(editprofile.this, "profile updated", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(editprofile.this, mainprofile.class));

                                                    finish();

                                                } else {
                                                    p.setVisibility(View.INVISIBLE);
                                                    Toasty.error(editprofile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });


                                // Toast.makeText(profile.this,url.toString(), Toast.LENGTH_SHORT).show();



                            }
                        });


                    } else {
                        p.setVisibility(View.INVISIBLE);
                        Toasty.error(editprofile.this, "You must add/update your profile picture to proceed", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        ppic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(editprofile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(editprofile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    else{
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(editprofile.this);
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
                ppic.setImageURI(ur);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GPS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void getLocation() {
        try{
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    2000, 10, this);



        }catch (SecurityException e){
            e.printStackTrace();
        }
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                address.setText(strAdd);
            } else {
                address.setText(strAdd);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
    }
    @Override
    public void onLocationChanged(Location location) {
       // getAddress(editprofile.this, location.getLatitude(), location.getLongitude());
        getCompleteAddressString(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public  String getAddress(Context context, double LATITUDE, double LONGITUDE) {

//Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 10);
            if (addresses != null && addresses.size() > 0) {


                String address1 = addresses.get(1).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                String road = addresses.get(1).getFeatureName();
                String road1 = addresses.get(1).getSubLocality();
                String city = addresses.get(1).getLocality();
                String country = addresses.get(1).getCountryName();
                String all = road + "," + road1 + "," + city + "," + country;
                double ltd = addresses.get(1).getLatitude();
                double lod = addresses.get(1).getLatitude();

                String ltd2 = String.valueOf(ltd);
                String lod2 = String.valueOf(lod);
                Map<String,String> usermap2= new HashMap<>();
                usermap2.put("Latitude",ltd2);
                usermap2.put("Longitude",lod2);


                firebaseFirestore.collection("Users").document(user_id).collection("loaction_data").document("LAT+LONG").set(usermap2)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                } else {


                                }
                            }
                        });







                address.setText(all);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
