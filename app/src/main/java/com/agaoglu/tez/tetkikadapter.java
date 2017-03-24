package com.agaoglu.tez;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Volkan on 21.03.2017.
 */

public class tetkikadapter extends RecyclerView.Adapter<tetkikholder> {

    private Context context;
    private List<tetkik> tetkikList;

    public tetkikadapter(List<tetkik> tetkikList, Context context) {
        this.context = context;
        this.tetkikList = tetkikList;
    }

    @Override
    public tetkikholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tetkikitem, parent, false);
        tetkikholder holder = new tetkikholder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(tetkikholder holder, int position) {
        tetkik tetkik = tetkikList.get(position);
        Glide.with(context).load(tetkik.gettetkikPath()).into(holder.tetkikresim);
        holder.tetkikresim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("dürtüldü","birisi resmi dürttü");
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.tetkikList.size();
    }

    public void insert(int position, tetkik tetkik){
        tetkikList.add(position, tetkik);
        notifyItemInserted(position);
    }
}
