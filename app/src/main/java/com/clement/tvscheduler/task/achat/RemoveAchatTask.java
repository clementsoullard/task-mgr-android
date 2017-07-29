package com.clement.tvscheduler.task.achat;

import android.util.Log;

import com.clement.tvscheduler.AppConstants;
import com.clement.tvscheduler.activity.fragment.ListeCourseFragment;

import com.clement.tvscheduler.task.BaseTask;

import java.net.HttpURLConnection;

/**
 * This task is to remove an achat to the liste de courses
 * Created by Clément on 09/07/2016.
 */
public class RemoveAchatTask extends BaseTask {


    private String messageRetour;

    private String achatId;

    private ListeCourseFragment listeCourseActivity;

    /**
     *
     *
     */

    public RemoveAchatTask(ListeCourseFragment listCourseActivity, String achatId) {
        super(listCourseActivity);
        this.listeCourseActivity = listCourseActivity;
        this.achatId = achatId;
    }

    @Override
    protected Long doInBackground(Integer... params) {

        try {
            HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/repository/achat/" + achatId);
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
        listeCourseActivity.refreshListeAchat();
    }
}