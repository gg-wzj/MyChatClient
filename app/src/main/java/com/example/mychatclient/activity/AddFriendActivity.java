package com.example.mychatclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mychatclient.R;

/**
 * Created by wzj on 2017/9/25.
 */

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_addfriend;
    private Button btn_addfriend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);

        et_addfriend = (EditText) findViewById(R.id.et_addfriend);
        btn_addfriend = (Button) findViewById(R.id.btn_addfriend);

        btn_addfriend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String user = et_addfriend.getText().toString();
        Intent  intent = new Intent(this,DetailActivity.class);
        intent.putExtra(DetailActivity.USER,user);
        startActivity(intent);
    }
}
