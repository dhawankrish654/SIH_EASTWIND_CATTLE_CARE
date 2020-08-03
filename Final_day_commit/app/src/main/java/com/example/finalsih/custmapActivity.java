package com.example.finalsih;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.razorpay.PaymentResultListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class custmapActivity extends AppCompatActivity implements OnMapReadyCallback, PaymentResultListener, NavigationView.OnNavigationItemSelectedListener {
    EditText add, phone, name, msg;
    Location userLocation;
    FusedLocationProviderClient client;
    MySupportMapFragment supportMapFragment;
    private Toolbar mtoolbar;
    private DrawerLayout mDrawerLayout;
    private ImageView previewImage;
    private NavigationView mNavigationView;

    FirebaseUser user;

    public void onMapReady(GoogleMap googleMap) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            Toast.makeText(custmapActivity.this, "Location  Not Available",
                    Toast.LENGTH_SHORT).show();

        } else {
            uulocation = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (uulocation == null)
                uulocation = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (uulocation != null) {
                lat = uulocation.getLatitude();
                lang = uulocation.getLongitude();
                Toast.makeText(custmapActivity.this, "Location  Updated",
                        Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void getcmpnum() {

        DatabaseReference fnal = FirebaseDatabase.getInstance().getReference().child("COMPLAINTS");
        fnal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                System.out.println("cehcking");
                cmpid = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    NestedScrollView scroll;
    ProgressDialog progressDialog;

    LocationManager locationManager;
    Location uulocation;

    String CHANNEL_ID = "2002";

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent ddd = new Intent(custmapActivity.this, locservice.class);


        startService(ddd);

        createNotificationChannel();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            Toast.makeText(custmapActivity.this, "Location  Not Available",
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
        lat=uulocation.getLatitude();
        lang=uulocation.getLongitude();
        Toast.makeText(custmapActivity.this, "Location  Updated",
                Toast.LENGTH_SHORT).show();}}










        setContentView(R.layout.activity_custmap);
        mtoolbar = findViewById(R.id.main_toolbar1);
        scroll=findViewById(R.id.scroll);
        setSupportActionBar(mtoolbar);
        user=FirebaseAuth.getInstance().getCurrentUser();
        getcmpnum();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);





getcmpnum();
        mDrawerLayout = findViewById(R.id.drawer_layout_extra);
        mNavigationView = findViewById(R.id.nav_view1);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mtoolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        mStorage=FirebaseStorage.getInstance().getReference();

        mNavigationView.setNavigationItemSelectedListener(this);
        add = findViewById(R.id.address);

        previewImage = findViewById(R.id.image_preview);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phonecc);
        msg = findViewById(R.id.msg);

        supportMapFragment = (MySupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapx);
        supportMapFragment.getMapAsync(this);
        client = LocationServices.getFusedLocationProviderClient(this);

        final RelativeLayout rl = findViewById(R.id.rl);
        if (supportMapFragment != null)
            supportMapFragment.setListener(new MySupportMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    rl.requestDisallowInterceptTouchEvent(true);
                }
            });




        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("CUSTOMERS").child(user.getUid());
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> usemap = (Map<String, String>) snapshot.getValue();
                phone.setText(usemap.get("phone"));
                name.setText(usemap.get("name"));
                Toast toast = Toast.makeText(getApplicationContext(),
                        "REGISTER YOUR COMPLAINT HERE",
                        Toast.LENGTH_SHORT);

                toast.show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (ActivityCompat.checkSelfPermission(custmapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getlocation();

        } else
            ActivityCompat.requestPermissions(custmapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_frag) {
//            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
//            fr.replace(R.id.frag_cont, new user_home_frag());
//            fr.commit();
            Intent newi = new Intent(custmapActivity.this, custmapActivity.class);
            startActivity(newi);


        }
        if(item.getItemId() == R.id.feedback){
            mDrawerLayout.closeDrawers();
            final RatingBar rb=new RatingBar(custmapActivity.this);
            AlertDialog.Builder builder1 = new AlertDialog.Builder(custmapActivity.this);
            builder1.setMessage("Please Rate us...");
            builder1.setCancelable(true);
            rb.setNumStars(5);


           builder1.setView(rb);

           builder1.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                  if(rb.getRating()<2){
                      dialogInterface.cancel();
                      return;
                  }

                   Toast toast = Toast.makeText(getApplicationContext(),
                           "Thank You!",
                           Toast.LENGTH_LONG);

                   toast.show();
                   dialogInterface.cancel();
               }
           });

           builder1.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   dialogInterface.cancel();
               }
           });

            AlertDialog alert11 = builder1.create();
            alert11.show();


        }








        if(item.getItemId() == R.id.fund_frag){
            mDrawerLayout.closeDrawers();
            scroll.setVisibility(View.INVISIBLE);
            FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
            fr.replace(R.id.frag_cont,new fundFrag());
            fr.commit();


        }
        if(item.getItemId() == R.id.adopt_frag){
            mDrawerLayout.closeDrawers();
            scroll.setVisibility(View.INVISIBLE);
            FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
            fr.replace(R.id.frag_cont,new AdoptFrag());
            fr.commit();


        }
        if(item.getItemId()==R.id.profile_frag){
            mDrawerLayout.closeDrawers();
            scroll.setVisibility(View.INVISIBLE);
            FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
            fr.replace(R.id.frag_cont,new UserProfile());

            fr.commit();

        }



                if(item.getItemId() == R.id.num_frag){
                    mDrawerLayout.closeDrawers();
                    scroll.setVisibility(View.INVISIBLE);
            FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
            fr.replace(R.id.frag_cont,new StatisticsFrag());
            fr.commit();


        }
                if(item.getItemId() == R.id.sup_frag){
                    mDrawerLayout.closeDrawers();
                    scroll.setVisibility(View.INVISIBLE);
            FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
            fr.replace(R.id.frag_cont,new SupportFrag());
            fr.commit();


        }
                if(item.getItemId()==R.id.news_frag){

            mDrawerLayout.closeDrawers();
            Intent nm=new Intent(custmapActivity.this,NewsActivity.class);
            startActivity(nm);

        }



                if(item.getItemId()==R.id.infoslider){
                    mDrawerLayout.closeDrawers();
                    Intent nm=new Intent(custmapActivity.this,sliderActivity.class);
                    startActivity(nm);

                }
                if(item.getItemId() == R.id.stat_frag){
                    mDrawerLayout.closeDrawers();
                    progressDialog.show();

            FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
            fr.replace(R.id.frag_cont,new status_frag());
            fr.commit();
            progressDialog.hide();
                    scroll.setVisibility(View.INVISIBLE);

        }
                if(item.getItemId() == R.id.pick_frag){
                    mDrawerLayout.closeDrawers();
            Intent nn=new Intent(custmapActivity.this,PickuppointActivity.class);
            nn.putExtra("active","c");

            startActivity(nn);

        }










        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
