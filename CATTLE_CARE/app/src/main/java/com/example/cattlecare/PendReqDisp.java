package com.example.cattlecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cattlecare.DriverActivity;
import com.example.cattlecare.MainActivity;
import com.example.cattlecare.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PendReqDisp extends AppCompatActivity {
    TextView id;
    String customer[]=new String[10];
    TextView address;
    Double crntlat,crntlong;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_logout,menu);
        return true;
    }


    public void logout(MenuItem item) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(PendReqDisp.this);
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


    public Boolean checkPermission(String permission){
        int check= ContextCompat.checkSelfPermission(this,permission);
        return  (check== PackageManager.PERMISSION_GRANTED);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pend_req_disp);
        id = findViewById(R.id.req_id);
        address = findViewById(R.id.req_address);
        Intent i = getIntent();
        if(!checkPermission(Manifest.permission.SEND_SMS)){
            ActivityCompat.requestPermissions(PendReqDisp.this, new String[]{Manifest.permission.SEND_SMS}, 501);
        }
        String id_fin = i.getStringExtra("id");
        id.setText(id_fin);
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(PendReqDisp.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getlocation();

        } else
            ActivityCompat.requestPermissions(PendReqDisp.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);


        address.setText(i.getStringExtra("address"));
        System.out.println("final detaioks "+id + " " + address);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String chkid = user.getUid();
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("DRIVER").child(chkid).child("ASSIGNED").child(String.valueOf(id_fin));

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> pl=(Map<String,String>) snapshot.getValue();

                TextView nam=findViewById(R.id.req_name);
                TextView aph=findViewById(R.id.req_phone);
                TextView ms=findViewById(R.id.req_message);
                nam.setText(pl.get("name"));
                aph.setText(pl.get("contact"));
                ms.setText("No. of cattle :-"+pl.get("msg"));
                customer[0]=pl.get("address");
                customer[1]=pl.get("contact");
                customer[2]=pl.get("image");
                customer[3]=pl.get("msg");
                customer[4]=pl.get("name");
                customer[5]=pl.get("psid");
                customer[6]=pl.get("status");
                customer[7]=pl.get("uid");
                customer[8]=pl.get("ulang");
                customer[9]=pl.get("ulat");
                Location loc1 = new Location("");

                loc1.setLatitude(Double.valueOf(crntlat));
                loc1.setLongitude(Double.valueOf(crntlong));

                Location loc2 = new Location("");
                loc2.setLatitude(Double.valueOf(customer[9]));
                loc2.setLongitude(Double.valueOf(customer[8]));

                double distance = loc1.distanceTo(loc2)/1000;
                String fff=String.valueOf(distance).substring(0,6);
                address.setText(customer[0]+", Distance is-:"+fff+"km");
                FirebaseStorage fs= FirebaseStorage.getInstance();
                StorageReference sf=fs.getReferenceFromUrl("gs://cattle-care-sih.appspot.com/complaints").child(customer[2]);
                try {
                    final File fl = File.createTempFile("img","jpg");
                    if(fl==null)
                        return;
                    sf.getFile(fl).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                                            System.out.println("DONEeeeeeeeeeeeeeeeeeee");
                            Bitmap bmp= BitmapFactory.decodeFile(fl.getAbsolutePath());
                            ImageView show=findViewById(R.id.imagexxx);
                            show.setImageBitmap(bmp);





                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("ma chudao");
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public  void navigate(View c){
        String url = "http://maps.google.com/maps?daddr=" + customer[9] + "," + customer[8];

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
//        Intent intent=new Intent(this,MainActivity2.class);
//
//        intent.putExtra("lat",Double.valueOf(customer[9]));
//
//
//        intent.putExtra("lon",Double.valueOf(customer[8]));
//        intent.putExtra("name",customer[4]) ;
//        intent.putExtra("address",customer[0]);
//        intent.putExtra("number",customer[1]);
//        intent.putExtra("ids",customer[5]);
//
//        startActivity(intent);

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

                    crntlat=location.getLatitude();
                    crntlong=location.getLongitude();

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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


    public void calllll(View c){
        if (ActivityCompat.checkSelfPermission(PendReqDisp.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PendReqDisp.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
            System.out.println("makabhosda");
            return;
        }
        else{
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            String num=customer[1];
            callIntent.setData(Uri.parse("tel:"+num));//change the number
            startActivity(callIntent);
        }


    }












    FusedLocationProviderClient client;
    public void report(View v){








        SmsManager smsManager= SmsManager.getSmsManagerForSubscriptionId(0);
        int pll=Integer.valueOf(customer[5])*393;

        smsManager.sendTextMessage( customer[1],null,"CATTLE CARE MANAGEMENT-Your request id "+pll+" has been resolved , reported and no rewards have been added.",null,null);
        smsManager.sendTextMessage(customer[1],null,"Please report authentically or otherwise your profile will be blocked from getting rewards.",null,null);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String chkid=user.getUid();
        final DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("DRIVER").child(chkid).child("ASSIGNED").child(customer[5]);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> lkad = new HashMap<>();

                lkad.put("status","RE");
                lkad.put("collecte","0");

                mref.updateChildren(lkad);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        final DatabaseReference muserdb=FirebaseDatabase.getInstance().getReference().child("COMPLAINTS").child(customer[5]);
        muserdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> potty = new HashMap<>();

                potty.put("status","RE");
                potty.put("rewards","0");
                potty.put("collected","0");

                muserdb.updateChildren(potty);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final DatabaseReference show=FirebaseDatabase.getInstance().getReference().child("number of animals").child(customer[5]);
        show.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Map<String, Object> pottys = new HashMap<>();

                    pottys.put("collected","0");

                    show.updateChildren(pottys);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "USER REPORTED",
                            Toast.LENGTH_SHORT);

                    toast.show();
                    Intent xx= new Intent(PendReqDisp.this, DriverActivity.class);
                    startActivity(xx);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    public void resolverequest(View gg){













        final EditText ip=new EditText(PendReqDisp.this);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(PendReqDisp.this);
        builder1.setView(ip);
        ip.setInputType(InputType.TYPE_CLASS_NUMBER);
        ip.setHint("Enter Number Here");
        builder1.setMessage("Enter number of animals collected.");
        builder1.setCancelable(true);
        builder1.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String cat=ip.getText().toString();
                if(cat.length()<1)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            " Invalid Input",
                            Toast.LENGTH_SHORT);

                    toast.show();
                    dialogInterface.cancel();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Request Resolved",
                            Toast.LENGTH_SHORT);

                    toast.show();
                    done(ip.getText().toString());
                    dialogInterface.cancel();

                }
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public void done(final String val){



        final String id=customer[5];

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String chkid=user.getUid();
        final DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("DRIVER").child(chkid).child("ASSIGNED").child(id);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> lkad = new HashMap<>();

                lkad.put("status","RE");
                lkad.put("collecte",val);

                mref.updateChildren(lkad);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        final DatabaseReference muserdb=FirebaseDatabase.getInstance().getReference().child("COMPLAINTS").child(id);
        muserdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> potty = new HashMap<>();

                potty.put("status","RE");
                potty.put("rewards","25");
                potty.put("collected",val);

                muserdb.updateChildren(potty);
                SmsManager smsManager= SmsManager.getSmsManagerForSubscriptionId(0);
                int pll=Integer.valueOf(id)*393;

                smsManager.sendTextMessage( customer[1],null,"CATTLE CARE MANAGEMENT-Your request id "+pll+" has been resolved and your rewards have been added.",null,null);
                smsManager.sendTextMessage(customer[1],null,"Please do give us your valuable feedback and donate and report more for amazing rewards",null,null);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        final DatabaseReference show=FirebaseDatabase.getInstance().getReference().child("number of animals").child(id);
        show.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Map<String, Object> pottys = new HashMap<>();

                    pottys.put("collected",val);

                    show.updateChildren(pottys);
                    Intent xx=new Intent(PendReqDisp.this,DriverActivity.class);
                    startActivity(xx);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }







}

