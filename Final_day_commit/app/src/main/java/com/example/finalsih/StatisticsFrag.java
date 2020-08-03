package com.example.finalsih;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class StatisticsFrag extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_statistics, container, false);
//
//        final TextView cow=view.findViewById(R.id.textView);
//        final TextView mone=view.findViewById(R.id.textView1);
//        cow.setText("254");
//        mone.setText("1133");
//for(int x=0;x<200;x++)
//{
//    final int tes=Integer.valueOf(x);
//    Timer timer=new Timer();
//    timer.schedule(new TimerTask() {
//        @Override
//        public void run() {
//            cow.setText(String.valueOf(tes));
//
//
//
//        }
//    },1000);
//
//
//    continue;
//
//}
//        for(int x=0;x<2000;x++)
//        {
//            final int tes=Integer.valueOf(x);
//            Timer timer=new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    mone.setText(String.valueOf(tes));
//
//
//
//                }
//            },100);
//
//
//            continue;
//
//        }
















        return view;

    }
}