double lat,lang;
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
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            lat=location.getLatitude();
                            lang=location.getLongitude();
                            LatLng userloc = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(userloc).title("Your Location").icon(funcmarker(getApplicationContext(), R.drawable.ic_location_on_black_24dp)));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
                            String address = getLocationAddress(lat,lang);
                            add.setText(address);
                        }
                    });
                }

            }
        });

    }
    private BitmapDescriptor funcmarker(Context applicationContext, int ic_picture) {

        Drawable vectorDrawable= ContextCompat.getDrawable(getApplicationContext(),ic_picture);
        vectorDrawable.setBounds(0,0,100,100);
        Bitmap bitmap=Bitmap.createBitmap(200,200,
                Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);







    }

    private String getLocationAddress(double latitude, double longitude) {
        String res = "";
        try {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList.isEmpty()) {
                // addressText.setText("Wating for Location......");
            } else {
                if (addressList.size() > 0) {
//                    res=addressList.get(0).getFeatureName() + "," + addressList.get(0).getLocality() + "," + addressList.get(0) + "," + addressList.get(0).getAdminArea() + "," + addressList.get(0).getCountryName();
//                    res=res+addressList.get(0).getFeatureName() + ", " + addressList.get(0).getLocality() + ", " + addressList.get(0).getAdminArea() + ", " + addressList.get(0).getCountryName();
                    res = addressList.get(0).getAddressLine(0);
                }
            }
        }

        catch (Exception e) {

            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getlocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }





    }

    private static final int CAMERA_REQUEST = 120;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    public void clickimage(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        }
    }
    StorageReference mStorage;Bitmap photo;
    Uri resulturi;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && null != data) {
            photo = (Bitmap) data.getExtras().get("data");
            Uri uri=data.getData();
            resulturi=uri;
            previewImage.setImageBitmap(photo);
            photo=resize(photo, 500, 500);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] datastore = baos.toByteArray();

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            long id=cmpid;
            StorageReference fp=mStorage.child("complaints").child(String.valueOf(id+1)+user.getUid());

            UploadTask uploadTask = fp.putBytes(datastore);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("ppppppppppppppppp"+user.getUid());

                }
            });



        }
    }

    private String getStringImage;
    private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            //sessionManager.SavedProfileImage(getStringImage(image));
            getStringImage = getStringImage(image);
            return image;
        } else {
            //  sessionManager.SavedProfileImage(getStringImage(image));
            return image;
        }
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }
long cmpid;

    public void lodgecomplaint(View view) {
        System.out.println(cmpid);
        if(cmpid==0)
            getcmpnum();


if(msg.getText().toString().length()==0 || phone.getText().toString().length()<10 || name.getText().toString().length()<2 || add.getText().toString().length()<5) {
    Toast toast = Toast.makeText(getApplicationContext(),
            "INVALID DETAILS-Empty Cattle Count or Invalid Phone number or Address",
            Toast.LENGTH_SHORT);

    toast.show();
    return;
}

       final long idx=cmpid;
final int fffff=(int)idx+2;
        final DatabaseReference grp=FirebaseDatabase.getInstance().getReference().child("COMPLAINTS").child(String.valueOf(fffff));

        grp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("uid",user.getUid());
                    userMap.put("pid",String.valueOf(idx+1));
                    userMap.put("phone",phone.getText().toString());
                    userMap.put("name", name.getText().toString());
                    userMap.put("Msg",msg.getText().toString());
                    userMap.put("address",add.getText().toString());
                    userMap.put("latitiude",String.valueOf(lat));
                    userMap.put("longitude",String.valueOf(lang));
                    userMap.put("status","P");
                    long id=cmpid;
                    userMap.put("image",String.valueOf(id+2)+user.getUid());
                    grp.updateChildren(userMap);
                    assigntonearestdriver(user.getUid(),String.valueOf(idx+2),phone.getText().toString(),name.getText().toString(),msg.getText().toString(),add.getText().toString(),String.valueOf(lat),String.valueOf(lang),String.valueOf(id+1)+user.getUid());


                    AlertDialog.Builder ab=new AlertDialog.Builder(custmapActivity.this);
                    LayoutInflater li = LayoutInflater.from(custmapActivity.this);
                    final View promptsView = li.inflate(R.layout.dialogue, null);
                    ab.setView(promptsView);

                    final MaterialTextView code=promptsView.findViewById(R.id.txtInfo);
                    final Button ok=promptsView.findViewById(R.id.okbtn);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(custmapActivity.this, custmapActivity.class);
                            startActivity(intent);

                        }
                    });
                    code.setText(String.valueOf((cmpid+2)*393) + "\n . You can use this id to check status & submit feedback.");


                    ab.setCancelable(false);

                    ab.create();
                    ab.show();







                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    String dlat,dlang,dnum;
    double distanse=100000000000.0000;
    String findlat,findlang,findnum;

    String database[];
    String driverid;
    public void assigntonearestdriver(String userid, String psid, String phone, String name, String msg, String address, final String lat, final String lang,String img){
database=new String[9];
database[0]=userid;
database[1]=psid;
database[2]=name;
database[3]=msg;
database[4]=address;
database[5]=lat;
database[6]=lang;
database[7]=phone;
database[8]=img;
DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("DRIVER");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dts: snapshot.getChildren()){

                    Map<String,String> mp=(Map<String,String>)dts.getValue();
                    dlat=mp.get("dlat");
                    dlang=mp.get("dlang");
                    dnum=mp.get("phone");
                    Location loc1 = new Location("");

                    loc1.setLatitude(Double.valueOf(lat));
                    loc1.setLongitude(Double.valueOf(lang));

                    Location loc2 = new Location("");
                    loc2.setLatitude(Double.valueOf(dlat));
                    loc2.setLongitude(Double.valueOf(dlang));

                    double distanceInMeters = loc1.distanceTo(loc2);
                    System.out.println(distanceInMeters);
                    if(distanceInMeters<distanse){
                        distanse=distanceInMeters;
                        findlat=dlat;
                        findlang=dlang;
                        findnum=dnum;
                        driverid=dts.getKey();
                    }


                }
                savedatainserver();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });












    }

