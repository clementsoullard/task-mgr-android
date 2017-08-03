package com.clement.task.task.task;

import com.clement.task.activity.TaskListFragmentI;
import com.clement.task.object.Task;
import com.clement.task.task.BaseTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * Created by cleme on 03/08/2017.
 */

public abstract class CrudTodoTask extends BaseTask {

    protected String messageRetour;


    protected TaskListFragmentI taskFragmentI;


    public CrudTodoTask(TaskListFragmentI createTaskActivity) {
        super(createTaskActivity,createTaskActivity.getTaskSQLiteHelper());
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


}
