package com.clement.tvscheduler.task.task;

import android.util.Log;

import com.clement.tvscheduler.TVSchedulerConstants;
import com.clement.tvscheduler.activity.TaskListActivityI;
import com.clement.tvscheduler.task.BaseTask;

import java.net.HttpURLConnection;

/**
 * This task is to remove an achat to the liste de courses
 * Created by Clément on 09/07/2016.
 */
public class RemoveTodoTask extends BaseTask {


    private String messageRetour;

    private String taskId;

    private TaskListActivityI mainActivity;

    /**
     *
     *
     */

    public RemoveTodoTask(TaskListActivityI mainActivity, String id) {
        super(mainActivity);
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
            Log.e(TVSchedulerConstants.ACTIVITY_TAG__TAG, "Erreur " + e.getMessage());
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