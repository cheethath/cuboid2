package com.vuforia.samples.OnSwap;

public class DataModel {

    String firstLine;
    String secondLine;
    int image;
    public DataModel(int image,String firstLine, String secondLine ) {
        this.firstLine=firstLine;
        this.image=image;
        this.secondLine=secondLine;
    }

    public int getImage() {
        return this.image;
    }
    public String getFirstLine() {
        return this.firstLine;
    }

    public String getSecondLine() {
        return this.secondLine;
    }

}
