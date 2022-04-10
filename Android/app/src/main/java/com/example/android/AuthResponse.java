package com.example.android;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AuthResponse implements Serializable {
    @SerializedName("prediction")
    @Expose
    String prediction;

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getBoolPrediction(){
        if(getPrediction().equals("1")){
            return "High";
        }else{
            return "Low";
        }
    }
}
