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
public class RemoveTodoTask extends BaseTask {


    private String messageRetour;

    private String taskId;

    private TaskListFragmentI mainActivity;

    /**
     *
     *
     */

    public RemoveTodoTask(TaskListFragmentI mainActivity, String id) {
        super(mainActivity,mainActivity.getTaskSQLiteHelper());
        this.mainActivity = mainActivity;
        this.taskId = id;
    }

    @Override
    protected Long doInBackground(Integer... params) {

        try {
            HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/repository/task/" + taskId);
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setDoInput(false);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 204) {
                messageRetour = "Succès";
            } else {
                messageRetour = "Erreur";
            }
            return 0L;
        } catch (Exception e) {
            Log.e(AppConstants.ACTIVITY_TAG__TAG, "Erreur " + e.getMessage());
        }
        messageRetour = "Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        connectedActivity.showMessage(messageRetour);
        mainActivity.refreshTaskList();
    }
}