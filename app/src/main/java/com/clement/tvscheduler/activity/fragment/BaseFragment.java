package com.clement.tvscheduler.activity.fragment;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import com.clement.tvscheduler.activity.ConnectedActivityI;

/**
 * Created by cleme on 29/07/2017.
 */

public class BaseFragment  extends android.support.v4.app.Fragment implements ConnectedActivityI {

    @Override
    public Object getSystemService(String layoutInflaterService) {
        return getActivity().getSystemService(layoutInflaterService);
    }
    public AssetManager getAssets() {
        return getActivity().getAssets();
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
