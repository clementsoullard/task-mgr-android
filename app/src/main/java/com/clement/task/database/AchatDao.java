package com.clement.task.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.clement.task.object.Achat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The hiearachy in those class is a bit artificial as we use the inheritance mechanism only, so have a distinct class to write the method related to task/achat.
 * Created by cleme on 04/08/2017.
 */

public class AchatDao extends BaseTableDao implements TableDaoI {


    public static final String TABLE_NAME = "achat";
    public static final String COLUMN_NAME_TITLE = "name";
    public static final String COLUMN_NAME_DONE = "done";
    public static final String COLUMN_NAME_ACTIVE = "active";
    public static final String COLUMN_NAME_DATE_COMPLETION = "datecompletion";
    public static final String COLUMN_NAME_MONGO_ID = "mongoid";
    public static final String COLUMN_NAME_INSYNC = "sync";
    public static final String COLUMN_NAME_LAST_SYNC_DATE = "syncdate";
    public static final String COLUMN_NAME_TOCREATE_TODELETE = "tctd";


    private static final String SQL_ACHAT_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_DONE + " INT," +
                    COLUMN_NAME_ACTIVE + " INT," +
                    COLUMN_NAME_DATE_COMPLETION + " TEXT," +
                    COLUMN_NAME_MONGO_ID + " TEXT," +
                    COLUMN_NAME_INSYNC + " TEXT," +
                    COLUMN_NAME_TOCREATE_TODELETE + " TEXT," +
                    COLUMN_NAME_LAST_SYNC_DATE + " TEXT)";

    private static final String SQL_ACHAT_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public AchatDao(SQLiteOpenHelper sqLiteOpenHelper) {
        super(sqLiteOpenHelper);
    }

    @Override
    public String getCreateStatement() {
        return SQL_ACHAT_CREATE_ENTRIES;
    }

    @Override
    public String getDropStatement() {
        return SQL_ACHAT_DELETE_ENTRIES;
    }

    /**
     * Clear all the entries in the database.
     */
    public void clearAchat() {
        SQLiteDatabase db = sqlLiteHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    /**
     * List the acahts that are stored in database.
     *
     * @param onlyAchatToSync determine if the only taks to return are the one to sync.
     * @return
     */
    public List<Achat> listAchats(boolean onlyAchatToSync) {
        List<Achat> achats = new ArrayList<Achat>();
        SQLiteDatabase db = sqlLiteHelper.getWritableDatabase();
        String[] projection = {
                BaseColumns._ID,
                COLUMN_NAME_TITLE,
                COLUMN_NAME_DONE,
                COLUMN_NAME_MONGO_ID,
                COLUMN_NAME_TOCREATE_TODELETE
        };
        String sortOrder = null;
        String selection = null;
        String[] selectionArgs = {};
        /**
         * In case we will use the list to perform the synchronization
         */
        if (onlyAchatToSync) {
            selection = COLUMN_NAME_INSYNC + " LIKE ?";
            selectionArgs = new String[]{"0"};
        }
        /**
         * In case we will use the list to perform the deletion
         */
        else {
            selection = COLUMN_NAME_TOCREATE_TODELETE + " != 2  or " + COLUMN_NAME_TOCREATE_TODELETE + " is null and active != 0";
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
            String mongoId = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_NAME_MONGO_ID));
            int toCreateToUpdate = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_NAME_TOCREATE_TODELETE));

            Achat achat = new Achat();
            achat.setId(mongoId);
            achat.setDone(done);
            achat.setName(name);
            achat.setToCreateToDelete(toCreateToUpdate);
            achats.add(achat);
        }
        cursor.close();
        return achats;
    }

    /**
     * Insert an achat in the database.
     *
     * @param achat
     */
    public void insertAchat(Achat achat, boolean inSync) {
        SQLiteDatabase db = sqlLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TITLE, achat.getName());
        values.put(COLUMN_NAME_DONE, achat.getDone());
        values.put(COLUMN_NAME_INSYNC, inSync);

        /**
         * Whenever an achat is created, it should be active.
         */
        values.put(COLUMN_NAME_ACTIVE, true);
        /**
         * In case this is not in synch we have to specify what can of modification it will be.
         */
        if (!inSync) {
            values.put(COLUMN_NAME_TOCREATE_TODELETE, TO_CREATE);
        }
        values.put(COLUMN_NAME_MONGO_ID, achat.getId());
        long newRowId = db.insert(TABLE_NAME, null, values);
    }

    /**
     * This mark a task for deletion in database
     *
     * @param achatId
     */
    public void markAchatForDeletion(String achatId) {
        SQLiteDatabase db = sqlLiteHelper.getWritableDatabase();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_INSYNC, false);
        values.put(COLUMN_NAME_TOCREATE_TODELETE, TO_DELETE);
        String selection = COLUMN_NAME_MONGO_ID + " LIKE ?";
        String[] selectionArgs = {achatId};
        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    /***
     * Mark a task as not in sync
     * @param achat the achat you wan to update
     */
    public void updateAchat(Achat achat, boolean inSync) {
        SQLiteDatabase db = sqlLiteHelper.getWritableDatabase();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_INSYNC, inSync);
        values.put(COLUMN_NAME_DATE_COMPLETION, df.format(new Date()));
        values.put(COLUMN_NAME_DONE, achat.getDone());
        values.put(COLUMN_NAME_ACTIVE, achat.getActive());
        values.put(COLUMN_NAME_TOCREATE_TODELETE, TO_UPDATE);

        // Which row to update, based on the title
        String selection = COLUMN_NAME_MONGO_ID + " LIKE ?";
        String[] selectionArgs = {achat.getId()};
        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}
