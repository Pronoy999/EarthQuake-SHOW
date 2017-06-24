package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JsonParser {
    /** Sample JSON response for a USGS query */
private static String SAMPLE_JSON_RESPONSE;
    /**
     * Create a private constructor because no one should ever create a {@link JsonParser} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    public JsonParser(String data) {
        SAMPLE_JSON_RESPONSE=data;
    }

    /**
     * Return a list of {@link QuakeReport} objects that has been built up from
     * parsing a JSON response.
     */
    public  ArrayList<QuakeReport> extractEarthquakes() {
        ArrayList<QuakeReport> earthQuakes = new ArrayList<>();
       try {
           JSONObject earthQuakeData = new JSONObject(SAMPLE_JSON_RESPONSE);
           JSONArray features = earthQuakeData.getJSONArray("features");
           for (int i = 0; i < features.length(); i++) {
               JSONObject currentObject = features.getJSONObject(i);
               JSONObject property = currentObject.getJSONObject("properties");
               String magnitude = property.getString("mag");
               String place = property.getString("place");
               String time = property.getString("time");
               Date dateobject=new Date(Long.parseLong(time));
               SimpleDateFormat dateFormat= new SimpleDateFormat("MMM DD, yyyy");
               String date=dateFormat.format(dateobject);
               QuakeReport report = new QuakeReport(magnitude, place, date);
               earthQuakes.add(report);
           }
       }
       catch (JSONException e){
           Log.d(EarthquakeActivity.LOG_TAG,e.toString());
       }
       return earthQuakes;
    }
}

