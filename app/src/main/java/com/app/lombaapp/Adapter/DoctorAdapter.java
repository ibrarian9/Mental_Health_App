package com.app.lombaapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lombaapp.ChatDoc;
import com.app.lombaapp.Model.UserDetails;
import com.app.lombaapp.R;

import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder> {

    Context context;
    private final ArrayList<UserDetails> list;

    public DoctorAdapter(Context context, ArrayList<UserDetails> list) {
        this.context = context;
        this.list = list;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DoctorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_doctor, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.MyViewHolder holder, int position) {
        UserDetails uDetail = list.get(position);

        holder.tvRole.setText(uDetail.getRole());
        if (uDetail.getRole().equals("user")){
            holder.poto.setImageResource(R.drawable.user_profile);
        } else {
            holder.poto.setImageResource(R.drawable.profile_doc);
        }
        holder.tvName.setText(uDetail.getUserName());
        holder.rl.setOnClickListener(v -> {
            Intent i = new Intent(context, ChatDoc.class);
            i.putExtra("nama", uDetail.getUserName());
            i.putExtra("uid", uDetail.getUserId());
            i.putExtra("role", uDetail.getRole());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl;
        TextView tvName, tvRole;
        ImageView poto;
        public MyViewHolder(@NonNull View v) {
            super(v);

            poto = v.findViewById(R.id.foto);
            tvName = v.findViewById(R.id.tvName);
            tvRole = v.findViewById(R.id.tvRole);
            rl = v.findViewById(R.id.rl);
        }
    }
}
