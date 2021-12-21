package com.calendarszametkami.calendar;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class CalContract {
    public static final String CONTENT_AUTHORITY = "com.calendarszametkami.calendar";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CAL = "Event";


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private CalContract() {}

    public static final class CalEntry implements BaseColumns {



        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CAL);

        /** Name of database table for calendar */
        public final static String TABLE_NAME = "Event";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CAL;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a calendar event.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CAL;

        // COLUMN_DAY ; day of the event

        public final static String COLUMN_DAY ="day";

        // COLUMN_NOTE ; note to be saved on the specific event

        public final static String COLUMN_NOTE ="note";




    }

}
