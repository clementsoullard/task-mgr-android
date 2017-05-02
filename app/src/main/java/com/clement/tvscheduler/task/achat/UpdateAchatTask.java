package com.clement.tvscheduler.task.achat;

import android.util.Log;

import com.clement.tvscheduler.activity.ListeCourseActivity;
import com.clement.tvscheduler.activity.TvPcActivity;
import com.clement.tvscheduler.object.Achat;
import com.clement.tvscheduler.task.BaseTask;

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


    public UpdateAchatTask(ListeCourseActivity listeCourseActivity, Achat achat) {
        super(listeCourseActivity);
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
                Log.i(TvPcActivity.TAG, "14 - HTTP_OK pour id " + achat.getId());
            } else {
                Log.e(TvPcActivity.TAG, "Retour " + responseCode);
                messageRetour = "Service non disponible";
            }
            return 0L;
        } catch (Exception e) {
            Log.e(TvPcActivity.TAG, e.getMessage(), e);
        }
        messageRetour = "Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
    }
}