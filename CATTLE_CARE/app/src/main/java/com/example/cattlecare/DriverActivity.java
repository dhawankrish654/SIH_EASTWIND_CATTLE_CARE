package com.example.cattlecare;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cattlecare.MainActivity;
import com.example.cattlecare.R;
import com.example.cattlecare.locservice;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverActivity extends AppCompatActivity {
    FusedLocationProviderClient client;
    double lat, lang;
    TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager viewPager;

    PendingFrag pendingFrag;
    com.example.finalsih.ResolvedFrag resolvedFrag;
    LocationManager locationManager;
    Location uulocation;

    @Override
    public void onBackPressed() {
        Intent ll = new Intent(DriverActivity.this, MainActivity.class);
        startActivity(ll);
        super.onBackPressed();
    }


    public void logout(MenuItem item) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(DriverActivity.this);
        builder1.setMessage("Are you sure you want to logout? ");
        builder1.setCancelable(true);
        builder1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "SIGNED OUT",
                        Toast.LENGTH_SHORT);

                toast.show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        builder1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }


    public Boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        Intent ddd = new Intent(DriverActivity.this, locservice.class);


        startService(ddd);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            Toast.makeText(DriverActivity.this, "Location  Not Available",
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
            lat=uulocation.getLatitude();
            lang=uulocation.getLongitude();
            Toast.makeText(DriverActivity.this, "Location  Updated",
                    Toast.LENGTH_SHORT).show();}





        if(!checkPermission(Manifest.permission.SEND_SMS)){
            ActivityCompat.requestPermissions(DriverActivity.this, new String[]{Manifest.permission.SEND_SMS}, 501);
        }
        client = LocationServices.getFusedLocationProviderClient(this);   toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager=findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        pendingFrag = new PendingFrag();
        resolvedFrag = new com.example.finalsih.ResolvedFrag();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(pendingFrag, "Pending");
        viewPagerAdapter.addFragment(resolvedFrag,"Resolved");
        viewPager.setAdapter(viewPagerAdapter);

        if (ActivityCompat.checkSelfPermission(DriverActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getlocation();

        } else
            ActivityCompat.requestPermissions(DriverActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);


    }



    public void getlocation() {
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

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {

                    lat=location.getLatitude();
                    lang=location.getLongitude();
                    System.out.println(lat+" "+lang);
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final DatabaseReference muserdb= FirebaseDatabase.getInstance().getReference().child("DRIVER").child(user.getUid());
                    muserdb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("dlat", String.valueOf(lat));
                            userMap.put("dlang",String.valueOf(lang));
                            muserdb.updateChildren(userMap);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }


            }

        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("NOOOOOOOOOOOOOOOOOOOOOOOOOO");
            }
        });

    }





    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }


    }
}