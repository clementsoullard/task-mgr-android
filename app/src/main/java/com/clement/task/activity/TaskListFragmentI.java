package com.clement.task.activity;

import android.content.Context;

import com.clement.task.object.Task;

import java.util.List;

/**
 * Created by cleme on 29/04/2017.
 */

public interface TaskListFragmentI extends ConnectedContextI {
    Object getSystemService(String layoutInflaterService);

    void askConfirmationBeforeRemoving(String id, String name);

    void refreshTaskList();

    void setTodos(List<Task> tasks);

    Context getContext();

    public void taskEnregistre();

}

