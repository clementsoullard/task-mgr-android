package com.clement.task.activity;

import android.content.res.AssetManager;

import com.clement.task.activity.contract.DbHelper;

/**
 * Contract that should be implemented by every connected activity and managing synchronization.
 *
 */

public interface ConnectedContextI {


    public AssetManager getAssets();

    public Object getSystemService(String name);

    public void showMessage(String message);

    public DbHelper getTaskSQLiteHelper();
}
