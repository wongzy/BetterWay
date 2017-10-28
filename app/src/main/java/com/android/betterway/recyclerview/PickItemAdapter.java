package com.android.betterway.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.betterway.R;
import com.android.betterway.data.LocationItemBean;


import java.util.List;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class PickItemAdapter extends RecyclerView.Adapter<PickItemAdapter.ViewHolder> {
    private List<LocationItemBean> mLocationItemBeanList;

    /**
     * 内部ViewHolder类
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView address;

         ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.pickitem_name);
            address = (TextView) itemView.findViewById(R.id.pickitem_address);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LocationItemBean locationItemBean = mLocationItemBeanList.get(position);
        holder.name.setText(locationItemBean.getName());
        holder.address.setText(locationItemBean.getAddress());
    }

    public PickItemAdapter(List<LocationItemBean> locationItemBeans) {
        mLocationItemBeanList = locationItemBeans;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_pick_item, parent);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mLocationItemBeanList.size();
    }
}
