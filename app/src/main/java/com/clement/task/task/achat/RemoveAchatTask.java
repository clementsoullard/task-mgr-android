package com.clement.task.task.achat;

import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.activity.fragment.CourseFragment;

import com.clement.task.task.BaseTask;

import java.net.HttpURLConnection;

/**
 * This task is to remove an achat to the liste de courses
 * Created by Clément on 09/07/2016.
 */
public class RemoveAchatTask extends CrudAchatTask {


    private String achatId;

    /**
     *
     *
     */

    public RemoveAchatTask(CourseFragment listCourseActivity, String achatId) {
        super(listCourseActivity);
        this.achatId = achatId;
    }

    @Override
    protected Long doInBackground(Integer... params) {

        try {
            int responseCode = callDeleteAchatWebService(achatId);

            if (responseCode == 204) {
                messageRetour = "Succès";
            } else {
                messageRetour = "Erreur";
            }
            return 0L;
        } catch (Exception e) {
            dbHelper.markAchatForDeletion(achatId);
            Log.e(AppConstants.ACTIVITY_TAG__TAG, "Erreur " + e.getMessage());
        }
        messageRetour = "Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        connectedActivity.showMessage(messageRetour);
        courseFragment.refreshListeAchat();
    }
}