package com.android.betterway.itempickeractivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.android.betterway.R;
import com.android.betterway.data.LocationItemBean;
import com.android.betterway.itemplandialog.LocationDialogFragment;
import com.android.betterway.recyclerview.PickItemAdapter;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择地点的类
 */
public class ItemPickerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        Inputtips.InputtipsListener {

    @BindView(R.id.pickitem_toolbar)
    Toolbar mPickitemToolbar;
    @BindView(R.id.pickitem_recyclerview)
    RecyclerView mPickitemRecyclerview;
    @BindView(R.id.pickitem_search)
    SearchView mPickitemSearch;
    private String searchCity;
    private List<LocationItemBean> mLocationItemBeanList = new ArrayList<LocationItemBean>();
    private PickItemAdapter pickItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_picker);
        ButterKnife.bind(this);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        setSupportActionBar(mPickitemToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mPickitemSearch.onActionViewExpanded();
        mPickitemSearch.setOnQueryTextListener(this);
        Intent intent = getIntent();
        searchCity = intent.getStringExtra("location");
        LogUtil.d("intent data", searchCity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPickitemRecyclerview.setLayoutManager(linearLayoutManager);
        pickItemAdapter = new PickItemAdapter(mLocationItemBeanList);
        pickItemAdapter.setSelectItem(new PickItemAdapter.SelectItem() {
            @Override
            public void select(View view, int position) {
                selectItem(position);
            }
        });
        mPickitemRecyclerview.setAdapter(pickItemAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent();
        intent.putExtra("searched result", "None");
        setResult(LocationDialogFragment.WRONGCODE, intent);
        this.finish();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        LogUtil.e("onQueryTextChange", newText);
        if (!isEmptyOrNullString(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, searchCity);
            inputquery.setCityLimit(true);
            Inputtips inputTips = new Inputtips(this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        } else {
            if (mLocationItemBeanList != null && pickItemAdapter != null) {
                mLocationItemBeanList.clear();
                pickItemAdapter.notifyDataSetChanged();
            }
        }
        return false;
    }


    @Override
    public void onGetInputtips(final List<Tip> list, int i) {
        LogUtil.e("onGetInputtips", list.size() + " items");
        if (i == 1000) {
            for (int j = 0; j < list.size(); j++) {
                LogUtil.e("dist", list.get(j).getDistrict());
                LocationItemBean locationItemBean = new LocationItemBean();
                Tip tip = list.get(j);
                locationItemBean.setName(tip.getName());
                locationItemBean.setAddress(tip.getAddress());
                locationItemBean.setLatLonPoint(tip.getPoint());
                mLocationItemBeanList.add(locationItemBean);
            }
            pickItemAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.show(this, "未找到相应地点");
        }
    }

    /**
     * 判断字符串是否为空
     * @param s 需要判断的字符串
     * @return 是否为空
     */
    private static boolean isEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    /**
     * 选择地点
     * @param position 选择地点的排列号
     */
    private void selectItem(int position) {
        if (mLocationItemBeanList != null) {
            LocationItemBean lociationItemBean = mLocationItemBeanList.get(position);
            Intent intent = new Intent();
            intent.putExtra("getLocation", lociationItemBean);
            setResult(LocationDialogFragment.TRUECODE, intent);
            this.finish();
        }
    }
}
