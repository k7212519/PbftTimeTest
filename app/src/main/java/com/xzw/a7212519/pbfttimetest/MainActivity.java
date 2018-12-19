package com.xzw.a7212519.pbfttimetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    public static int communicateCount = 0;
    public static int nodeAmount = 5;
    public static PbftNode[] pbftNodes;
    public static int closeNodeNumber = 0;
    public static long startTime;
    public static long endTime;
    public static long usedTime;
    private int offset;
    private Client client;

    private TextView textViewLog;
    private Button buttonStartSimulate;
    private EditText editTextNodeAmount;
    private TextView textViewTime;
    private TextView textViewCommunicateCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        textViewLog = findViewById(R.id.text_log);
        editTextNodeAmount = findViewById(R.id.editText_nodeAmount);
        buttonStartSimulate = findViewById(R.id.btn_start);
        textViewTime = findViewById(R.id.text_time);
        textViewCommunicateCount = findViewById(R.id.text_count);

        textViewLog.setMovementMethod(ScrollingMovementMethod.getInstance());

        buttonStartSimulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextNodeAmount.getText().length() != 0) {
                    textViewLog.setText("");
                    nodeAmount = Integer.parseInt(editTextNodeAmount.getText().toString());
//                    Log.d("debug", String.valueOf(nodeAmount));
                    final MyMessage myMessage = new MyMessage("pass");
                    client = new Client(MainActivity.this);

                    //开始时间
                    startTime = System.currentTimeMillis();
                    //创建节点
                    createNode(nodeAmount);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //request模拟
                            try {
                                client.sendReplyMessage(myMessage);
//                                communicateCount ++;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            //PrePrepare模拟
                            for (int j = 1; j < nodeAmount; j++) {
                                try {
                                    pbftNodes[0].sendPrePrepareMessage(myMessage, j);
                                    communicateCount ++;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            //Prepare模拟 i代表发送端nodeIndex  j代表接收端nodeIndex
                            for (int i = 1; i < nodeAmount; i++) {
                                for (int j = 0; j < nodeAmount; j++) {
                                    if (j != i) {
                                        try {
                                            pbftNodes[i].sendPrepareMessage(myMessage, j);
                                            communicateCount++;
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            //commit模拟
                            for (int i = 0; i < nodeAmount; i++) {
                                for (int j = 0; j < nodeAmount; j++) {
                                    if (j != i) {
                                        try {
                                            pbftNodes[i].sendCommitMessage(myMessage, j);
                                            communicateCount++;
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            //reply模拟
                            for (int i = 0; i < nodeAmount; i++) {
                                try {
                                    pbftNodes[i].sendReplyMessage(myMessage);
//                                    communicateCount++;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();


                }
            }
        });
    }

    private void initData() {
        MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("finish");
        intentFilter.addAction("updateTextLog");
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    private void createNode(int amount) {
        pbftNodes = new PbftNode[amount];
        for (int i = 0; i < amount; i++) {
            pbftNodes[i] = new PbftNode(i,this);
//            Log.d("tag", String.valueOf(pbftNodes[i].getIndex()));
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "finish":
                    textViewTime.setText("timeUsed:" + String.valueOf(usedTime) + "ms");
                    textViewCommunicateCount.setText(String.valueOf(communicateCount));
                    break;
                case "updateTextLog":
//                    textViewLog.setText("");
                    textViewLog.append(intent.getExtras().getString("data"));
                    offset = textViewLog.getLineCount()*textViewLog.getLineHeight();
                    if (offset > textViewLog.getHeight()) {
                        textViewLog.scrollTo(0,offset-textViewLog.getHeight());
                    }
                    break;
                default:
                    break;
            }

        }
    }
}
