package com.clement.task.task.task;

import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.activity.fragment.TaskFragment;
import com.clement.task.object.Task;
import com.clement.task.task.BaseTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * This task is to add an task to the liste de courses
 * Created by Clément on 09/07/2016.
 */
public class AddTodoTask extends BaseTask {


    private String messageRetour;

    private Task task;

    private TaskFragment createTaskActivity;

    /**
     *
     *
     */

    public AddTodoTask(TaskFragment createTaskActivity, Task task) {
        super(createTaskActivity);
        this.createTaskActivity = createTaskActivity;
        this.task = task;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
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
        createTaskActivity.taskEnregistre();
    }


}