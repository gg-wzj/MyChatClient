package com.example.mychatclient.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mychatclient.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et;
    private Button btn;
    private TextView tv;
    private Socket mSocket;
    private boolean alive;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String data = (String) msg.obj;
            tv.setText(data);
        }
    };
    private EditText evip;
    private Button btnconn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        et = (EditText) findViewById(R.id.et);
        btn = (Button) findViewById(R.id.btn);
        tv = (TextView) findViewById(R.id.tv);

        evip = (EditText) findViewById(R.id.evip);
        btnconn = (Button) findViewById(R.id.btnconn);
        btnconn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String s = evip.getText().toString();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            mSocket = new Socket(s, 30000);
                            alive = true;
                            new Thread(new MyClient()).start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final String s = et.getText().toString();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                   /* OutputStreamWriter writer = new OutputStreamWriter(mSocket.getOutputStream());
                    writer.write(s);
                    writer.write("\n");
                    writer.flush();*/
                    OutputStreamWriter ops = new OutputStreamWriter(mSocket.getOutputStream());
                   BufferedWriter writer = new BufferedWriter(ops);
                    writer.write(s);
                    writer.newLine();
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    class MyClient implements Runnable {

        @Override
        public void run() {
            try {
                InputStreamReader is =new InputStreamReader( mSocket.getInputStream());
                BufferedReader reader = new BufferedReader(is);
                String data ;
                while ((data = reader.readLine())!=null){
                    Log.e("wzj",data);

                    Message message = mHandler.obtainMessage();
                    message.obj =data;
                    mHandler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alive =false;
    }
}
