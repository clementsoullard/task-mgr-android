package com.clement.task.activity;

import android.content.res.AssetManager;

/**
 * Created by cleme on 14/01/2017.
 */

public interface ConnectedActivityI {


    public AssetManager getAssets();

    public Object getSystemService(String name);

    public void showMessage(String message);

}
