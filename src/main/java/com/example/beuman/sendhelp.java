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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class sendhelp extends AppCompatActivity implements LocationListener{
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private  String user_id;
    private CircleImageView ppic;
    private StorageReference storageReference;
    private RadioGroup reason,priority,times;
    private RadioButton r,p,t;
    private Uri ur=null;

    private final int GPS_REQUEST = 100;
    private LocationManager locationManager;
    EditText name,addres,age,number,type,needs,time;
    Button send,add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendhelp);
        ppic=(CircleImageView)findViewById(R.id.propic);
        name=(EditText)findViewById(R.id.name);
        add=(Button)findViewById(R.id.addlocation);
        addres=(EditText)findViewById(R.id.address);
        number=(EditText)findViewById(R.id.number);
        needs=(EditText)findViewById(R.id.need);
        send=(Button)findViewById(R.id.sendx);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        user_id=firebaseAuth.getCurrentUser().getUid();
        askHelper askhelp=new askHelper();
        reason=(RadioGroup) findViewById(R.id.reasons);
        reason.check(R.id.ss);
        priority=(RadioGroup) findViewById(R.id.priority);
        priority.check(R.id.sss);
        times=(RadioGroup) findViewById(R.id.addtype);
        times.check(R.id.ssss);
        root=FirebaseDatabase.getInstance();
        reference=root.getReference("posts");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selected1 = reason.getCheckedRadioButtonId();
                r = (RadioButton) findViewById(selected1);
                int selected2 = priority.getCheckedRadioButtonId();
                p = (RadioButton) findViewById(selected2);
                int selected3 = times.getCheckedRadioButtonId();
                t = (RadioButton) findViewById(selected3);

                String namex = name.getText().toString();
                String addressx = addres.getText().toString();
                String numberx = number.getText().toString();
                String needx = needs.getText().toString();

                askHelper askhelp = new askHelper(namex, addressx, numberx, p.getText().toString(), needx, t.getText().toString(), r.getText().toString(), user_id);
                if(namex.length()==0||addressx.length()==0||needx.length()==0||r.length()==0||p.length()==0||t.length()==0)
                {
                    Toasty.error(sendhelp.this, "fields cannot be empty!", Toast.LENGTH_SHORT, true).show();
                }
                else
                {


                reference.child(user_id).setValue(askhelp);
                name.setText("");
                addres.setText("");
                number.setText("");
                needs.setText("");
                    startActivity(new Intent(sendhelp.this, MainActivity.class));
                    Toasty.success(sendhelp.this, "Successfully posted!. Waiting for verification", Toast.LENGTH_SHORT, true).show();
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

                    ActivityCompat.requestPermissions(sendhelp.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, GPS_REQUEST);


                }else{
                    getLocation();
                }
            }
        });
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
                    2000, 10, (LocationListener) this);



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
                addres.setText(strAdd);
            } else {
                addres.setText(strAdd);
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








                addres.setText(all);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
