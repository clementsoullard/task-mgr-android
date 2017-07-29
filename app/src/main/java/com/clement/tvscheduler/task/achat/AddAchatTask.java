package com.clement.tvscheduler.task.achat;

import android.util.Log;

import com.clement.tvscheduler.TVSchedulerConstants;
import com.clement.tvscheduler.activity.ListeCourseActivity;
import com.clement.tvscheduler.object.Achat;
import com.clement.tvscheduler.task.BaseTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * This task is to add an achat to the liste de courses
 * Created by Clément on 09/07/2016.
 */
public class AddAchatTask extends BaseTask {


    private String messageRetour;

    private Achat achat;

    private ListeCourseActivity listeCourseActivity;

    /**
     *
     *
     */

    public AddAchatTask(ListeCourseActivity listCourseActivity, Achat achat) {
        super(listCourseActivity);
        this.listeCourseActivity = listCourseActivity;
        this.achat = achat;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        if (achat.getName() == null || achat.getName().trim().length() == 0) {
            messageRetour = "L'achat n'est pas renseigné";
            return 0L;
        }

        try {
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
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
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
        listeCourseActivity.refreshListeAchat();
    }
}