package com.example.android.quakereport;

/**
 * Created by Pronoy Mukherjee on 09-May-17.
 */

public class QuakeReport {
    private String magnitude,place,date;
    public QuakeReport(String magnitude,String place, String date){
        this.magnitude=magnitude;
        this.place=place;
        this.date=date;
    }
    public String getMagnitude(){
        return magnitude;
    }
    public String getPlace(){
        return place;
    }
    public String getDate(){
        return date;
    }
}
