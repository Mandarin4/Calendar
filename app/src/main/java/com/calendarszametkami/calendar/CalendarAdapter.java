package com.calendarszametkami.calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CalendarAdapter extends ArrayAdapter<String> {

    private ArrayList<String> cellData;
    private ArrayList<Integer> markthese =null ;
    private LayoutInflater inflater;
    private int thisyear , thisday,thisdayweek;
    private String thismonth;
    private int realday,realmoun,realyear,mmm;

    CalendarAdapter(@NonNull Context context, int resource, ArrayList<String> cells,int day, int month, ArrayList<Integer> markthese, int year,int dd,int mm,int yy,int week) {
        super(context,R.layout.grid_cell_layout ,cells);
        cellData = cells;
        this.markthese = markthese;
        inflater = LayoutInflater.from(context);
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for(Integer d : markthese)
            thismonth = months[month];
        thisyear = year;
        thisday = day;
        thisdayweek = week;

        realday = dd;
        realmoun = mm;
        realyear = yy;
        mmm = month;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {


        String date = getItem(position);
        //Log.i("position index",String.valueOf(position));
        assert date != null;
        String[] s = date.split(" ");
        int day = Integer.parseInt(s[0]);
        String month = s[1];
        int year = Integer.parseInt(s[2]);

        if (view == null)
            view = inflater.inflate(R.layout.grid_cell_layout, parent, false);

        if(markthese.get(position)==1){
            view.setBackgroundResource(R.drawable.note);
        }

        if ( !month.equals(thismonth) || year != thisyear)
        {
            // if this day is outside current month, grey it out
            ((TextView)view).setTextColor(Color.parseColor("#808080"));
        }
        else
        {
            // if it is today, set it to blue/bold
            ((TextView)view).setTextColor(Color.parseColor("#FF000000"));
        }
        if ( mmm == realmoun && year == realyear && day == realday){
            ((TextView)view).setBackgroundResource(R.drawable.realdat);
        }
       if ((position == 6 || position==5 || position==12 || position==13 || position==19 || position==20 || position==26 || position==27 || position==33 || position==34 || position==40 || position==41)&&( month.equals(thismonth)))
        {
            // if this day is outside current month, grey it out
            ((TextView)view).setTextColor(Color.parseColor("#E62E0F"));
        }

        ((TextView)view).setText(String.valueOf(day));
        return view;
    }



}
