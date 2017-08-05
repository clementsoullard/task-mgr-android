package com.clement.task.task.achat;

import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.activity.fragment.CourseFragment;
import com.clement.task.object.Achat;
import com.clement.task.task.BaseTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Clément on 09/07/2016.
 */
public class UpdateAchatTask extends CrudAchatTask {


    private Achat achat;


    public UpdateAchatTask(CourseFragment listeCourseActivity, Achat achat) {
        super(listeCourseActivity);
        this.achat = achat;

    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            int responseCode = callUpdateAchatWebService(achat);

            if (responseCode == HttpsURLConnection.HTTP_NO_CONTENT) {
                Log.i(AppConstants.ACTIVITY_TAG__TAG, "14 - HTTP_OK pour id " + achat.getId());
            } else {
                Log.e(AppConstants.ACTIVITY_TAG__TAG, "Retour " + responseCode);
                messageRetour = "Service non disponible";

            }
            dbHelper.updateAchat(achat,true);
            return 0L;
        } catch (Exception e) {
            dbHelper.updateAchat(achat,false);
            Log.e(AppConstants.ACTIVITY_TAG__TAG, e.getMessage()+ " impossible de se connecter au serveur stockage en db de l'achat " );
        }
        messageRetour = "Service non disponible";
        return null;
    }



    @Override
    protected void onPostExecute(Long aLong) {
    }
}