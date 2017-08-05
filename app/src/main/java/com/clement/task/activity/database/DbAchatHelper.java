package com.clement.task.activity.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.clement.task.object.Achat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The hiearachy in those class is a bit artificial as we use the inheritance mechanism only, so have a distinct class to write the method related to task/achat.
 * Created by cleme on 04/08/2017.
 */

public class DbAchatHelper extends DbTaskHelper implements DbHelperI {

    public DbAchatHelper(Context context) {
        super(context);
    }

    /**
     * Clear all the entries in the database.
     */
    @Override
    public void clearAchat() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(AchatContract.AchatEntry.TABLE_NAME, null, null);
    }

    /**
     * List the acahts that are stored in database.
     *
     * @param onlyAchatToSync determine if the only taks to return are the one to sync.
     * @return
     */
    @Override
    public List<Achat> listAchats(boolean onlyAchatToSync) {
        List<Achat> achats = new ArrayList<Achat>();
        SQLiteDatabase db = getWritableDatabase();
        String[] projection = {
                AchatContract.AchatEntry._ID,
                AchatContract.AchatEntry.COLUMN_NAME_TITLE,
                AchatContract.AchatEntry.COLUMN_NAME_DONE,
                AchatContract.AchatEntry.COLUMN_NAME_MONGO_ID,
                AchatContract.AchatEntry.COLUMN_NAME_TOCREATE_TODELETE
        };
        String sortOrder = null;
        String selection = null;
        String[] selectionArgs = {};
        /**
         * In case we will use the list to perform the synchronization
         */
        if (onlyAchatToSync) {
            selection = AchatContract.AchatEntry.COLUMN_NAME_INSYNC + " LIKE ?";
            selectionArgs = new String[]{"0"};
        }
        /**
         * In case we will use the list to perform the deletion
         */
        else {
            selection = AchatContract.AchatEntry.COLUMN_NAME_TOCREATE_TODELETE + " != 2  or " + AchatContract.AchatEntry.COLUMN_NAME_TOCREATE_TODELETE + " is null";
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
            int toCreateToUpdate = cursor.getInt(
                    cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_TOCREATE_TODELETE));

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
    @Override
    public void insertAchat(Achat achat, boolean inSync) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AchatContract.AchatEntry.COLUMN_NAME_TITLE, achat.getName());
        values.put(AchatContract.AchatEntry.COLUMN_NAME_DONE, achat.getDone());
        values.put(AchatContract.AchatEntry.COLUMN_NAME_INSYNC, inSync);
        /**
         * In case this is not in synch we have to specify what can of modification it will be.
         */
        if (!inSync) {
            values.put(AchatContract.AchatEntry.COLUMN_NAME_TOCREATE_TODELETE, TO_CREATE);
        }
        values.put(AchatContract.AchatEntry.COLUMN_NAME_MONGO_ID, achat.getId());
        long newRowId = db.insert(AchatContract.AchatEntry.TABLE_NAME, null, values);
    }

    /**
     * This mark a task for deletion in database
     *
     * @param taskId
     */
    public void markAchatForDeletion(String taskId) {
        SQLiteDatabase db = getWritableDatabase();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(AchatContract.AchatEntry.COLUMN_NAME_INSYNC, false);
        values.put(AchatContract.AchatEntry.COLUMN_NAME_TOCREATE_TODELETE, TO_DELETE);
        String selection = AchatContract.AchatEntry.COLUMN_NAME_MONGO_ID + " LIKE ?";
        String[] selectionArgs = {taskId};
        int count = db.update(
                AchatContract.AchatEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    /***
     * Mark a task as not in sync
     * @param achat the achat you wan to update
     */
    public void updateAchat(Achat achat, boolean inSync) {
        SQLiteDatabase db = getWritableDatabase();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(AchatContract.AchatEntry.COLUMN_NAME_INSYNC, inSync);
        values.put(AchatContract.AchatEntry.COLUMN_NAME_DATE_COMPLETION, df.format(new Date()));
        values.put(AchatContract.AchatEntry.COLUMN_NAME_DONE, achat.getDone());
        values.put(AchatContract.AchatEntry.COLUMN_NAME_TOCREATE_TODELETE, TO_UPDATE);

        // Which row to update, based on the title
        String selection = AchatContract.AchatEntry.COLUMN_NAME_MONGO_ID + " LIKE ?";
        String[] selectionArgs = {achat.getId()};
        int count = db.update(
                AchatContract.AchatEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }


}
