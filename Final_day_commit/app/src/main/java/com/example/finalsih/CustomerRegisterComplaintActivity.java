package com.example.finalsih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;


public class CustomerRegisterComplaintActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {


    private Toolbar mtoolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register_complaint);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.frag_cont, new user_home_frag());
        fragmentTransaction.commit();

        mtoolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mtoolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mtoolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//        if(item.getItemId() == R.id.stat_frag){
//            FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
//            fr.replace(R.id.frag_cont,new status_frag());
//            fr.commit();
//        }

//        if(item.getItemId() == R.id.pick_frag){
//            FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
//            fr.replace(R.id.frag_cont,new PickPointFrag());
//            fr.commit();
//        }
//        if(item.getItemId() == R.id.num_frag){
//            FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
//            fr.replace(R.id.frag_cont,new StatisticsFrag());
//            fr.commit();
//
//
//        }
//        if(item.getItemId() == R.id.fund_frag){
//            FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
//            fr.replace(R.id.frag_cont,new fundFrag());
//            fr.commit();
//
//
//        }
        if (item.getItemId() == R.id.home_frag) {
//            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
//            fr.replace(R.id.frag_cont, new user_home_frag());
//            fr.commit();
            Intent newi=new Intent(CustomerRegisterComplaintActivity.this,custmapActivity.class);
            startActivity(newi);


        }
        if(item.getItemId() == R.id.adopt_frag){
            FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
            fr.replace(R.id.frag_cont,new AdoptFrag());
            fr.commit();


        }
//        if(item.getItemId() == R.id.sup_frag){
//            FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
//            fr.replace(R.id.frag_cont,new SupportFrag());
//            fr.commit();
//
//
//        }


        return true;

    }
}

