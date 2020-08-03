package com.example.finalsih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PickuppointActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gMap;String latti,longi,namey,codey;;long pod;
    public void addpp(View view)
    {



        AlertDialog.Builder ab=new AlertDialog.Builder(PickuppointActivity.this);
        LayoutInflater li = LayoutInflater.from(PickuppointActivity.this);
        final View promptsView = li.inflate(R.layout.prompts, null);

        ab.setView(promptsView);
        ab.setTitle("Enter unique code,lattitude and longitude");
        final EditText code=promptsView.findViewById(R.id.id);
        final EditText lat=promptsView.findViewById(R.id.lat);
        final EditText lon=promptsView.findViewById(R.id.lang);
        final EditText name=promptsView.findViewById(R.id.naam);
        lat.setText(String.valueOf(laty));
        lon.setText(String.valueOf(langy));
        Toast toast = Toast.makeText(getApplicationContext(),
                "Showing your current location , you can edit the loaction too..",
                Toast.LENGTH_LONG);
        toast.show();

        ab.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                codey=code.getText().toString();
                latti=lat.getText().toString();
                longi=lon.getText().toString();
                namey=name.getText().toString();
                if(codey.length()<1 || latti.length()<1 || longi.length()<1 || namey.length()<1)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "INVALID  DETAILS",
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if(codey.charAt(0)=='1' && codey.charAt(1)=='2' && codey.charAt(2)=='3' && codey.charAt(3)=='4' && codey.charAt(4)=='5' && codey.charAt(5)=='6')
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "DONE",
                            Toast.LENGTH_LONG);
                    toast.show();
                    storedbs();
                    dialogInterface.cancel();


                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "INVALID  CODE",
                            Toast.LENGTH_LONG);
                    toast.show();
                    dialogInterface.cancel();
                }





            }
        });
        ab.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });











        ab.setCancelable(true);


        AlertDialog dr=ab.create();
        dr.show();















    }
    public void storedbs(){
                final DatabaseReference mas= FirebaseDatabase.getInstance().getReference().child("PICKUPOINT").child(String.valueOf(pod+1));
        mas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!snapshot.exists()) {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("lat",latti);
                        userMap.put("long",longi);
                        userMap.put("name",namey);
                        mas.updateChildren(userMap);
                        Intent intent=new Intent(PickuppointActivity.this,PickuppointActivity.class);
                        startActivity(intent);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });















    }












LocationManager locationManager;
    Location uulocation;double laty,langy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickuppoint);
        SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapx);
        supportMapFragment.getMapAsync(this);
        Button b=findViewById(R.id.buttonppp);
        if(getIntent().getStringExtra("active").charAt(0)=='c'){
        b.setAlpha(0);}

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            Toast.makeText(PickuppointActivity.this, "Location  Not Available",
                    Toast.LENGTH_SHORT).show();

        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            uulocation = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (uulocation == null)
                uulocation = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(uulocation!=null){
                laty=uulocation.getLatitude();
                langy=uulocation.getLongitude();
                Toast.makeText(PickuppointActivity.this, "Location  Updated",
                        Toast.LENGTH_SHORT).show();}}






















        final DatabaseReference mas= FirebaseDatabase.getInstance().getReference().child("PICKUPOINT");
        mas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                pod=snapshot.getChildrenCount();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap=googleMap;int cnt=0;double x=15.299;double y=74.125;
        final DatabaseReference mas= FirebaseDatabase.getInstance().getReference().child("PICKUPOINT");
        mas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dts: snapshot.getChildren()){
                    Map<String,String> pl=(Map<String,String>)dts.getValue();

                    String lt=pl.get("lat");
                    String lng=pl.get("long");
                    String naam=pl.get("name");
                    LatLng mLatLng1 = new LatLng(Double.valueOf(lt),Double.valueOf(lng));
                    gMap.addMarker(new MarkerOptions().position(mLatLng1).title(naam).icon(funcmarker(getApplicationContext(), R.drawable.ic_picture)));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(15.4914937,73.8191506), 12));


        LatLng mLatLng = new LatLng(15.4915002,73.8199633);
        gMap.addMarker(new MarkerOptions().position(mLatLng).title("My Title").snippet("My Snippet"+"\n"+"1st Line Text"+"\n"+"2nd Line Text"+"\n"+"3rd Line Text").icon(funcmarker(getApplicationContext(), R.drawable.ic_picture)));


        LatLng mLatLng1 = new LatLng(15.491002,73.8197633);
        gMap.addMarker(new MarkerOptions().position(mLatLng1).title("My Title").snippet("My Snippet"+"\n"+"1st Line Text"+"\n"+"2nd Line Text"+"\n"+"3rd Line Text").icon(funcmarker(getApplicationContext(), R.drawable.ic_picture)));



    }

    private BitmapDescriptor funcmarker(Context applicationContext, int ic_picture) {

        Drawable vectorDrawable= ContextCompat.getDrawable(getApplicationContext(),ic_picture);
        vectorDrawable.setBounds(0,0,50,50);
        Bitmap bitmap=Bitmap.createBitmap(50,50,
                Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);







    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
