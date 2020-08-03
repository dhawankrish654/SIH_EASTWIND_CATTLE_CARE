package com.example.finalsih;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PendingFrag extends Fragment {

    private RecyclerView mRecyclerViewPend;
    private List<Complaint> pendComp;
    private SwipeRefreshLayout pendSwipe;
    private RecyclerViewAdapterDriver pendRecyclerViewAdapter;


    String customer[];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending, container, false);
        MaterialButton ff=view.findViewById(R.id.navi);
        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nearestpickup();

            }
        });






        mRecyclerViewPend = (RecyclerView) view.findViewById(R.id.pend_recview);
        pendRecyclerViewAdapter = new RecyclerViewAdapterDriver(getContext(), pendComp);
        mRecyclerViewPend.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewPend.setAdapter(pendRecyclerViewAdapter);
        customer=new String[10];
        pendComp.clear();
        pendSwipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_pend);
        pendSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pendRecyclerViewAdapter.notifyDataSetChanged();
                pendSwipe.setRefreshing(false);
            }
        });




        return view;
    }
    public void getlocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
    FusedLocationProviderClient client;
ProgressDialog progressDialog;Double crntlat,crntlong;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
progressDialog=new ProgressDialog(getActivity());
progressDialog.setMessage("Please Wait..");
progressDialog.setCancelable(false);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getlocation();

        } else
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        getlocation();


//ek bar refresh mein aaye sb

        pendComp = new ArrayList<>();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String chkid=user.getUid();
        DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("DRIVER").child(chkid).child("ASSIGNED");

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dts: snapshot.getChildren()){
                    Map<String,String> pl=(Map<String,String>)dts.getValue();
                    System.out.println(dts.getValue());

                    if(pl.get("status").charAt(0)!='P'){
                        continue;
                    }

                    customer[0]=pl.get("address");
                    customer[1]=pl.get("contact");
                    customer[2]=pl.get("image");
                    customer[3]=pl.get("msg");
                    customer[4]=pl.get("name");
                    customer[5]=pl.get("psid");
                    customer[6]="PENDING";
                    customer[7]=pl.get("uid");
                    customer[8]=pl.get("ulang");
                    customer[9]=pl.get("ulat");

                    System.out.println(customer[5]);
                    for(int y=0;y<10;y++)
                        System.out.print(customer[y]);
                    System.out.println();
                    int flffg = 0;
                    for (int po = 0; po < pendComp.size(); po++) {
                        if (Integer.valueOf(pendComp.get(po).getID()) == Integer.valueOf(customer[5]))
                            flffg = 1;
                    }
                    if (flffg == 0) {

                        pendComp.add(new Complaint(customer[5],customer[0],customer[6]));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast toast = Toast.makeText(getActivity(),
                "PULL DOWN ONCE TO REFRESH",
                Toast.LENGTH_SHORT);

        toast.show();

    }


double chkdis=1000000000000000000000.00;
    String senddata[];
    public void nearestpickup(){
        senddata=new String[3];
        DatabaseReference df=FirebaseDatabase.getInstance().getReference().child("PICKUPOINT");
        df.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dts: snapshot.getChildren()) {
                    Map<String, String> pl = (Map<String, String>) dts.getValue();
                    Double laat=Double.valueOf(pl.get("lat"));
                    Double loomg=Double.valueOf(pl.get("long"));
                        Location loc1 = new Location("");

                    loc1.setLatitude(Double.valueOf(crntlat));
                    loc1.setLongitude(Double.valueOf(crntlong));

    Location loc2 = new Location("");
                    loc2.setLatitude(Double.valueOf(laat));
                    loc2.setLongitude(Double.valueOf(loomg));

    double distanceInMeters = loc1.distanceTo(loc2);
    if(distanceInMeters<=chkdis){
        senddata[0]=pl.get("name");
        senddata[1]=pl.get("lat");
        senddata[2]=pl.get("long");
    }


                }
                String url = "http://maps.google.com/maps?daddr=" + senddata[1] + "," + senddata[2];


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
//        Intent intent=new Intent(getActivity(),MainActivity2.class);
//
//        intent.putExtra("lat",Double.valueOf(senddata[1]));
//
//
//        intent.putExtra("lon",Double.valueOf(senddata[2]));
//        intent.putExtra("name",senddata[0]) ;
//        intent.putExtra("address"," ");
//        intent.putExtra("number"," ");
//        intent.putExtra("ids"," ");
//
//        startActivity(intent);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });













































    }
}
