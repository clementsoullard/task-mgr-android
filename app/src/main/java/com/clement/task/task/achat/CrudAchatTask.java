package com.clement.task.task.achat;

import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.activity.TaskListFragmentI;
import com.clement.task.activity.fragment.CourseFragment;
import com.clement.task.object.Achat;
import com.clement.task.object.Task;
import com.clement.task.task.BaseTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by cleme on 03/08/2017.
 */

public abstract class CrudAchatTask extends BaseTask {

    protected String messageRetour;


    protected CourseFragment courseFragment;


    public CrudAchatTask(CourseFragment courseFragment) {
        super(courseFragment, courseFragment.getTaskSQLiteHelper());
        this.courseFragment = courseFragment;
    }

    protected int callCreateAchatWebService(Achat achat) throws Exception {
        HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/ws-create-achat");
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);

        urlConnection.setRequestProperty("Content-Type", "application/json");
          /*
             * JSON
             */
        JSONObject root = new JSONObject();
        root.put("name", achat.getName());
        String str = root.toString();
        byte[] outputBytes = str.getBytes("UTF-8");
        OutputStream os = urlConnection.getOutputStream();
        os.write(outputBytes);
        return urlConnection.getResponseCode();
    }


    protected int callDeleteAchatWebService(String achatId) throws Exception {
        HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/repository/achat/" + achatId);
        urlConnection.setRequestMethod("DELETE");
        urlConnection.setDoInput(false);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        return urlConnection.getResponseCode();
    }

    protected int callUpdateAchatWebService(Achat achat) throws Exception {
        HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/repository/achat/" + achat.getId());
        urlConnection.setRequestMethod("PATCH");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);

        urlConnection.setRequestProperty("Content-Type", "application/json");

            /*
             * JSON
             */

        JSONObject root = new JSONObject();
        root.put("id", achat.getId());
        root.put("done", achat.getDone());
        String str = root.toString();
        byte[] outputBytes = str.getBytes("UTF-8");
        OutputStream os = urlConnection.getOutputStream();
        os.write(outputBytes);
        messageRetour = "Succès";

        return urlConnection.getResponseCode();

    }



}
