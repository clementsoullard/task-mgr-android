package com.clement.task.task.task;

import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.activity.TaskListFragmentI;
import com.clement.task.task.BaseTask;

import java.net.HttpURLConnection;

/**
 * This task is to remove an achat to the liste de courses
 * Created by Clément on 09/07/2016.
 */
public class RemoveTodoTask extends CrudTodoTask {


    private String taskId;

    /**
     *
     *
     */

    public RemoveTodoTask(TaskListFragmentI mainActivity, String id) {
        super(mainActivity);
        this.taskId = id;
    }

    @Override
    protected Long doInBackground(Integer... params) {

        try {
            callDelWebService(taskId);
            return 0L;
        } catch (Exception e) {
            dbHelper.markTaskForDeletion(taskId);
            Log.e(AppConstants.ACTIVITY_TAG__TAG, "Erreur " + e.getMessage());
        }
        messageRetour = "Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        connectedActivity.showMessage(messageRetour);
        taskFragmentI.refreshTaskList();
    }
}