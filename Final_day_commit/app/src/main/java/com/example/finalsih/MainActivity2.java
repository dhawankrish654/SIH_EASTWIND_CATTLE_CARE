package com.example.finalsih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {
    // variables for adding location layer
    private MapView mapView;
    private MapboxMap mapboxMap;
    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    // variables needed to initialize navigation
    private Button button;
    private Point destination;
    public static final String NAME = "name";
    public static final String SERIAL_NO = "serial_number";
    String name, serialNo;
    Double latitude, longitude;
    TextView uName, contact, dateTime, pincode, address, message, status;

    Button getDirection;
    Button markAsResolved;
    String statusValue = "0";
    ProgressDialog progressDialog;

    public void resolverequest(View gg){





        final EditText ip=new EditText(MainActivity2.this);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity2.this);
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



final String id=getIntent().getStringExtra("ids");
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
                potty.put("rewards","5");
                potty.put("collected",val);

                muserdb.updateChildren(potty);
                SmsManager smsManager= SmsManager.getSmsManagerForSubscriptionId(0);
                int pll=Integer.valueOf(id)*393;

                smsManager.sendTextMessage( nj.getText().toString(),null,"CATTLE CARE MANAGEMENT-Your request id "+pll+" has been resolved and your rewards have been added.",null,null);
                smsManager.sendTextMessage(nj.getText().toString(),null,"Please do give us your valuable feedback and donate and report more for amazing rewards",null,null);


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
            Intent xx=new Intent(MainActivity2.this,DriverActivity.class);
            startActivity(xx);

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});








    }



















    private final int CALL_REQUEST = 100;
    public void calllll(View c){
        if (ActivityCompat.checkSelfPermission(MainActivity2.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);
            System.out.println("makabhosda");
            return;
        }
        else{
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            String num=nj.getText().toString();
            callIntent.setData(Uri.parse("tel:"+num));//change the number
            startActivity(callIntent);
        }


    }














    TextView nj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        Bundle b=getIntent().getExtras();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
//
//        Double lat=b.get("lon"));
//
//        Double lon=b.get("lat"));     System.out.println("check  coord"+lat+" "+lon);

        setContentView(R.layout.activity_main2);
        mapView = findViewById(R.id.mapView);
        TextView nm=findViewById(R.id.naam);
        nm.setText(getIntent().getStringExtra("name"));
        TextView mm=findViewById(R.id.address);
        mm.setText(getIntent().getStringExtra("address"));
         nj=findViewById(R.id.number);
        nj.setText(getIntent().getStringExtra("number"));






        Toast.makeText(this,"Click On Screen Once to get location then start navigation",Toast.LENGTH_LONG).show();







        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }
//    public void loadComplainDetail() {
//
//
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.complainDetail + "?sno=" + serialNo, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                progressDialog.hide();
//                try {
//                    JSONObject object = new JSONObject(response);
//                    boolean error = object.optBoolean("error");
//                    if (!error) {
//
//                        JSONObject jsonObject = object.getJSONObject("msg");
//
//                        serialNo = jsonObject.optString("sno");
//                        dateTime.setText("Date Time:- " + jsonObject.getString("datetime"));
//                        uName.setText("Name:- " + jsonObject.getString("name"));
//                        contact.setText("Contact:- " + jsonObject.optString("contact"));
//
//
//                        pincode.setText("Pincode:- " + jsonObject.optString("pincode"));
//                        address.setText("Address:- " + jsonObject.getString("address"));
//                        message.setText("Message:- " + jsonObject.getString("message"));
//                        latitude = jsonObject.optDouble("latitude");
//                        longitude = jsonObject.optDouble("longitude");
//                        //status.setText(jsonObject.getString("status"));
//                        int sta = Integer.parseInt(jsonObject.getString("status"));
//                        if (sta == 0) {
//                            status.setText("Status:- Pending");
//
//                        } else if (sta == 1) {
//                            status.setText("Status:- Resolved");
//
//                        }
//                    }
//                } catch (JSONException ex) {
//                    Toast.makeText(getApplicationContext(), "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.hide();
//                Toast.makeText(getApplicationContext(), "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
//        requestQueue.add(stringRequest);
//    }
































    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        Point des=this.destination;

        mapboxMap.setStyle(getString(R.string.navigation_guidance_night), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);

                addDestinationIconSymbolLayer(style);
                mapboxMap.addOnMapClickListener(MainActivity2.this);
                button = findViewById(R.id.startButton);



                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean simulateRoute = true;
                            NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                    .directionsRoute(currentRoute)
                                    .shouldSimulateRoute(simulateRoute)
                                    .build();
                            // Call this method with Context from within an Activity
                            NavigationLauncher.startNavigation(MainActivity2.this, options);
                        }
                    });
            }
        });
    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {


        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }

    @SuppressWarnings( {"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        Bundle b=getIntent().getExtras();
        Point destinationPoint = Point.fromLngLat(b.getDouble("lon"),b.getDouble("lat"));
        System.out.println("onclick "+b.getDouble("lon")+b.getDouble("lat"));
        System.out.println("onclick "+b.getDouble("lon")+b.getDouble("lat"));
        System.out.println("onclick "+b.getDouble("lon")+b.getDouble("lat"));
        System.out.println("onclick "+b.getDouble("lon")+b.getDouble("lat"));
        System.out.println("onclick "+b.getDouble("lon")+b.getDouble("lat"));
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }

        DirectionsRoute togo=getRoute(originPoint, destinationPoint);
        button.setEnabled(true);
        button.setBackgroundResource(R.color.colorAccent);
        return true;
    }

    private DirectionsRoute getRoute(Point origin, Point destination) {
        Toast.makeText(this,"Click on Start Navigation",Toast.LENGTH_LONG).show();
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);


                        navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        if(currentRoute==null)
                        {
                            Toast tos = Toast.makeText(getApplicationContext(),
                                    "No Cureent Route Available",
                                    Toast.LENGTH_SHORT);
                            tos.show();
                            return;
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
        return currentRoute;
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }



    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(this, "R.string.user_location_permission_not_granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public Boolean checkPermission(String permission){
        int check= ContextCompat.checkSelfPermission(this,permission);
        return  (check== PackageManager.PERMISSION_GRANTED);

    }


}
