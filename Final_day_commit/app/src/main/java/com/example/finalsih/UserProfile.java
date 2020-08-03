package com.example.finalsih;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UserProfile extends Fragment {


TextView name,phone,amt,pnt;LottieAnimationView congi;
    ImageView tk;int limit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_user_profile, container, false);
        congi=view.findViewById(R.id.congi);
//        tk=view.findViewById(R.id.tjanks);


        limit=50;





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
                chk();


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
    private  void chk(){
        if(points>=limit){

            congi.setAlpha((float)1);

            AlertDialog.Builder ab=new AlertDialog.Builder(getActivity());

            ImageView tkk=new ImageView(getActivity());

            ab.setView(tkk);
            ab.setTitle("CONGRATULATIONS ON WINNING YOUR FIRST COUPON!");
            tkk.setImageResource(R.drawable.tjanks);
            ab.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    congi.setAlpha((float)0);


                }
            });
            ab.setCancelable(true);


            AlertDialog dr=ab.create();
            dr.show();






            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("CUSTOMERS").child(user.getUid());
            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("limit","125");

                    mref.updateChildren(userMap);}




                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Toast toast = Toast.makeText(getActivity(),
                    "CONGRATULATIONS:- You have won a Discount Coupon. Please take a screenshot of the screen and show at XYZ shop to redeem it.",Toast.LENGTH_LONG);

            toast.show();



        }


    }
}