//    database=new String[8];
//    database[0]=userid;
//    database[1]=psid;
//    database[2]=name;
//    database[3]=msg;
//    database[4]=address;
//    database[5]=lat;
//    database[6]=lang;
//    database[7]=phone;
public void savedatainserver(){

        System.out.println("Final details"+findlang+" "+findlat+" "+findnum);


        if(driverid==null){
            driverid="kruBqR4SNUcaOuECopKDQ0SuKNK2";
        }

        final DatabaseReference savdri=FirebaseDatabase.getInstance().getReference().child("DRIVER").child(driverid).child("ASSIGNED").child(database[1]);
        savdri.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> lkad = new HashMap<>();
                lkad.put("uid",database[0]);
                lkad.put("psid",database[1]);
                lkad.put("name",database[2]);
                lkad.put("msg",database[3]);
                lkad.put("address",database[4]);
                lkad.put("ulat",database[5]);
                lkad.put("ulang",database[6]);
                lkad.put("contact",database[7]);
                lkad.put("image",database[8]);
                lkad.put("status","P");

                savdri.updateChildren(lkad);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//    final DatabaseReference grpxx=FirebaseDatabase.getInstance().getReference().child("CUSTOMERS").child(user.getUid()).child("YOUR COMPLAINTS").child(database[1]);
//    grpxx.addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//    });
//






















}





























