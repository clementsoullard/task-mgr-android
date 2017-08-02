package com.clement.task.activity.contract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.object.Achat;
import com.clement.task.object.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cleme on 30/07/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    /**
     * This are the value to store the status of the entry in the column to create to delete.
     * TO_UPDATE means it is nothing
     */
    public static Integer TO_UPDATE = 0;
    public static Integer TO_CREATE = 1;
    public static Integer TO_DELETE = 2;

    /* Date format to convert to stonrg*/
    DateFormat df = new SimpleDateFormat("yyyMMddhhmmss");
    private static final String SQL_TASK_CREATE_ENTRIES =
            "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " (" +
                    TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskContract.TaskEntry.COLUMN_NAME_TITLE + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_DONE + " INT," +
                    TaskContract.TaskEntry.COLUMN_NAME_TEMPORARY + " INT," +
                    TaskContract.TaskEntry.COLUMN_NAME_OWNER + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_DATE_COMPLETION + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_MONGO_ID + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_INSYNC + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_TOCREATE_TODELETE + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_LAST_SYNC_DATE + " TEXT)";

    private static final String SQL_ACHAT_CREATE_ENTRIES =
            "CREATE TABLE " + AchatContract.AchatEntry.TABLE_NAME + " (" +
                    AchatContract.AchatEntry._ID + " INTEGER PRIMARY KEY," +
                    AchatContract.AchatEntry.COLUMN_NAME_TITLE + " TEXT," +
                    AchatContract.AchatEntry.COLUMN_NAME_DONE + " INT," +
                    AchatContract.AchatEntry.COLUMN_NAME_DATE_COMPLETION + " TEXT," +
                    AchatContract.AchatEntry.COLUMN_NAME_MONGO_ID + " TEXT," +
                    AchatContract.AchatEntry.COLUMN_NAME_INSYNC + " TEXT," +
                    AchatContract.AchatEntry.COLUMN_NAME_TOCREATE_TODELETE + " TEXT," +
                    AchatContract.AchatEntry.COLUMN_NAME_LAST_SYNC_DATE + " TEXT)";

    private static final String SQL_TASK_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME;
    private static final String SQL_ACHAT_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AchatContract.AchatEntry.TABLE_NAME;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 8;

    public static final String DATABASE_NAME = "Task.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(AppConstants.DEBUG_TAG, "Creating database ");
        db.execSQL(SQL_TASK_CREATE_ENTRIES);
        db.execSQL(SQL_ACHAT_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(AppConstants.DEBUG_TAG, "updgrading database ");
        db.execSQL(SQL_TASK_DELETE_ENTRIES);
        db.execSQL(SQL_ACHAT_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Clear all the task entries in the database.
     */
    public void clearTask() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE_NAME, null, null);
    }
    /**
     * Clear all the entries in the database.
     */
    public void clearAchat() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(AchatContract.AchatEntry.TABLE_NAME, null, null);
    }

    /**
     * Check if the entry is in the database.
     *
     * @param mongoId
     * @return
     */
    public boolean getTaskByMongoId(String mongoId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_NAME_TITLE,
                TaskContract.TaskEntry.COLUMN_NAME_MONGO_ID
        };


// How you want the results sorted in the resulting Cursor
        String sortOrder =
                TaskContract.TaskEntry.COLUMN_NAME_TITLE + " DESC";

        String selection = TaskContract.TaskEntry.COLUMN_NAME_MONGO_ID + " = ?";

        // Filter results WHERE "title" = 'My Title'
        String[] selectionArgs = {mongoId};
        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List itemIds = new ArrayList<>();
        if (cursor.isLast()) {
            return false;
        }
        cursor.close();
        return true;
    }

    /**
     * List the tasks that are stored in database.
     *
     * @param onlyTaskToSync determine if the only taks to return are the one to sync.
     * @return
     */
    public List<Task> listTasks(boolean onlyTaskToSync) {
        List<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase db = getWritableDatabase();
        String[] projection = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_NAME_TITLE,
                TaskContract.TaskEntry.COLUMN_NAME_DONE,
                TaskContract.TaskEntry.COLUMN_NAME_OWNER,
                TaskContract.TaskEntry.COLUMN_NAME_MONGO_ID,

        };


