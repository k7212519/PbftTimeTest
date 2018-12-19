package com.xzw.a7212519.pbfttimetest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class Client {
    private Context context;

    public Client(Context context) {
        this.context = context;
    }

    /**
     * 发送Reply消息
     * @param myMessage
     * @throws InterruptedException
     */
    public void sendReplyMessage(MyMessage myMessage) throws InterruptedException {
        Thread.sleep(10);
        Log.d("tag", "Client发送了消息(Request):"+myMessage.getVoteValue());

    }

    /**
     * 接收Reply消息
     * @param myMessage
     */
    public static void receiveReplyMessage(MyMessage myMessage){
        Log.d("tag", "Client接收了消息(Reply):"+myMessage.getVoteValue());

        //结束时间
        MainActivity.endTime = System.currentTimeMillis();
        //使用时间
        MainActivity.usedTime = MainActivity.endTime - MainActivity.startTime;

    }

    private void sendMyBroadcast(Context context, String action, String data) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("data", data);
        context.sendBroadcast(intent);
    }
}
