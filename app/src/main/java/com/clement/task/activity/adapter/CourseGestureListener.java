package com.clement.task.activity.adapter;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.clement.task.AppConstants;
import com.clement.task.activity.fragment.ListeCourseFragment;

/**
 * Created by cleme on 30/07/2017.
 */

public class CourseGestureListener extends GestureDetector.SimpleOnGestureListener {
    private ListeCourseFragment listeCourseFragment;

    private String courseId;

    private String courseName;


    public CourseGestureListener(ListeCourseFragment tasksActivity, String courseId, String courseName) {
        super();
        this.listeCourseFragment = tasksActivity;
        this.courseId = courseId;
        this.courseName = courseName;

    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(AppConstants.DEBUG_TAG, "long Press " + e.getDownTime());
        listeCourseFragment.askConfirmationBeforeRemoving(courseId, courseName);

        super.onLongPress(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }


    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(AppConstants.DEBUG_TAG, "Double tap");
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(AppConstants.DEBUG_TAG, "Flying");
        return super.onFling(e1, e2, velocityX, velocityY);
    }


}