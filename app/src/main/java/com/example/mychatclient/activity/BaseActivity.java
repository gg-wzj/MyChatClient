package com.example.mychatclient.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mychatclient.R;

/**
 * Created by wzj on 2017/9/20.
 */

public class BaseActivity extends AppCompatActivity {

    protected AppBarLayout mToolBar;
    private ImageView mToolbarNavigation;
    private TextView mLlToolbarTitle;
    private View mToolbarDivision;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initView();
        setupToolBar();
        initDats();
        initListener();
    }




    protected void init() {
    }

    protected void initView() {
    }

    protected void initDats() {
    }

    protected void initListener() {
    }

    private void setupToolBar() {


        mToolBar = (AppBarLayout) findViewById(R.id.appBar);
        if (mToolBar != null && Build.VERSION.SDK_INT > 21) {
            mToolBar.setElevation(10);
        }

        if (mToolBar != null) {
            mToolbarNavigation = (ImageView) findViewById(R.id.ivToolbarNavigation);
            mToolbarNavigation.setVisibility(isToolbarCanBack() ? View.VISIBLE : View.GONE);
            mToolbarNavigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            mToolbarDivision = findViewById(R.id.vToolbarDivision);
            mToolbarDivision.setVisibility(isToolbarCanBack() ? View.VISIBLE : View.GONE);

            mLlToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
            mLlToolbarTitle.setPadding(isToolbarCanBack() ? 0 : 40, 0, 0, 0);
        }

    }

    /**
     * 子类重写该方法 决定是否显示回退按钮
     * @return
     */
    protected boolean isToolbarCanBack() {
        return true;
    }


}
