package com.clement.task.task.task;

import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.activity.TaskListFragmentI;
import com.clement.task.object.Task;
import com.clement.task.task.BaseTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by cleme on 03/08/2017.
 */

public abstract class CrudTodoTask extends BaseTask {

    protected String messageRetour;


    protected TaskListFragmentI taskFragmentI;


    public CrudTodoTask(TaskListFragmentI createTaskActivity) {
        super(createTaskActivity, createTaskActivity.getTaskSQLiteHelper());
        this.taskFragmentI = createTaskActivity;
    }

    protected void callCreateWebService(Task task) throws Exception {
        HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/ws-create-todo");
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);

        urlConnection.setRequestProperty("Content-Type", "application/json");
          /*
            * JSON
            */

        JSONObject root = new JSONObject();
        root.put("taskName", task.getName());
        root.put("owner", task.getOwner());
        root.put("expireAtTheEndOfTheDay", task.getTemporary());

        String str = root.toString();
        byte[] outputBytes = str.getBytes("UTF-8");
        OutputStream os = urlConnection.getOutputStream();
        os.write(outputBytes);
        int responseCode = urlConnection.getResponseCode();
        messageRetour = "Succès";

    }

    protected void callDelWebService(String taskId) throws Exception {
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
    }

    protected void callUpdateWebService(Task task) throws Exception {
        HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/repository/task/" + task.getId());
        urlConnection.setRequestMethod("PATCH");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);

        urlConnection.setRequestProperty("Content-Type", "application/json");

            /*
             * <preparing the JSON
             */

        JSONObject root = new JSONObject();
        root.put("id", task.getId());
        root.put("done", task.getDone());
        root.put("expireAtTheEndOfTheDay", task.getTemporary());
        String str = root.toString();
        byte[] outputBytes = str.getBytes("UTF-8");
        OutputStream os = urlConnection.getOutputStream();
        os.write(outputBytes);
        messageRetour = "Succès";

        int responseCode = urlConnection.getResponseCode();


        if (responseCode == HttpsURLConnection.HTTP_NO_CONTENT) {
            Log.e(AppConstants.ACTIVITY_TAG__TAG, "14 - HTTP_OK");
        }
    }


}
