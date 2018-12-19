package com.xzw.a7212519.pbfttimetest;

public class MyMessage {
    int fromNodeIndex;
    String voteValue;

    public MyMessage(String voteValue) {
        this.voteValue = voteValue;
    }

    public int getFromNodeIndex() {
        return fromNodeIndex;
    }

    public void setFromNodeIndex(int fromNodeIndex) {
        this.fromNodeIndex = fromNodeIndex;
    }

    public String getVoteValue() {
        return voteValue;
    }
}
