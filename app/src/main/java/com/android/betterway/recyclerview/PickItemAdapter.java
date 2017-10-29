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
    private SelectItem mSelectItem;
    /**
     * 内部ViewHolder类
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView address;
        View mainView;
         ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.pickitem_name);
            address = (TextView) itemView.findViewById(R.id.pickitem_address);
            mainView = itemView;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int i = position;
        LocationItemBean locationItemBean = mLocationItemBeanList.get(i);
        holder.name.setText(locationItemBean.getName());
        holder.address.setText(locationItemBean.getAddress());
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mSelectItem) {
                    mSelectItem.select(v, i);
                }
            }
        });
    }

    public PickItemAdapter(List<LocationItemBean> locationItemBeans) {
        mLocationItemBeanList = locationItemBeans;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_pick_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mLocationItemBeanList.size();
    }
    public void setSelectItem(SelectItem selectItem) {
        mSelectItem = selectItem;
    }

    /**
     * 在活动中实现的接口
     */
    public interface SelectItem {
        /**
         * 在活动中定义的方法
         * @param view view对象
         * @param position item的位置
         */
        void select(View view, int position);
    }
}
