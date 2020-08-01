package com.example.cattlecare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cattlecare.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class fundFrag extends Fragment implements  PaymentResultListener {


    View donateView, payView;
    MaterialButton btnDonate, paybtn;
    ImageButton btnCancel;
    EditText name, number, amount;
    String paisa;




    public void payxxx() {
        paisa = amount.getText().toString();
        startPayment();
    }

    public void startPayment() {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */


        /**
         * Reference to current activity
         */
        final Activity activity = getActivity();

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "GAUMATA");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Reference No. #123456");

            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            String ftc=String.valueOf(Integer.valueOf(paisa)*100);
            options.put("amount", ftc);

            checkout.open(activity, options);
        } catch(Exception e) {
            System.out.println("Error in starting Razorpay Checkout"+ e);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fund, container, false);
        btnDonate = (MaterialButton) view.findViewById(R.id.btndonate);
        btnCancel = (ImageButton) view.findViewById(R.id.btnCancel);
        name = view.findViewById(R.id.etUserName);
        number = view.findViewById(R.id.etMob);
        amount = view.findViewById(R.id.etAmount);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("CUSTOMERS").child(user.getUid());
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> usemap = (Map<String, String>) snapshot.getValue();
                number.setText(usemap.get("phone"));
                name.setText(usemap.get("name"));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        paybtn = view.findViewById(R.id.btnPay);
        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payxxx();
            }
        });
        donateView = (View) view.findViewById(R.id.donateView);
        payView = (View) view.findViewById(R.id.payView);

        Checkout.preload(container.getContext());

        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUp(payView);
                donateView.setVisibility(View.INVISIBLE);
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideDown(payView);
                donateView.setVisibility(View.VISIBLE);
            }
        });


        return view;

    }


    public void slideUp(View v) {
        v.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(0, 0, v.getHeight(), 0);
        animation.setDuration(500);
        v.startAnimation(animation);
    }

    public void slideDown(View v) {
        v.setVisibility(View.INVISIBLE);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, v.getHeight());
        animation.setDuration(500);
        v.startAnimation(animation);
    }

    long cnt;

    @Override
    public void onPaymentSuccess(String s) {

    }

    @Override
    public void onPaymentError(int i, String s) {

    }


//    @Override
//    public void onPaymentSuccess(String s) {
//
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("CUSTOMERS").child(user.getUid()).child("DONATIONS");
//        mUserDB.addListenerForSingleValueEvent(new
//                                                       ValueEventListener() {
//                                                           @Override
//                                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                               if (snapshot.exists())
//                                                                   cnt = snapshot.getChildrenCount();
//
//                                                           }
//
//                                                           @Override
//                                                           public void onCancelled(@NonNull DatabaseError error) {
//
//                                                           }
//                                                       });
//        final DatabaseReference str = FirebaseDatabase.getInstance().getReference().child("CUSTOMERS").child(user.getUid()).child("DONATIONS").child(String.valueOf(cnt + 1));
//        str.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Map<String, Object> userMap = new HashMap<>();
//
//                userMap.put("Amount", paisa);
//                mUserDB.updateChildren(userMap);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        Toast toast = Toast.makeText(getActivity(),
//                "Dear " + name.getText().toString() + " , Thank you for your great generosity! We, at CATTLE CARE, greatly appreciate your donation, and your sacrifice. You Can see your donations under profile section.",
//                Toast.LENGTH_LONG);
//
//        toast.show();
//        donateView.setVisibility(View.VISIBLE);
//
//    }
//
//
//
//    @Override
//    public void onPaymentError(int code, String response) {
//        System.out.println("poiio");
//    }
}