long cnt;
   String payini;String vsl;
    @Override
    public void onPaymentSuccess(String s) {
 vsl="";
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("CUSTOMERS").child(user.getUid()).child("DONATIONS");
        mUserDB.addListenerForSingleValueEvent(new
                                                       ValueEventListener() {
                                                           @Override
                                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                   if(!snapshot.exists()){
                                                                       String ret=null;
                                                                       storepayvalue(ret);return;}

                                                                   else{
                                                                   cnt = snapshot.getChildrenCount();
                                                                   ArrayList<Object> mp= (ArrayList<Object>) snapshot.getValue();
                                                                   Object h=mp.get(1);

                                                                   String ggg=h.toString();

                                                                   payini=ggg.substring(8);
                                                                   System.out.println(payini);
                                                                   for(int c=0;c<payini.length();c++){
                                                                       if(Character.isDigit(payini.charAt(c))){
                                                                           vsl=vsl+payini.charAt(c);
                                                                       }

                                                                   }
                                                                   System.out.println(vsl);
                                                                   storepayvalue(vsl);}






                                                           }

                                                           @Override
                                                           public void onCancelled(@NonNull DatabaseError error) {

                                                           }
                                                       });









        Toast toast = Toast.makeText(getApplicationContext(),
                "Dear " + name.getText().toString() + " , Thank you for your great generosity! We, at CATTLE CARE, greatly appreciate your donation, and your sacrifice. You Can see your donations under profile section.",
                Toast.LENGTH_LONG);

        toast.show();
        Intent sgg=new Intent(custmapActivity.this,custmapActivity.class);
        startActivity(sgg);
    }
long hj;
    private void storepayvalue(String vsl) {
       if(vsl!=null){

        final EditText amount = findViewById(R.id.etAmount);
        String fin=amount.getText().toString();
        long fg=Long.valueOf(vsl);
       hj=fg+Long.valueOf(fin);
        System.out.println(hj);}
       else
       {
           final EditText amount = findViewById(R.id.etAmount);
           String fin=amount.getText().toString();

          hj=Long.valueOf(fin);
       }


        final DatabaseReference str = FirebaseDatabase.getInstance().getReference().child("CUSTOMERS").child(user.getUid()).child("DONATIONS").child(String.valueOf(1));
        str.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> userMap = new HashMap<>();


                userMap.put("Amount", String.valueOf(hj));

                str.updateChildren(userMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout,menu);
        return true;
    }
    public void logout(MenuItem item) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(custmapActivity.this);
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
    public void onBackPressed() {
        Intent ll=new Intent(custmapActivity.this,MainActivity.class);
        startActivity(ll);
        super.onBackPressed();
    }
}

