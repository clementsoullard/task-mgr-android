package com.clement.task.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.object.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cleme on 30/07/2017.
 */

public class TaskDao extends BaseTableDao implements TableDaoI {

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


    private static final String SQL_TASK_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_DONE + " INT," +
                    COLUMN_NAME_TEMPORARY + " INT," +
                    COLUMN_NAME_OWNER + " TEXT," +
                    COLUMN_NAME_DATE_COMPLETION + " TEXT," +
                    COLUMN_NAME_MONGO_ID + " TEXT," +
                    COLUMN_NAME_INSYNC + " TEXT," +
                    COLUMN_NAME_TOCREATE_TODELETE + " TEXT," +
                    COLUMN_NAME_LAST_SYNC_DATE + " TEXT)";


    private static final String SQL_TASK_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public TaskDao(SQLiteOpenHelper sqLiteOpenHelper) {
        super(sqLiteOpenHelper);
    }

    @Override
    public String getDropStatement() {
        return SQL_TASK_DELETE_ENTRIES;
    }

    @Override
    public String getCreateStatement() {
        return SQL_TASK_CREATE_ENTRIES;
    }

    /**
     * Clear all the task entries in the database.
     */
    public void clearTask() {
        SQLiteDatabase db = sqlLiteHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }


     /**
     * List the tasks that are stored in database.
     *
     * @param onlyTaskToSync determine if the only taks to return are the one to sync.
     * @return
     */
    public List<Task> listTasks(boolean onlyTaskToSync) {
        List<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase db = sqlLiteHelper.getWritableDatabase();
        String[] projection = {
                BaseColumns._ID,
                COLUMN_NAME_TITLE,
                COLUMN_NAME_DONE,
                COLUMN_NAME_OWNER,
                COLUMN_NAME_MONGO_ID,
                COLUMN_NAME_TOCREATE_TODELETE
        };
        String sortOrder = null;

        String selection = null;
        String[] selectionArgs = {};
        /**
         * Only the tasks to sync
         */
        if (onlyTaskToSync) {
            selection = COLUMN_NAME_INSYNC + " LIKE ?";
            selectionArgs = new String[]{"0"};
        } else {
            selection = COLUMN_NAME_TOCREATE_TODELETE + " != 2  or " + COLUMN_NAME_TOCREATE_TODELETE + " is null";
            //     selectionArgs = new String[]{TO_DELETE.toString()};
        }

        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
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
                    cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_NAME_TITLE));
            Boolean done = !(0 == cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_NAME_DONE)));
            String owner = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_NAME_OWNER));
            String mongoId = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_NAME_MONGO_ID));
            int toCreateToUpdate = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_NAME_TOCREATE_TODELETE));

            Task task = new Task();
            task.setId(mongoId);
            task.setOwner(owner);
            task.setDone(done);
            task.setName(name);
            task.setToCreateToDelete(toCreateToUpdate);
            tasks.add(task);
        }
        cursor.close();
        return tasks;
    }


    /**
     * Insert a task in the database.
     *
     * @param task
     */
    public void insertTask(Task task, boolean inSync, int typeofMod) {
        SQLiteDatabase db = sqlLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TITLE, task.getName());
        values.put(COLUMN_NAME_DONE, task.getDone());
        values.put(COLUMN_NAME_INSYNC, inSync);
        values.put(COLUMN_NAME_OWNER, task.getOwner());
        values.put(COLUMN_NAME_MONGO_ID, task.getId());
        values.put(COLUMN_NAME_TOCREATE_TODELETE, typeofMod);
        values.put(COLUMN_NAME_TEMPORARY, task.getTemporary());
        long newRowId = db.insert(TABLE_NAME, null, values);
    }


    /***
     * Mark a task as not in sync
     * @param mongoId
     */
    public void updateTask(String mongoId, Boolean done, Integer toCreateToDelete) {
        SQLiteDatabase db = sqlLiteHelper.getWritableDatabase();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_INSYNC, false);
        values.put(COLUMN_NAME_DATE_COMPLETION, df.format(new Date()));
        values.put(COLUMN_NAME_DONE, done);
        values.put(COLUMN_NAME_TOCREATE_TODELETE, toCreateToDelete);

        // Which row to update, based on the title
        String selection = COLUMN_NAME_MONGO_ID + " LIKE ?";
        String[] selectionArgs = {mongoId};
        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    /**
     * This mark a task for deletion in database
     *
     * @param taskId
     */
    public void markTaskForDeletion(String taskId) {
        SQLiteDatabase db = sqlLiteHelper.getWritableDatabase();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_INSYNC, false);
        values.put(COLUMN_NAME_TOCREATE_TODELETE, TO_DELETE);

        String selection = COLUMN_NAME_MONGO_ID + " LIKE ?";
        String[] selectionArgs = {taskId};
        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}