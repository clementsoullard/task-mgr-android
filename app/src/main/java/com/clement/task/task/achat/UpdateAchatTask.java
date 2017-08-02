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
public class UpdateAchatTask extends BaseTask {


    private String messageRetour;

    private String baserUrl;

    private Achat achat;


    public UpdateAchatTask(CourseFragment listeCourseActivity, Achat achat) {
        super(listeCourseActivity,listeCourseActivity.getTaskSQLiteHelper());
        this.achat = achat;

    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
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

            int responseCode = urlConnection.getResponseCode();


            if (responseCode == HttpsURLConnection.HTTP_NO_CONTENT) {
                Log.i(AppConstants.ACTIVITY_TAG__TAG, "14 - HTTP_OK pour id " + achat.getId());
            } else {
                Log.e(AppConstants.ACTIVITY_TAG__TAG, "Retour " + responseCode);
                messageRetour = "Service non disponible";
            }
            return 0L;
        } catch (Exception e) {
            Log.e(AppConstants.ACTIVITY_TAG__TAG, e.getMessage(), e);
        }
        messageRetour = "Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
    }
}