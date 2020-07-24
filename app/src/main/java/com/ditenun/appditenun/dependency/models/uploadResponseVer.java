package com.ditenun.appditenun.dependency.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class uploadResponseVer {
    @SerializedName("pred")
    @Expose
    private String pred;

    public String getPred() {
        return pred;
    }

    public void setPred(String pred) {
        this.pred = pred;
    }
}