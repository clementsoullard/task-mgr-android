package com.clement.task.activity.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.clement.task.activity.fragment.CourseFragment;
import com.clement.task.activity.fragment.TaskFragment;

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
            return new CourseFragment();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

}
