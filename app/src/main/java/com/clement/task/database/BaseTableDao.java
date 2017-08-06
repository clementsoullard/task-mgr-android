package com.clement.task.database;

import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by cleme on 06/08/2017.
 */

public abstract class BaseTableDao implements TableDaoI {
    /* Date format to convert to stonrg*/
    DateFormat df = new SimpleDateFormat("yyyMMddhhmmss");

    protected SQLiteOpenHelper sqlLiteHelper;

    public BaseTableDao(SQLiteOpenHelper sqlLiteHelper){
        this.sqlLiteHelper=sqlLiteHelper;
    }

}
