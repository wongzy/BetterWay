package com.android.betterway.autoscheduleactivity.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.betterway.R;
import com.android.betterway.autoscheduleactivity.daggerneed.AutoScheduleMainFragmentComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.AutoSchedulePresenterImpelModule;
import com.android.betterway.autoscheduleactivity.daggerneed.DaggerAutoScheduleMainFragmentComponent;
import com.android.betterway.autoscheduleactivity.present.AutoSchedulePresenterImpel;
import com.android.betterway.itemplandialog.LocationDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AutoScheduleMainFragment extends Fragment implements AutoScheduleView {
    @BindView(R.id.add_start_location)
    Button mAddStartLocation;
    Unbinder unbinder;
    private AutoSchedulePresenterImpel mAutoSchedulePresenterImpel;
    private String searchLocation;
    public static final int TRUECODE = 100;
    public static final int WRONGCODE= 101;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoScheduleMainFragmentComponent autoScheduleMainFragmentComponent =
                DaggerAutoScheduleMainFragmentComponent.builder()
                        .autoSchedulePresenterImpelModule(new AutoSchedulePresenterImpelModule(this))
                        .build();
        mAutoSchedulePresenterImpel = autoScheduleMainFragmentComponent
                .getAutoAutoSchedulePresenterImpel();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLocationEvent(String location) {
        searchLocation = location;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_schedule_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void showRecyclerView() {

    }

    @Override
    public void hideRecyclerView() {

    }

    @Override
    public void notifyRecyclerView() {

    }


    @Override
    public void showButton() {
        mAddStartLocation.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButton() {
        mAddStartLocation.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.add_start_location)
    public void onViewClicked() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        LocationDialogFragment locationDialogFragment = new LocationDialogFragment();
        locationDialogFragment.setSearchLocation(searchLocation);
        locationDialogFragment.show(manager, "locationDialogFragment");
    }
}
