package com.android.betterway.showscheduleactivity.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.betterway.R;
import com.android.betterway.data.NewPlan;
import com.android.betterway.recyclerview.ShowScheduleAdapter;
import com.android.betterway.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleDetailFragment extends Fragment {


    @BindView(R.id.show_schedule_recyclerview)
    RecyclerView mShowScheduleRecyclerview;
    Unbinder unbinder;
    ArrayList<NewPlan> mNewPlanList;

    public ScheduleDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        mNewPlanList = this.getArguments().getParcelableArrayList("NewPlanList");
        LogUtil.i("ScheduleDetailFragment", mNewPlanList.size()+ "个");
        ShowScheduleAdapter showScheduleAdapter = new ShowScheduleAdapter(mNewPlanList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mShowScheduleRecyclerview.setLayoutManager(linearLayoutManager);
        mShowScheduleRecyclerview.setAdapter(showScheduleAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
