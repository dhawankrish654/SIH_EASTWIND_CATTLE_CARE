package com.example.finalsih;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cattlecare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import static android.os.Build.ID;


public class status_frag extends Fragment {

    private RecyclerView mRecyclerView;
    private List<com.example.finalsih.Complaint> comp;
    List<String> names;private SwipeRefreshLayout swipe;
int cnt;
    public status_frag() {


    }


    Bitmap bmp;String statuscc;ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        final View view= inflater.inflate(R.layout.fragment_status_frag, container,false);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.hist_recview);
        final com.example.finalsih.RecyclerViewAdapter recyclerViewAdapter=new com.example.finalsih.RecyclerViewAdapter(getContext(),comp);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(recyclerViewAdapter);
        comp.clear();
        Toast toast = Toast.makeText(getActivity(),
                "Pull down to refresh.",
                Toast.LENGTH_SHORT);

        toast.show();
        swipe=(SwipeRefreshLayout)view.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

//progressDialog.show();
                DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("COMPLAINTS");
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String chkid=user.getUid();

//                mref.addListenerForSingleValueEvent(new ValueEventListener() {
//
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        progressDialog.hide();
//                        for(DataSnapshot dts: snapshot.getChildren()){
//                           final Map<String,String> mp=(Map<String,String>)dts.getValue();
////                            System.out.println(dts);
////                            System.out.println(chkid);
//                            final String pidcc=mp.get("pid");
//                            final String addrescc=mp.get("address");
//                            statuscc=mp.get("status");
//                            if(statuscc.charAt(0)=='P')
//                                statuscc="PENDING";
//                            else
//                                statuscc="RESOLVED";
//                            if(chkid.equals(mp.get("uid"))){
//                                int flg=0;
//                                for(int po=0;po<comp.size();po++){
//                                    if(Integer.valueOf(comp.get(po).getID())==Integer.valueOf(pidcc))
//                                    {flg=1;break;}
//                                }
//                                if(flg==1)
//                                    continue;
//
//                                else
//                                    comp.add(new Complaint(String.valueOf(Integer.valueOf(pidcc)*393),addrescc,statuscc));
//
//
//
//
//                                Toast toast = Toast.makeText(getActivity(),
//                                        "Pull down once to refresh",
//                                        Toast.LENGTH_SHORT);
//
//                                toast.show();
//
//                            }
//
//
//
//
//
//
//
//
//
//                        }
//
//
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                recyclerViewAdapter.notifyDataSetChanged();
                swipe.setRefreshing(false);

            }
        });



        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comp = new ArrayList<>();
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("COMPLAINTS");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String chkid=user.getUid();

        mref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.hide();
                for(DataSnapshot dts: snapshot.getChildren()){
                    final Map<String,String> mp=(Map<String,String>)dts.getValue();
//                            System.out.println(dts);
//                            System.out.println(chkid);
                    final String pidcc=mp.get("pid");
                    final String addrescc=mp.get("address");
                    statuscc=mp.get("status");
                    if(statuscc.charAt(0)=='P')
                        statuscc="PENDING";
                    else
                        statuscc="RESOLVED";
                    if(chkid.equals(mp.get("uid"))){
                        int flg=0;
                        for(int po=0;po<comp.size();po++){
                            if(Integer.valueOf(comp.get(po).getID())==Integer.valueOf(pidcc))
                            {flg=1;break;}
                        }
                        if(flg==1)
                            continue;

                        else
                            comp.add(new com.example.finalsih.Complaint(String.valueOf(Integer.valueOf(pidcc)*393),addrescc,statuscc));




                        Toast toast = Toast.makeText(getActivity(),
                                "Pull down once to refresh",
                                Toast.LENGTH_SHORT);

                        toast.show();

                    }









                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        String p="945211111111111111111111111111";





    }


}
