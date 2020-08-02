package com.example.cattlecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DriverLogin extends AppCompatActivity {
EditText mPhoneNumber,mCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    String mVerificationId;
Boolean vrf;
    ProgressDialog progressDialog;

    public void sendverificationcode(View v) {
        final String cfg = "+91" + mPhoneNumber.getText().toString();
         vrf = false;

if(mVerificationId!=null)
{
    verifyPhoneNumberWithCode();
    return;
}
progressDialog.show();

        DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("DRIVER");
        df.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dts : snapshot.getChildren()) {
                    Map<String, String> pl = (Map<String, String>) dts.getValue();

                    String number = pl.get("phone");
                    if (number.equals(cfg)) {
                        vrf = true;
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Enter Password",
                                Toast.LENGTH_SHORT);
                        mCode.setAlpha(1);
                        pass.setAlpha(1);


                        progressDialog.hide();
                        log.setText("LOGIN");


                        toast.show();
                        start();
                    }

                }

                if (vrf == false)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "NOT A REGISTERED DRIVER",
                            Toast.LENGTH_SHORT);

                    toast.show();
                    Intent l=new Intent(DriverLogin.this,MainActivity.class);
                    startActivity(l);
                    progressDialog.hide();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    MaterialButton log;

    private void start() {
        if (mVerificationId != null) {
            verifyPhoneNumberWithCode();
        } else {
            startPhoneNumberVerification();
        }
    }
    TextInputLayout pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        FirebaseApp.initializeApp(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        log=findViewById(R.id.login);
        log.setText(" Send Otp");
         pass=findViewById(R.id.passlayout);
        pass.setAlpha(0);
        userIsLoggedIn();

        mPhoneNumber = findViewById(R.id.phoneNumber);

        mCode = findViewById(R.id.code);
        mCode.setAlpha(0);




        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
            }


            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);

                mVerificationId = verificationId;

            }
        };

    }

    private void verifyPhoneNumberWithCode() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mCode.getText().toString());

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user != null) {
                        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("DRIVER").child(user.getUid());
                        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("phone", user.getPhoneNumber());

                                    mUserDB.updateChildren(userMap);
                                }
                                userIsLoggedIn();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }

            }
        });
    }

    private void userIsLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            startActivity(new Intent(getApplicationContext(), DriverActivity.class));
            finish();
            return;
        }
    }

    private void startPhoneNumberVerification() {


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mPhoneNumber.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }



}
