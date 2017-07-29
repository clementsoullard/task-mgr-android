package com.clement.tvscheduler.task.task;

import android.util.Log;

import com.clement.tvscheduler.TVSchedulerConstants;
import com.clement.tvscheduler.activity.TaskListActivityI;
import com.clement.tvscheduler.object.Task;
import com.clement.tvscheduler.task.BaseTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Clément on 09/07/2016.
 */
public class UpdateTodoTask extends BaseTask {


    private String messageRetour;

    private String baserUrl;

    private Task task;


    public UpdateTodoTask(TaskListActivityI mainActivity, Task task) {
        super(mainActivity);
        this.task = task;

    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/repository/task/" + task.getId());
            urlConnection.setRequestMethod("PATCH");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");

            /*
             * JSON
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
                Log.e(TVSchedulerConstants.ACTIVITY_TAG__TAG, "14 - HTTP_OK");
            } else {
                Log.e(TVSchedulerConstants.ACTIVITY_TAG__TAG, responseCode + "  - False - HTTP_OK");
                messageRetour = "Service non disponible";
            }
            return 0L;
        } catch (Exception e) {
            Log.e(TVSchedulerConstants.ACTIVITY_TAG__TAG, e.getMessage(), e);
        }
        messageRetour = "Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        connectedActivity.showMessage(messageRetour);
    }
}