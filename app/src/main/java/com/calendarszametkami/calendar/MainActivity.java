package com.calendarszametkami.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    GridView calendarView;
    TextView txtDate;
    LinearLayout header;
    ImageView btnPrev, btnNext;
    ArrayList<String> cells ;
    ArrayList<Integer> markthese;
    Calendar currentDate;
    Button saveEvent,delete_event;
    String selectedDate = " ";
    View pre=null;
    int preColor;
    EditText note_edittext;
    int flag =0;  // 0 save and 1 update

    int dayreal, mounreal,yearreal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarGrid);
        header =  findViewById(R.id.calendar_header);
        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        txtDate = findViewById(R.id.calendar_date_display);
        saveEvent = findViewById(R.id.save_event);
        delete_event = findViewById(R.id.delete_event);
        note_edittext =  findViewById(R.id.note_edittext);
        currentDate = Calendar.getInstance();
        dayreal = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mounreal = Calendar.getInstance().get(Calendar.MONTH);
        yearreal = Calendar.getInstance().get(Calendar.YEAR);
        updateCalendar();
        setClickListener();
        assignClickHandlers();
    }

    private void setClickListener(){
        // click listener
        calendarView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                saveEvent.setVisibility(View.VISIBLE);
                note_edittext.setVisibility(View.VISIBLE);
                if (pre!=null)
                {
                    ((TextView)pre).setTextColor(preColor);  // setting text colour of previously selected day to normal

                }
                selectedDate = String.valueOf(cells.get(position));

                pre = v;
                preColor = ((TextView)v).getCurrentTextColor(); // storing text colour of current selected day to restore it when it is not selected
                ((TextView)v).setTextColor(Color.parseColor("#FF03DAC5"));
                if(markthese.get(position)==1){  // if current date has any event saved
                    saveEvent.setText(R.string.Update);
                    updateEditText(selectedDate);
                    delete_event.setVisibility(View.VISIBLE);
                    flag=1;
                }
                else{
                    saveEvent.setText(R.string.Save);
                    delete_event.setVisibility(View.INVISIBLE);
                    note_edittext.setHint(R.string.add_note);
                    note_edittext.setText("");
                    flag=0;
                }
            }
        });
    }

    private void updateEditText(String selectedDate) { // updating edittext to saved text in database
        String text = query(selectedDate);
        note_edittext.setText(text);
    }


    private void updateCalendar() {  // setting up calendar
        cells = new ArrayList<>();
        markthese = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();
        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 2;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells (42 days calendar as per our business logic)
        while (cells.size() < 42) {
            Date temp = calendar.getTime();
            String[] arr = temp.toString().split(" ");
            String d;
            d = arr[2]+" "+arr[1]+" "+arr[5];
            cells.add(d);
            markthese.add(hasNote(d));

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        for(Integer d : markthese)
            Log.i("list   ", String.valueOf(d));
        // update grid
        calendarView.setAdapter(new CalendarAdapter(this, 0, cells ,currentDate.get(Calendar.DAY_OF_MONTH),  currentDate.get(Calendar.MONTH) , markthese ,currentDate.get(Calendar.YEAR), dayreal,mounreal,yearreal,currentDate.get(Calendar.DAY_OF_WEEK)));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        txtDate.setText(sdf.format(currentDate.getTime()));
    }

    private void assignClickHandlers() {
        // add one month and refresh UI

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // updating calendar to next month
                currentDate.add(Calendar.MONTH, 1);
                Log.i("current month", String.valueOf(currentDate.get(Calendar.MONTH)));
                updateCalendar();
                saveEvent.setVisibility(View.GONE);
                note_edittext.setVisibility(View.GONE);
                delete_event.setVisibility(View.GONE);
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new View.OnClickListener() { // updating calendar to previous month
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                Log.i("current month", String.valueOf(currentDate.get(Calendar.MONTH)));
                updateCalendar();
                saveEvent.setVisibility(View.GONE);
                note_edittext.setVisibility(View.GONE);
                delete_event.setVisibility(View.GONE);
            }
        });
    }

    String query(String day){  // getting note from database
        ContentValues values = new ContentValues();
        values.put(CalContract.CalEntry.COLUMN_DAY, day);

        Cursor c;
        c = getContentResolver().query(CalContract.CalEntry.CONTENT_URI.buildUpon().appendPath(day).build(),null,null, new String[]{day},null,null);
        if(c!=null && c.getCount()>0)
        {
            c.moveToFirst();
            Log.i("from database",c.getString(1));
            String fromdb = c.getString(1);
            c.close();
            return fromdb;
        }
        else
            return "null";

    }

    void update(String note,String day){  // updating note in database
        ContentValues values = new ContentValues();
        values.put(CalContract.CalEntry.COLUMN_NOTE, note);
        Integer rowsupdated = getContentResolver().update(CalContract.CalEntry.CONTENT_URI,values,"day=?",new String[]{day});
        Log.i("rowsupdated", String.valueOf(rowsupdated));
        updateCalendar();
        Toast.makeText(this, "Заметка успешно сохранена",
                Toast.LENGTH_SHORT).show();
        saveEvent.setVisibility(View.GONE);
        note_edittext.setVisibility(View.GONE);
        delete_event.setVisibility(View.GONE);

    }

    private void insert(String day ,String note){ // inserting new note in database

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(CalContract.CalEntry.COLUMN_DAY, day);
        values.put(CalContract.CalEntry.COLUMN_NOTE, note);

        Uri newUri;

        newUri = getContentResolver().insert(CalContract.CalEntry.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(this, "Проверьте данные",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Заметка успешно сохранена",
                    Toast.LENGTH_SHORT).show();
        }
        saveEvent.setVisibility(View.GONE);
        note_edittext.setVisibility(View.GONE);
        delete_event.setVisibility(View.GONE);
    }

    private  void deletenote(String day){

        int rowsdeleted = getContentResolver().delete(CalContract.CalEntry.CONTENT_URI, CalContract.CalEntry.COLUMN_DAY+"=?", new String[]{day});

        // Show a toast message depending on whether or not the insertion was successful
        if (rowsdeleted == 0) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, "Файл не существует", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, "Заметка успешно удалена", Toast.LENGTH_SHORT).show();
        }
        note_edittext.setHint(R.string.add_note);
        note_edittext.setText("");
        updateCalendar();
        selectedDate=" ";
        saveEvent.setVisibility(View.GONE);
        note_edittext.setVisibility(View.GONE);
        delete_event.setVisibility(View.GONE);
    }


    public void saveEvent(View view) {  // on clicking save button either save or update

        if(selectedDate.equals(" ")){
            errorToast();
            return;
        }

        String text = String.valueOf(note_edittext.getText());
        if(text.equals("")){
            Toast.makeText(this, "Проверьте данные",
                    Toast.LENGTH_SHORT).show();
            saveEvent.setVisibility(View.GONE);
            note_edittext.setVisibility(View.GONE);
            delete_event.setVisibility(View.GONE);
            updateCalendar();
        }else
        if(!text.isEmpty()) {
            if (flag == 0) {

                insert(selectedDate, text);
                updateCalendar();

            } else {
                update(text, selectedDate);
            }
        }
        selectedDate=" ";
    }

    int hasNote(String day){  // checking if the current date has an event
        Cursor c = null;
        try{
            ContentValues values = new ContentValues();
            values.put(CalContract.CalEntry.COLUMN_DAY, day);


            c = getContentResolver().query(CalContract.CalEntry.CONTENT_URI.buildUpon().appendPath(day).build(),null,null, new String[]{day},null,null);
            if(c!=null && c.getCount()>0){
                c.close();
                return 1;
            }
            assert c != null;
            c.close();
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteEvent(View view) {
        if(selectedDate.equals(" ")){
            errorToast();
            return;
        }
        deletenote(selectedDate);
        delete_event.setVisibility(View.GONE);
        saveEvent.setText(R.string.Save);
        saveEvent.setVisibility(View.GONE);
        note_edittext.setVisibility(View.GONE);
    }

    public void errorToast(){
        Toast.makeText(this, "Выберите дату", Toast.LENGTH_LONG).show();
    }
}