package com.clement.task.activity.contract;

import android.provider.BaseColumns;

/**
 * Created by cleme on 30/07/2017.
 */

public class AchatContract {
    /* Inner class that defines the table contents */
    public static class AchatEntry implements BaseColumns {
        public static final String TABLE_NAME = "achat";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_DONE = "done";
        public static final String COLUMN_NAME_DATE_COMPLETION = "datecompletion";
        public static final String COLUMN_NAME_MONGO_ID = "mongoid";
        public static final String COLUMN_NAME_INSYNC = "sync";
        public static final String COLUMN_NAME_LAST_SYNC_DATE = "syncdate";
        public static final String COLUMN_NAME_TOCREATE_TODELETE = "tctd";
    }


}
