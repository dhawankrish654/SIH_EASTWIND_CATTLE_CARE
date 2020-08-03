package com.example.finalsih;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;


public class SupportFrag extends Fragment {
    LinearLayout layout1;
    LinearLayout layout2;
    LinearLayout layout3;
    ImageButton btn1;
    ImageButton btn2;
    ImageButton btn3;
    MaterialCardView card1;
    MaterialCardView card2;
    MaterialCardView card3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        btn1 = (ImageButton) view.findViewById(R.id.hidden_btn1);
        layout1 = (LinearLayout) view.findViewById(R.id.hidden_view1);
        card1 = (MaterialCardView) view.findViewById(R.id.cardView1);
        btn2 = (ImageButton) view.findViewById(R.id.hidden_btn2);
        layout2 = (LinearLayout) view.findViewById(R.id.hidden_view2);
        card2 = (MaterialCardView) view.findViewById(R.id.cardView2);
        btn3 = (ImageButton) view.findViewById(R.id.hidden_btn3);
        layout3 = (LinearLayout) view.findViewById(R.id.hidden_view3);
        card3 = (MaterialCardView) view.findViewById(R.id.cardView3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout1.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(card1, new AutoTransition());
                    layout1.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(card1, new AutoTransition());
                    layout1.setVisibility(View.GONE);
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout2.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(card2, new AutoTransition());
                    layout2.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(card2, new AutoTransition());
                    layout2.setVisibility(View.GONE);
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout3.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(card3, new AutoTransition());
                    layout3.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(card3, new AutoTransition());
                    layout3.setVisibility(View.GONE);
                }
            }
        });
        ImageView cc = view.findViewById(R.id.mailll);
        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail();
            }
        });
        MaterialTextView gg = view.findViewById(R.id.llll);
        gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail();
            }
        });


        return view;
    }

    private void mail() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        String[] recipients = new String[]{"cattlecare@gmail.com", "",};

        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);

        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");

        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

        emailIntent.setType("text/plain");

        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public void calllll(View c) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 100);
            System.out.println("makabhosda");
            return;
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            String num = "9999999999";

            callIntent.setData(Uri.parse("tel:" + num));//change the number
            startActivity(callIntent);
        }


    }
}
