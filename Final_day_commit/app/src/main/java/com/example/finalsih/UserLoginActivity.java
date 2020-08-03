package com.example.finalsih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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

public class UserLoginActivity extends AppCompatActivity {
    LinearLayout otpLayout;
    MaterialButton otpBtn;
    MaterialButton loginBtn;


    public void sendverificationcode(View v) {
            if (mVerificationId != null) {
                verifyPhoneNumberWithCode();
            } else {

                startPhoneNumberVerification();
            }
        }

        private EditText mPhoneNumber, mCode, mname;
        private Button mSend;

        private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

        String mVerificationId;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_login);


            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);


            otpLayout=findViewById(R.id.otp_layout);
            otpBtn=findViewById(R.id.otp_btn);
            loginBtn=findViewById(R.id.login_btn);
            otpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    otpBtn.setVisibility(View.GONE);
                    otpLayout.setVisibility(View.VISIBLE);
                    sendverificationcode(view);
                }
            });

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendverificationcode(view);

                }
            });


            FirebaseApp.initializeApp(this);

            userIsLoggedIn();

            mPhoneNumber = findViewById(R.id.phoneNumber);
            mname = findViewById(R.id.name);
            mCode = findViewById(R.id.code);




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

        private void signInWithPhoneAuthCredential(final PhoneAuthCredential phoneAuthCredential) {
            progressDialog.show();
            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
progressDialog.hide();
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user != null) {
                            final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("CUSTOMERS").child(user.getUid());
                            mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put("phone", user.getPhoneNumber());
                                        userMap.put("name", mname.getText().toString());
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
    ProgressDialog progressDialog;
        private void userIsLoggedIn() {
            progressDialog.show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {

                startActivity(new Intent(getApplicationContext(), custmapActivity.class));
                progressDialog.hide();
                finish();
                return;
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Login To Continue",
                        Toast.LENGTH_SHORT);

                toast.show();
                progressDialog.hide();
            }
        }

        private void startPhoneNumberVerification() {
progressDialog.show();
if(mPhoneNumber.getText().toString().trim().length()!=10 || mname.getText().toString().length()<3)
{Toast toast = Toast.makeText(getApplicationContext(),
        "INVALID DETAILS",
        Toast.LENGTH_SHORT);

    toast.show();
    progressDialog.hide();
    Intent ll=new Intent(UserLoginActivity.this,UserLoginActivity.class);
    startActivity(ll);

    return;
}
progressDialog.hide();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + mPhoneNumber.getText().toString(),
                    60,
                    TimeUnit.SECONDS,
                    this,
                    mCallbacks);
        }



}

