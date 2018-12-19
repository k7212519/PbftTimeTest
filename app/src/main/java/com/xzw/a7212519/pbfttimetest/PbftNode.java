package com.xzw.a7212519.pbfttimetest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PbftNode {
    int index;
    Context context;

    public PbftNode(int index, Context context) {
        this.index = index;
        this.context = context;

    }

    public int getIndex() {
        return index;
    }

    public void sendPrePrepareMessage(MyMessage myMessage, int targetIndex) throws InterruptedException {
        //模拟发送消息耗时操作
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    //接收信息
                    receivePrePrepareMessage(myMessage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
        Thread.sleep(5);

        Log.d("tag", "node"+index+"发送了消息(PrePrepare):"+myMessage.getVoteValue());
        sendMyBroadcast(context, "updateTextLog", "node"+index+"发送了消息(PrePrepare):"+myMessage.getVoteValue()+"\n");

        receivePrePrepareMessage(myMessage, targetIndex);
    }


    /**
     * 接收PrePrepare消息
     * @param myMessage
     * @param targetIndex
     */
    public void receivePrePrepareMessage(MyMessage myMessage, int targetIndex){
        if (targetIndex != 0) {
            Log.d("tag", "node"+targetIndex+"接收了消息(PrePrepare):"+myMessage.getVoteValue());
            sendMyBroadcast(context, "updateTextLog", "node"+targetIndex+"接收了消息(PrePrepare):"+myMessage.getVoteValue()+"\n");

        }
    }

    /**
     * 发送Prepare消息
     * @param myMessage
     * @param targetIndex
     * @throws InterruptedException
     */
    public void sendPrepareMessage(MyMessage myMessage, int targetIndex) throws InterruptedException {
        Thread.sleep(5);
        Log.d("tag", "node"+index+"发送了消息(Prepare):"+myMessage.getVoteValue());
        sendMyBroadcast(context, "updateTextLog", "node"+index+"发送了消息(Prepare):"+myMessage.getVoteValue()+"\n");
        receivePrepareMessage(myMessage, targetIndex);
    }

    /**
     * 接收Prepare消息
     * @param myMessage
     * @param targetIndex
     */
    public void receivePrepareMessage(MyMessage myMessage, int targetIndex){
        if (targetIndex != index) {
            Log.d("tag", "node"+targetIndex+"接收了消息(Prepare):"+myMessage.getVoteValue());
            sendMyBroadcast(context, "updateTextLog", "node"+targetIndex+"接收了消息(Prepare):"+myMessage.getVoteValue()+"\n");

        }
    }


    /**
     * 发送Commit消息
     * @param myMessage
     * @param targetIndex
     * @throws InterruptedException
     */
    public void sendCommitMessage(MyMessage myMessage, int targetIndex) throws InterruptedException {
        Thread.sleep(5);
        Log.d("tag", "node"+index+"发送了消息(Commit):"+myMessage.getVoteValue());
        sendMyBroadcast(context, "updateTextLog", "node"+index+"发送了消息(Commit):"+myMessage.getVoteValue()+"\n");
        receiveCommitMessage(myMessage, targetIndex);
    }

    /**
     * 接收Commit消息
     * @param myMessage
     * @param targetIndex
     */
    public void receiveCommitMessage(MyMessage myMessage, int targetIndex){
        if (targetIndex != index) {
            Log.d("tag", "node"+targetIndex+"接收了消息(Commit):"+myMessage.getVoteValue());
            sendMyBroadcast(context, "updateTextLog", "node"+targetIndex+"接收了消息(Commit):"+myMessage.getVoteValue()+"\n");

        }
    }

    /**
     * 发送Reply消息
     * @param myMessage
     * @throws InterruptedException
     */
    public void sendReplyMessage(MyMessage myMessage) throws InterruptedException {
//        Thread.sleep(5);
        Log.d("tag", "node"+index+"发送了消息(Reply):"+myMessage.getVoteValue());
        sendMyBroadcast(context, "updateTextLog", "node"+index+"发送了消息(Reply):"+myMessage.getVoteValue()+"\n");
        Client.receiveReplyMessage(myMessage);
        sendMyBroadcast(context, "updateTextLog", "Client接收了消息(Reply):"+myMessage.getVoteValue()+"\n");
        sendMyBroadcast(context, "finish", null);
    }

    private void sendMyBroadcast(Context context, String action, String data) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("data", data);
        context.sendBroadcast(intent);
    }
}
