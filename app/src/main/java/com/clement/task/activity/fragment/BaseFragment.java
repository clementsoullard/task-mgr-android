package com.clement.task.activity.fragment;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import com.clement.task.activity.ConnectedContextI;
import com.clement.task.activity.TasksActivity;
import com.clement.task.activity.contract.DbHelper;

/**
 * Created by cleme on 29/07/2017.
 */

public class BaseFragment  extends android.support.v4.app.Fragment implements ConnectedContextI {

    @Override
    public Object getSystemService(String layoutInflaterService) {
        return getActivity().getSystemService(layoutInflaterService);
    }
    public AssetManager getAssets() {
        return getActivity().getAssets();
    }

    @Override
    public DbHelper getTaskSQLiteHelper() {
        return ((TasksActivity)getActivity()).getTaskSQLiteHelper();
    }

    /**
     * @param message
     */
    public void showMessage(String message) {
        Context context = getActivity().getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
