package com.android.betterway.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.betterway.R;
import com.android.betterway.data.MyTime;
import com.android.betterway.data.Schedule;
import com.android.betterway.utils.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<Schedule> mScheduleList;
    private Agency mAgency;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.schedule_start_time)
        TextView startTime;
        @BindView(R.id.schedule_city)
        TextView city;
        @BindView(R.id.location_list)
        TextView locationList;
        @BindView(R.id.schedule_spend_time)
        TextView spendTime;
        @BindView(R.id.schedule_spend_money)
        TextView spendMoney;
        @BindView(R.id.click_to_details)
        TextView clickToDetails;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ScheduleAdapter(List<Schedule> list) {
        mScheduleList = list;
    }

    public void setAgency(Agency agency) {
        mAgency = agency;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int count = position;
        Schedule schedule = mScheduleList.get(position);
        MyTime startmytime = TimeUtil.longToTotalMyTime(schedule.getStartTime());
        String starttime = startmytime.getYear() + '/' + startmytime.getMonth() + '/'
                + startmytime.getDay() + ' '+ startmytime.getSingleTime();
        holder.startTime.setText(starttime);
        holder.city.setText(schedule.getCity());
        holder.locationList.setText(schedule.getLocation());
        holder.spendTime.setText(TimeUtil.minteintToString(schedule.getSpendTime()));
        holder.spendMoney.setText(schedule.getSpendMoney());
        holder.clickToDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAgency != null) {
                    mAgency.toDetails(count);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mScheduleList.size();
    }

    public interface Agency {
        /**
         * 代理的接口函数
         * @param position item的位置
         */
        void toDetails(int position);
    }
}
