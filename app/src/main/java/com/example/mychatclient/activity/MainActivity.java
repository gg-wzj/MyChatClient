package com.example.mychatclient.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mychatclient.R;
import com.example.mychatclient.adapter.MyViewPagerAdapter;
import com.example.mychatclient.app.MyApplication;
import com.example.mychatclient.fragment.ContactFragment;
import com.example.mychatclient.fragment.DiscoveryFragment;
import com.example.mychatclient.fragment.MeFragment;
import com.example.mychatclient.fragment.RecentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzj on 2017/9/21.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<Fragment> mFragmentList;
    private List<String> titleList;
    private String[] titles = new String[]{
            "微信","通讯录","发现","我的"
    };
    private int[] mImgs = new int[]{
            R.drawable.selector_tab_weixin,R.drawable.selector_tab_contact,R.drawable.selector_tab_discovery,R.drawable.selector_tab_me
    };

    private MyViewPagerAdapter mViewPagerAdapter;
    private AppBarLayout mAppBar;
    private ImageView iv_toolbar_add;
    private ImageView iv_toolbar_search;
    private MainReceive receive;


    @Override
    protected void init() {
        super.init();
        mFragmentList = new ArrayList<>();
        RecentFragment recentFragment = new RecentFragment();
        mFragmentList.add(recentFragment);
        ContactFragment contactFragment = new ContactFragment();
        mFragmentList.add(contactFragment);
        DiscoveryFragment discoveryFragment = new DiscoveryFragment();
        mFragmentList.add(discoveryFragment);
        MeFragment meFragment = new MeFragment();
        mFragmentList.add(meFragment);

        titleList = new ArrayList<>();
        for(int i = 0 ;i < titles.length;i++){
            titleList.add(titles[i]);
        }


        receive = new MainReceive();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyApplication.MESSAGE_BROADCAST);
        filter.addAction(MyApplication.VERIFICATION_BROADCAST);
        filter.addAction(MyApplication.OK_BROADCAST);
        registerReceiver(receive,filter);
    }

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mAppBar = (AppBarLayout) findViewById(R.id.appBar);
        //菜单栏
        iv_toolbar_add = mAppBar.findViewById(R.id.iv_toolbar_add);
        iv_toolbar_search = mAppBar.findViewById(R.id.iv_toolbar_search);
        iv_toolbar_add.setVisibility(View.VISIBLE);
        iv_toolbar_search.setVisibility(View.VISIBLE);

        //viewPager
        mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),mFragmentList,
                titleList);
        mViewPager.setAdapter(mViewPagerAdapter);


        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mTabLayout.setSelectedTabIndicatorHeight(0);
        for (int i = 0; i < titleList.size(); i++) {
            //获得到对应位置的Tab
            TabLayout.Tab itemTab = mTabLayout.getTabAt(i);
            if (itemTab != null) {
                //设置自定义的标题
                itemTab.setCustomView(R.layout.item_tab);
                TextView textView = (TextView) itemTab.getCustomView().findViewById(R.id.tv_name);
                textView.setText(titleList.get(i));
                ImageView imageView= (ImageView) itemTab.getCustomView().findViewById(R.id.iv_img);
                imageView.setImageResource(mImgs[i]);
            }
        }
        mTabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_toolbar_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_toolbar_add:
                Intent intent = new Intent(this,AddFriendActivity.class);
                startActivity(intent);
                break;
        }
    }

    class MainReceive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(MyApplication.MESSAGE_BROADCAST.equals(action)) {
                Toast.makeText(MainActivity.this, "收到新信息", Toast.LENGTH_SHORT).show();
                RecentFragment fragment = (RecentFragment) mViewPagerAdapter.getItem(0);
                fragment.update();
            }else if(MyApplication.VERIFICATION_BROADCAST.equals(action)){
                Toast.makeText(MainActivity.this,"收到好友请求",Toast.LENGTH_SHORT).show();
                ContactFragment fragment = (ContactFragment) mViewPagerAdapter.getItem(1);
                fragment.updateTop();
            }else if(MyApplication.OK_BROADCAST.equals(action)){
                Toast.makeText(MainActivity.this,"好友列表以更新",Toast.LENGTH_SHORT).show();
                ContactFragment fragment = (ContactFragment) mViewPagerAdapter.getItem(1);
                fragment.updateList();
            }
        }
    }

    @Override
    protected boolean isToolbarCanBack() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receive);
    }
}
