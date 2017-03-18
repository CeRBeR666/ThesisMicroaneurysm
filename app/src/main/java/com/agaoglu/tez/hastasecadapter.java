package com.agaoglu.tez;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by Volkan on 15.03.2017.
 */

public class hastasecadapter extends RecyclerView.Adapter<hastasecholder> {

    List<hasta> list = Collections.emptyList();
    Context context;

    public hastasecadapter(List<hasta> list, Context context){
        this.list = list;
        this.context = context;
    }


    @Override
    public hastasecholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hastaitem, parent, false);
        hastasecholder holder = new hastasecholder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(hastasecholder holder, int position) {
        holder.hastaisim.setText(list.get(position).getIsim());
        holder.hastacinsiyet.setText(list.get(position).getCinsiyet());
        holder.hastadogtar.setText(list.get(position).getDogtar());

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void insert(int position, hasta hasta){
        list.add(position, hasta);
        notifyItemInserted(position);
    }

    public void remove(hasta hasta){
        list.remove(hasta);
    }


}
