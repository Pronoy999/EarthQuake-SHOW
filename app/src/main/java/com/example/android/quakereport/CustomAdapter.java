package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Pronoy Mukherjee on 09-May-17.
 */

public class CustomAdapter extends ArrayAdapter<QuakeReport> {
    private final static String LOCATION_SEPARATOR="of";
    public CustomAdapter(Activity context, ArrayList<QuakeReport> quakeReports){
        super(context,0,quakeReports);
    }
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View listitem=convertView;
        if(listitem==null){
            listitem= LayoutInflater.from(getContext()).inflate(R.layout.custom_adapter,parent,false);
        }
        QuakeReport quakeReport=getItem(position);
        TextView mag=(TextView) listitem.findViewById(R.id.magnitude);
        GradientDrawable magnitudeCircle=(GradientDrawable) mag.getBackground();
        int magnitudeColor=getMagnitudeColor(Double.parseDouble(quakeReport.getMagnitude()));
        magnitudeCircle.setColor(magnitudeColor);
        mag.setText(formatMagnitude(quakeReport.getMagnitude()));
        TextView primaryLocation=(TextView) listitem.findViewById(R.id.primary_location);
        TextView offsetLocation=(TextView) listitem.findViewById(R.id.location_offset);
        String location=quakeReport.getPlace();
        String primary,offset;
        if (location.contains(LOCATION_SEPARATOR)) {
            String parts[]=location.split(LOCATION_SEPARATOR);
            offset=parts[0]+LOCATION_SEPARATOR;
            primary=parts[1];
        }
        else {
                offset=getContext().getString(R.string.near_the);
                primary=quakeReport.getPlace();
        }
        primaryLocation.setText(primary);
        offsetLocation.setText(offset);
        TextView date=(TextView) listitem.findViewById(R.id.date);
        date.setText(quakeReport.getDate());
        return listitem;
    }
    private String formatMagnitude(String magnitude){
        double mag=Double.parseDouble(magnitude);
        DecimalFormat magnitudeFormat=new DecimalFormat("0.0");
        return magnitudeFormat.format(mag);
    }
    private int getMagnitudeColor(double magnitude){
        int magnitudeColorResId;
        int magnitudeFloor=(int)Math.floor(magnitude);
        switch (magnitudeFloor){
            case 0:
            case 1:magnitudeColorResId=R.color.magnitude1;
                break;
            case 2:magnitudeColorResId=R.color.magnitude2;
                break;
            case 3: magnitudeColorResId=R.color.magnitude3;
                break;
            case 4:magnitudeColorResId=R.color.magnitude4;
                break;
            case 5: magnitudeColorResId=R.color.magnitude5;
                break;
            case 6:magnitudeColorResId=R.color.magnitude6;
                break;
            case 7: magnitudeColorResId=R.color.magnitude7;
                break;
            case 8:magnitudeColorResId=R.color.magnitude8;
                break;
            case 9:magnitudeColorResId=R.color.magnitude9;
                break;
            case 10: magnitudeColorResId=R.color.magnitude9;
                break;
            default:magnitudeColorResId=R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),magnitudeColorResId);
    }
}
