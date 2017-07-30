package com.clement.task.activity.adapter;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.clement.task.AppConstants;
import com.clement.task.activity.TaskListFragmentI;
import com.clement.task.activity.TasksActivity;

/**
 * Created by cleme on 30/07/2017.
 */

public class TaskGestureListener extends GestureDetector.SimpleOnGestureListener  {
    private TaskListFragmentI tasksActivity;

    private String taskId;

    private String taskName;



    public TaskGestureListener(TaskListFragmentI tasksActivity,String taskId,String taskName) {
        super();
        this.tasksActivity =  tasksActivity;
        this.taskId=taskId;
        this.taskName=taskName;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(AppConstants.DEBUG_TAG, "long Press "+(System.currentTimeMillis()- e.getDownTime()));
        tasksActivity.askConfirmationBeforeRemoving(taskId, taskName);
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
        return true;
    }


}