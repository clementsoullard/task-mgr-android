package com.clement.task.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.object.Achat;
import com.clement.task.object.Task;

import java.util.List;

/**
 * Created by cleme on 06/08/2017.
 */

public class DbAppHelper extends SQLiteOpenHelper implements DbHelperI {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 9;

    public static final String DATABASE_NAME = "Task.db";

    AchatDao achatDao;
    TaskDao taskDao;

    public DbAppHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        achatDao = new AchatDao(this);
        taskDao = new TaskDao(this);
  }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(AppConstants.DEBUG_TAG, "Creating database ");
        db.execSQL(taskDao.getCreateStatement());
        db.execSQL(achatDao.getCreateStatement());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(AppConstants.DEBUG_TAG, "updgrading database ");
        db.execSQL(taskDao.getDropStatement());
        db.execSQL(achatDao.getDropStatement());
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void clearAchat() {
        achatDao.clearAchat();
    }

    @Override
    public void clearTask() {
        taskDao.clearTask();
    }

    @Override
    public void updateAchat(Achat achat, boolean inSync) {
        achatDao.updateAchat(achat, inSync);
    }

    @Override
    public void updateTask(String mongoId, Boolean done, Integer toCreateToDelete) {
        taskDao.updateTask(mongoId, done, toCreateToDelete);
    }

    @Override
    public void insertAchat(Achat achat, boolean inSync) {
        achatDao.insertAchat(achat, inSync);
    }

    @Override
    public void insertTask(Task task, boolean inSync, int typeofMod) {
        taskDao.insertTask(task, inSync, typeofMod);
    }

    @Override
    public List<Achat> listAchats(boolean onlyAchatToSync) {
       return  achatDao.listAchats(onlyAchatToSync);
    }

    @Override
    public List<Task> listTasks(boolean onlyTaskToSync) {
        return taskDao.listTasks(onlyTaskToSync);
    }

    @Override
    public void markAchatForDeletion(String achatId) {
        achatDao.markAchatForDeletion(achatId);
    }

    @Override
    public void markTaskForDeletion(String taskId) {
        taskDao.markTaskForDeletion(taskId);
    }

}
