package com.agaoglu.tez;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Volkan on 21.03.2017.
 */

public class tetkikholder extends RecyclerView.ViewHolder {

    ImageView tetkikresim;
    TextView tetkikoran;
    TextView hastaad;

    public tetkikholder(View itemView) {
        super(itemView);
        tetkikresim = (ImageView) itemView.findViewById(R.id.tetkikresimview);
        tetkikoran = (TextView) itemView.findViewById(R.id.dibetrate);
        hastaad = (TextView) itemView.findViewById(R.id.tetkikad);
    }
}
