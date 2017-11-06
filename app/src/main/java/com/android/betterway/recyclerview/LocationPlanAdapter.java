package com.android.betterway.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.betterway.R;
import com.android.betterway.data.LocationPlan;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class LocationPlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  List<LocationPlan> mLocationPlanList;
    private static final int CONTENT = 1;
    private static final int BOTTOM = 2;
    private AddLocationButton mAddLocationButton;

    /**
     * content类
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.location_name_in_item)
        TextView mLocationNameInItem;
        @BindView(R.id.thing_statement_in_item)
        TextView mThingStatementInItem;
        @BindView(R.id.spend_money_in_item)
        TextView mSpendMoneyInItem;
        @BindView(R.id.spend_time_in_item)
        TextView mSpendTimeInItem;
        @BindView(R.id.start_time_in_item)
        TextView mStartTimeInItem;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setAddLocationButton(AddLocationButton addLocationButton) {
        mAddLocationButton = addLocationButton;
    }

    /**
     * 底部按钮类
     */
    public static class BottomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.add_location_button)
        Button mAddLocationButton;
        BottomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public LocationPlanAdapter() {
        mLocationPlanList = new ArrayList<>();
    }
    /**
     * 根据位置判断item的类型
     *
     * @param position 位置
     * @return 类型
     */
    @Override
    public int getItemViewType(int position) {
        if (position == mLocationPlanList.size()) {
            return BOTTOM;
        } else {
            return CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == BOTTOM) {
            View bottomView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.location_recycler_bottom_item, parent, false);
            return new BottomViewHolder(bottomView);
        } else {
            View contentView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.location_item, parent, false);
            return new ViewHolder(contentView);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            LocationPlan locationPlan = mLocationPlanList.get(position);
            ((ViewHolder) holder).mLocationNameInItem.setText(locationPlan.getLocation());
            if (locationPlan.getStatement() != null) {
                ((ViewHolder) holder).mThingStatementInItem.setText(locationPlan.getStatement());
            } else {
                ((ViewHolder) holder).mThingStatementInItem.setVisibility(View.GONE);
            }
            if (locationPlan.getMoneySpend() != 0) {
                ((ViewHolder) holder).mSpendMoneyInItem.setText(locationPlan.getMoneySpend() + "元");
            } else {
                ((ViewHolder) holder).mSpendMoneyInItem.setVisibility(View.GONE);
            }
            if (locationPlan.getStayMinutes() != 0) {
                String stayMinutes = locationPlan.getStayMinutes() + "分钟";
                ((ViewHolder) holder).mSpendTimeInItem.setText(stayMinutes);
            } else {
                ((ViewHolder) holder).mSpendTimeInItem.setVisibility(View.GONE);
            }
            if (locationPlan.getStartTime() != null) {
                ((ViewHolder) holder).mStartTimeInItem.setText(locationPlan.getStartTime().getSingleTime());
            } else {
                ((ViewHolder) holder).mStartTimeInItem.setVisibility(View.GONE);
            }
        }
        if (holder instanceof BottomViewHolder) {
            ((BottomViewHolder) holder).mAddLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAddLocationButton != null) {
                        mAddLocationButton.buttonClick();
                    }
                }
            });
        }
    }

    /**
     * button实现的接口
     */
    public interface AddLocationButton {
        /**
         * 由外部实现的接口函数
         */
        void buttonClick();
    }

    @Override
    public int getItemCount() {
        return mLocationPlanList.size() + 1;
    }

    /**
     * 增加地点
     * @param locationPlan 增加的地点
     */
    public void addLocationPlan(LocationPlan locationPlan) {
        mLocationPlanList.add(locationPlan);
        notifyItemInserted(mLocationPlanList.size());
    }

    /**
     * 返回是否为第一个地点
     * @return 判断
     */
    public boolean getIsFirst() {
        return mLocationPlanList.size() == 0;
    }

    /**
     * 移除地点
     * @param position 需要移除地点的位置
     */
    public void removeLocationPlan(int position) {
        mLocationPlanList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 移除所有的地点
     */
    public void removeAllLocationPlan() {
        int i = mLocationPlanList.size();
        mLocationPlanList.clear();
        for (int j = 0; j <= i; j++) {
            notifyItemRemoved(j);
        }
    }

}
