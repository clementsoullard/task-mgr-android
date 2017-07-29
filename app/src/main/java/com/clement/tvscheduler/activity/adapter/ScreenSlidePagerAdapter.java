package com.clement.tvscheduler.activity.adapter;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.clement.tvscheduler.activity.fragment.ListeCourseFragment;
import com.clement.tvscheduler.activity.fragment.TaskFragment;

/**
 * Created by cleme on 29/07/2017.
 */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        if (position == 0) {
            return new TaskFragment();
        } else {
            return new ListeCourseFragment();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

}
