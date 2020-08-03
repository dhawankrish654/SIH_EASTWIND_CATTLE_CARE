package com.example.finalsih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.intellij.lang.annotations.Language;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
int drv,cust;
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


MaterialButton drvbtn,custbtn;ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drvbtn=findViewById(R.id.dr_bxn);
        Button btn=findViewById(R.id.switch1);
        test=0;





//        Intent xx=new Intent(MainActivity.this,NewsActivity.class);
//        startActivity(xx);






//    startActivity(new Intent(MainActivity.this,MainActivity.class));








        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.SEND_SMS,
                android.Manifest.permission.CAMERA
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        custbtn=findViewById(R.id.user_btn);
        drvbtn.setAlpha(0);
        custbtn.setAlpha(0);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            isdriver(FirebaseAuth.getInstance().getCurrentUser().getUid());iscustomer(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        else{
            progressDialog.hide();
            drvbtn.setAlpha(1);
            custbtn.setAlpha(1);
        }


    }
int flg;
private  void isdriver(final String idx){
         flg=0;
        DatabaseReference ccc=FirebaseDatabase.getInstance().getReference().child("DRIVER");
        ccc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dts: snapshot.getChildren()){

                    if(dts.getKey().equals(idx))
                    {flg=1;drvbtn.setAlpha(1);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "YOU ARE AUTHENTICATED AS A DRIVER",
                                Toast.LENGTH_SHORT);

                        toast.show();progressDialog.hide();}
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









}
int f2;
private void iscustomer(final String idx){

    f2=0;
    DatabaseReference ccc=FirebaseDatabase.getInstance().getReference().child("CUSTOMERS");
    ccc.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot dts: snapshot.getChildren()){
                System.out.println("KEY"+dts.getKey());
                if(dts.getKey().equals(idx))
                {f2=1;custbtn.setAlpha(1);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "YOU ARE AUTHENTICATED AS AN USER",
                            Toast.LENGTH_SHORT);

                    toast.show();progressDialog.hide();}
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });











}







    public void userlogin(View r) {


        Intent s = new Intent(MainActivity.this, UserLoginActivity.class);
        startActivity(s);


    }


    public void dl(View o) {
        final EditText ip=new EditText(MainActivity.this);

//        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
//        builder1.setView(ip);
//        ip.setInputType(InputType.TYPE_CLASS_NUMBER);
//        ip.setHint("Enter Number Here");
//        builder1.setMessage("Enter number of animals collected.");


        Intent s = new Intent(MainActivity.this, DriverLogin.class);
        startActivity(s);

    }
int test;
    public void langg(View view) {

        if(test==0){


            test=1;
            Locale locale = new Locale("kok");
            Locale.setDefault(locale);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "लंगुअगे चंगे तो कोंकनि",
                    Toast.LENGTH_SHORT);

            toast.show();progressDialog.hide();
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            this.setContentView(R.layout.activity_main);






        }
        else
        {


            test=0;
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Language set to english",
                    Toast.LENGTH_SHORT);

            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            this.setContentView(R.layout.activity_main);

        }
    }
}