package com.example.gigalodon.bcreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnStop, btnSend;
    EditText editTextMsgToSend;
    TextView textViewCntReceived, textViewMsgReceived;

    MyMainReceiver myMainReceiver;
    Intent myIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button)findViewById(R.id.startservice);
        btnStop = (Button)findViewById(R.id.stopservice);
        btnSend = (Button)findViewById(R.id.send);
        editTextMsgToSend = (EditText)findViewById(R.id.msgtosend);
        textViewCntReceived = (TextView)findViewById(R.id.cntreceived);
        textViewMsgReceived = (TextView)findViewById(R.id.msgreceived);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgToService = editTextMsgToSend.getText().toString();

                Intent intent = new Intent();
                intent.setAction(MyService.ACTION_MSG_TO_SERVICE);
                intent.putExtra(MyService.KEY_MSG_TO_SERVICE, msgToService);
                sendBroadcast(intent);
            }
        });
    }

    private void startService(){
        myIntent = new Intent(MainActivity.this, MyService.class);
        startService(myIntent);
    }

    private void stopService(){
        if(myIntent != null){
            stopService(myIntent);
        }
        myIntent = null;
    }

    @Override
    protected void onStart() {
        myMainReceiver = new MyMainReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.ACTION_UPDATE_CNT);
        intentFilter.addAction(MyService.ACTION_UPDATE_MSG);
        registerReceiver(myMainReceiver, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myMainReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    private class MyMainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(MyService.ACTION_UPDATE_CNT)){
                int int_from_service = intent.getIntExtra(MyService.KEY_INT_FROM_SERVICE, 0);
                textViewCntReceived.setText(String.valueOf(int_from_service));
            }else if(action.equals(MyService.ACTION_UPDATE_MSG)){
                String string_from_service = intent.getStringExtra(MyService.KEY_STRING_FROM_SERVICE);
                textViewMsgReceived.setText(String.valueOf(string_from_service));
            }
        }
    }
}
