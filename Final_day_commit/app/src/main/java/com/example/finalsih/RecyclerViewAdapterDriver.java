package com.example.finalsih;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class RecyclerViewAdapterDriver extends RecyclerView.Adapter<RecyclerViewAdapterDriver.MyViewHolder> {
    private Context mContext;
    private List<Complaint> mData;

    public RecyclerViewAdapterDriver(Context mContext, List<Complaint> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(mContext).inflate(R.layout.comp_card_view,parent,false);
        final RecyclerViewAdapterDriver.MyViewHolder vHolder=new RecyclerViewAdapterDriver.MyViewHolder(v);

        vHolder.req_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,String.valueOf(mData.get(vHolder.getAdapterPosition()).getID()),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(mContext,PendReqDisp.class);
                intent.putExtra("id",String.valueOf(mData.get(vHolder.getAdapterPosition()).getID()));
                intent.putExtra("address",String.valueOf(mData.get(vHolder.getAdapterPosition()).getAddress()));
                mContext.startActivity(intent);
            }
        });
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_id.setText(mData.get(position).getID());
        holder.tv_address.setText(mData.get(position).getAddress());
        holder.tv_status.setText(mData.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private MaterialCardView req_view;
        private TextView tv_id;
        private TextView tv_address;
        private  TextView tv_status;



        public MyViewHolder(View itemView){
            super(itemView);

            tv_id=(TextView)itemView.findViewById(R.id.tvID);
            tv_address=(TextView)itemView.findViewById(R.id.tvAddress);
            tv_status=(TextView)itemView.findViewById(R.id.tvStatus);
            req_view=(MaterialCardView) itemView.findViewById(R.id.complaintCard);


        }
    }

}