// How you want the results sorted in the resulting Cursor
        String sortOrder = null;

        String selection = null;

        // Filter results WHERE "title" = 'My Title'
        String[] selectionArgs = {};
        if (onlyTaskToSync) {
            selection = TaskContract.TaskEntry.COLUMN_NAME_INSYNC + " LIKE ?";
            selectionArgs =new String[] {"false" };
        }
        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List itemIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(TaskContract.TaskEntry._ID));
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_TITLE));
            Boolean done = !(0 == cursor.getInt(
                    cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_DONE)));
            String owner = cursor.getString(
                    cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_OWNER));
            String mongoId = cursor.getString(
                    cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_MONGO_ID));

            Task task = new Task();
            task.setId(mongoId);
            task.setOwner(owner);
            task.setDone(done);
            task.setName(name);
            tasks.add(task);
        }
        cursor.close();
        return tasks;
    }

    /**
     * List the tasks that are stored in database.
     *
     * @param onlyAchatToSync determine if the only taks to return are the one to sync.
     * @return
     */
    public List<Achat> listAchats(boolean onlyAchatToSync) {
        List<Achat> achats = new ArrayList<Achat>();
        SQLiteDatabase db = getWritableDatabase();
        String[] projection = {
                AchatContract.AchatEntry._ID,
                AchatContract.AchatEntry.COLUMN_NAME_TITLE,
                AchatContract.AchatEntry.COLUMN_NAME_DONE,
                AchatContract.AchatEntry.COLUMN_NAME_MONGO_ID,
      };


// How you want the results sorted in the resulting Cursor
        String sortOrder = null;

        String selection = null;

        // Filter results WHERE "title" = 'My Title'
        String[] selectionArgs = {};
        if (onlyAchatToSync) {
            selection = TaskContract.TaskEntry.COLUMN_NAME_INSYNC + " LIKE ?";
            selectionArgs =new String[] {"false" };
        }
        Cursor cursor = db.query(
                AchatContract.AchatEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List itemIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(AchatContract.AchatEntry._ID));
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(AchatContract.AchatEntry.COLUMN_NAME_TITLE));
            Boolean done = !(0 == cursor.getInt(
                    cursor.getColumnIndexOrThrow(AchatContract.AchatEntry.COLUMN_NAME_DONE)));
            String mongoId = cursor.getString(
                    cursor.getColumnIndexOrThrow(AchatContract.AchatEntry.COLUMN_NAME_MONGO_ID));

            Achat achat = new Achat();
            achat.setId(mongoId);
            achat.setDone(done);
            achat.setName(name);
            achats.add(achat);
        }
        cursor.close();
        return achats;
    }

    /**
     * Insert a task in the database.
     *
     * @param task
     */
    public void insertTask(Task task,boolean inSync) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TITLE, task.getName());
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DONE, task.getDone());
        values.put(TaskContract.TaskEntry.COLUMN_NAME_INSYNC, inSync);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_OWNER, task.getOwner());
        values.put(TaskContract.TaskEntry.COLUMN_NAME_MONGO_ID, task.getId());
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TEMPORARY, task.getTemporary());
        long newRowId = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
    }
    /**
     * Insert an achat in the database.
     *
     * @param achat
     */
    public void insertAchat(Achat achat,boolean inSync) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AchatContract.AchatEntry.COLUMN_NAME_TITLE, achat.getName());
        values.put(AchatContract.AchatEntry.COLUMN_NAME_DONE, achat.getDone());
        values.put(AchatContract.AchatEntry.COLUMN_NAME_INSYNC, inSync);
        values.put(AchatContract.AchatEntry.COLUMN_NAME_MONGO_ID, achat.getId());
        long newRowId = db.insert(AchatContract.AchatEntry.TABLE_NAME, null, values);
    }

    /***
     * Mark a task as not in sync
     * @param mongoId
     */
    public void setModified(String mongoId, Boolean done, Integer toCreateToDelete) {
        SQLiteDatabase db = getWritableDatabase();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_INSYNC, false);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DATE_COMPLETION, df.format(new Date()));
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DONE, done);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TOCREATE_TODELETE, toCreateToDelete);

        // Which row to update, based on the title
        String selection = TaskContract.TaskEntry.COLUMN_NAME_MONGO_ID + " LIKE ?";
        String[] selectionArgs = {mongoId};
        int count = db.update(
                TaskContract.TaskEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }


}