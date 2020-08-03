package com.example.finalsih;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ResolvedFrag extends Fragment {

    private RecyclerView mRecyclerViewRes;
    private List<Complaint> resComp;
    private SwipeRefreshLayout resSwipe;
    private RecyclerViewAdapter resRecyclerViewAdapter;String customer[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resolved, container, false);
        MaterialButton ff=view.findViewById(R.id.addi);
        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dd=new Intent(getActivity(),PickuppointActivity.class);

                dd.putExtra("active","d");

                startActivity(dd);



            }
        });
        mRecyclerViewRes = (RecyclerView) view.findViewById(R.id.res_recview);
        customer=new String[10];
        resRecyclerViewAdapter = new RecyclerViewAdapter(getContext(), resComp);
        mRecyclerViewRes.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewRes.setAdapter(resRecyclerViewAdapter);

        resSwipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_res);
        resSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {





                resRecyclerViewAdapter.notifyDataSetChanged();
                resSwipe.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast toast = Toast.makeText(getActivity(),
                "PULL DOWN ONCE TO REFRESH",
                Toast.LENGTH_SHORT);

        toast.show();
        resComp = new ArrayList<>();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String chkid=user.getUid();
        DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("DRIVER").child(chkid).child("ASSIGNED");

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dts: snapshot.getChildren()){
                    Map<String,String> pl=(Map<String,String>)dts.getValue();
                    System.out.println(dts.getValue());

                    if(pl.get("status").charAt(0)!='R'){
                        continue;
                    }

                    customer[0]=pl.get("address");
                    customer[1]=pl.get("contact");
                    customer[2]=pl.get("image");
                    customer[3]=pl.get("msg");
                    customer[4]=pl.get("name");
                    customer[5]=pl.get("psid");
                    customer[6]="RESOLVED";
                    customer[7]=pl.get("uid");
                    customer[8]=pl.get("ulang");
                    customer[9]=pl.get("ulat");

                    System.out.println(customer[5]);
                    for(int y=0;y<10;y++)
                        System.out.print(customer[y]);
                    System.out.println();
                    int flffg = 0;
                    for (int po = 0; po < resComp.size(); po++) {
                        if(resComp.get(po).getID()!=null && customer[5]!=null){
                        if (Integer.valueOf(resComp.get(po).getID()) == Integer.valueOf(customer[5]))
                            flffg = 1;
                    }}
                    if (flffg == 0) {

                        resComp.add(new Complaint(customer[5],customer[0],customer[6]));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}