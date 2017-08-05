package com.clement.task.task.achat;

import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.activity.fragment.CourseFragment;
import com.clement.task.object.Achat;
import com.clement.task.task.BaseTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * This task is to add an achat to the liste de courses
 * Created by Clément on 09/07/2016.
 */
public class CreateAchatTask extends CrudAchatTask {


    private Achat achat;

    /**
     *
     *
     */

    public CreateAchatTask(CourseFragment courseFragment, Achat achat) {
        super(courseFragment);
        this.achat = achat;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        if (achat.getName() == null || achat.getName().trim().length() == 0) {
            messageRetour = "L'achat n'est pas renseigné";
            return 0L;
        }

        try {
            int responseCode = callCreateAchatWebService(achat);
            if (responseCode == 200) {
                messageRetour = "Succès";
            } else {
                messageRetour = "Erreur";
            }
            return 0L;
        } catch (Exception e) {
            dbHelper.insertAchat(achat,false);
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