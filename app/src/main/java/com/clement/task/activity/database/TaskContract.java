package com.clement.task.activity.database;

import android.provider.BaseColumns;

/**
 * Created by cleme on 30/07/2017.
 */

public class TaskContract {
    /* Inner class that defines the table contents */
    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_OWNER = "owner";
        public static final String COLUMN_NAME_DONE = "done";
        public static final String COLUMN_NAME_TEMPORARY = "temporary";
        public static final String COLUMN_NAME_DATE_COMPLETION = "datecompletion";
        public static final String COLUMN_NAME_MONGO_ID = "mongoid";
        public static final String COLUMN_NAME_INSYNC = "sync";
        public static final String COLUMN_NAME_LAST_SYNC_DATE = "syncdate";
        public static final String COLUMN_NAME_TOCREATE_TODELETE = "tctd";
    }

    /**
     * This are the value to store the status of the entry in the column to create to delete.
     * TO_UPDATE means it is nothing
     */
    public static Integer TO_UPDATE = 0;
    public static Integer TO_CREATE = 1;
    public static Integer TO_DELETE = 2;
}
