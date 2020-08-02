package com.example.cattlecare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    Context mContext;
    List<Complaint> mData;

    public RecyclerViewAdapter(Context mContext, List<Complaint> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.comp_card_view, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_id;
        private TextView tv_address;
        private TextView tv_status;
        private ImageView iv_status;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_id = (TextView) itemView.findViewById(R.id.tvID);
            tv_address = (TextView) itemView.findViewById(R.id.tvAddress);
            tv_status = (TextView) itemView.findViewById(R.id.tvStatus);


        }
    }
}
