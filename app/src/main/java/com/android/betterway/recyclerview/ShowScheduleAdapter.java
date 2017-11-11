package com.android.betterway.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.betterway.R;
import com.android.betterway.data.NewPlan;
import com.android.betterway.utils.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class ShowScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewPlan> mNewPlanList;
    private static final int LOCATION = 10;
    private static final int ROUTE = 11;
    private static final int BIKE = 1;
    private static final int BUS = 2;
    private static final int CAR = 3;
    private static final int WALK = 4;
    public ShowScheduleAdapter(List<NewPlan> newPlanList) {
        mNewPlanList = newPlanList;
    }

    /**
     * 地点ViewHolder
     */
    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.location_show)
        TextView locationShow;
        @BindView(R.id.statement_show)
        TextView stateementShow;
        @BindView(R.id.time_show)
        TextView timeShow;
        @BindView(R.id.spend_money_show)
        TextView spendMoneyShow;
        @BindView(R.id.spend_time_show)
        TextView spendTimeShow;
        public LocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 路径ViewHolder
     */
    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.way_fee_layout)
        LinearLayout wayFeeLayout;
        @BindView(R.id.fee_show)
        TextView feeShow;
        @BindView(R.id.cost_time_show)
        TextView costTimeShow;
        @BindView(R.id.go_way_show)
        TextView goWayShow;
        public RouteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return LOCATION;
        } else {
            return ROUTE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOCATION) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_location_show, parent, false);
            return new LocationViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_route_show, parent, false);
            return new RouteViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewPlan newPlan = mNewPlanList.get(position);
        if (holder instanceof LocationViewHolder) {
            ((LocationViewHolder) holder).locationShow.setText(newPlan.getLocation());
            if (newPlan.getMoneySpend() != 0) {
                String spendMoney = newPlan.getMoneySpend() + "元";
                ((LocationViewHolder) holder).spendMoneyShow.setText(spendMoney);
            } else {
                ((LocationViewHolder) holder).spendMoneyShow.setVisibility(View.GONE);
            }
            String spendTime = newPlan.getStayMinutes() + "min";
            ((LocationViewHolder) holder).spendTimeShow.setText(spendTime);
            String startTime = TimeUtil.longToTotalMyTime(newPlan.getStartTime()).getSingleTime();
            ((LocationViewHolder) holder).timeShow.setText(startTime);
            if (newPlan.getStatement() != null) {
                ((LocationViewHolder) holder).stateementShow.setText(newPlan.getStatement());
            } else {
                ((LocationViewHolder) holder).stateementShow.setVisibility(View.GONE);
            }
            return;
        }
        if (holder instanceof RouteViewHolder) {
            if (newPlan.getMoneySpend() == 0) {
                ((RouteViewHolder) holder).wayFeeLayout.setVisibility(View.GONE);
            } else {
                ((RouteViewHolder) holder).wayFeeLayout.setVisibility(View.VISIBLE);
                String fee = newPlan.getMoneySpend() + "元";
                ((RouteViewHolder) holder).feeShow.setText(fee);
            }
            switch (newPlan.getType()) {
                case BIKE:
                    ((RouteViewHolder) holder).goWayShow.setText("骑行");
                    break;
                case CAR:
                    ((RouteViewHolder) holder).goWayShow.setText("驾车");
                    break;
                case BUS:
                    ((RouteViewHolder) holder).goWayShow.setText("公交");
                    break;
                case WALK:
                    ((RouteViewHolder) holder).goWayShow.setText("步行");
                    break;
                default:
                    break;
            }
            String costTime = newPlan.getStayMinutes() + "min";
            ((RouteViewHolder) holder).costTimeShow.setText(costTime);
        }
    }

    @Override
    public int getItemCount() {
        return mNewPlanList.size();
    }
}
