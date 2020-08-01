package com.example.cattlecare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cattlecare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


public class UserProfile extends Fragment {


TextView name,phone,amt,pnt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_user_profile, container, false);


        name=view.findViewById(R.id.namehere);
        phone=view.findViewById(R.id.phone);
        amt=view.findViewById(R.id.amt);
        pnt=view.findViewById(R.id.pnt);
        return view;
    }
    int amounts,points;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        amounts=0;
         points=0;

        DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("COMPLAINTS");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String chkid=user.getUid();

        mref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dts: snapshot.getChildren()){
                    final Map<String,String> mp=(Map<String,String>)dts.getValue();

                    final String statuscc=mp.get("status");
                    if(statuscc.charAt(0)=='P')
                        continue;

                    if(chkid.equals(mp.get("uid"))) {
                        points = points + Integer.valueOf(mp.get("rewards"));


                    }


                }
                pnt.setText(String.valueOf(points));




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference refdf= FirebaseDatabase.getInstance().getReference().child("CUSTOMERS").child(chkid).child("DONATIONS").child("1").child("Amount");
        refdf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

               amt.setText((String)snapshot.getValue());
                }
                else amt.setText("0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference xxx=FirebaseDatabase.getInstance().getReference().child("CUSTOMERS").child(chkid);
        xxx.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> rr=(Map<String,String>) snapshot.getValue();
                name.setText(rr.get("name"));
                phone.setText(rr.get("phone"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


















    }
}
