package com.clement.tvscheduler.task.achat;

import android.util.Log;

import com.clement.tvscheduler.AppConstants;
import com.clement.tvscheduler.activity.fragment.ListeCourseFragment;
import com.clement.tvscheduler.task.BaseTask;

import java.net.HttpURLConnection;

/**
 * Created by Clément on 09/07/2016.
 */
public class EndAchatTask extends BaseTask {


    private String messageRetour;

    private ListeCourseFragment listeCourseActivity;

    /**
     *
     *
     */

    public EndAchatTask(ListeCourseFragment listCourseActivity) {
        super(listCourseActivity);
        this.listeCourseActivity = listCourseActivity;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/ws-finish-achat");
            urlConnection.setRequestMethod("GET");
            urlConnection.getContent();
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
        listeCourseActivity.achatTermine();
    }
}