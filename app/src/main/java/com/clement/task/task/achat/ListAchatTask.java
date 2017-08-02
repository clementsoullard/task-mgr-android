package com.clement.task.task.achat;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.activity.fragment.CourseFragment;
import com.clement.task.object.Achat;
import com.clement.task.task.BaseTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clément on 09/07/2016.
 */
public class ListAchatTask extends BaseTask {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    List<Achat> achats;

    CourseFragment listeCourseActivity;

    //  private String messageRetour;

    public ListAchatTask(CourseFragment listeCourseActivity) {
        super(listeCourseActivity, listeCourseActivity.getTaskSQLiteHelper());
        this.listeCourseActivity = listeCourseActivity;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            Log.i(AppConstants.ACTIVITY_TAG__TAG, "Execution " + this.getClass());
            InputStream is = getHttpUrlConnection("/tvscheduler/ws-active-achat").getInputStream();
            readJsonStream(is);
            return 0L;
        } catch (Exception e) {
            Log.e(AppConstants.ACTIVITY_TAG__TAG, e.getMessage() + " reading the achat from the database");
            achats = dbHelper.listAchats(false);

        }
        return 0L;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        if (achats == null) {
            listeCourseActivity.showMessage("Erreur du service");
            return;
        }
        Log.i(AppConstants.ACTIVITY_TAG__TAG, "Achat retournés avec succès");
        for (Achat achat : achats) {
            Log.i(AppConstants.ACTIVITY_TAG__TAG, "Achat: " + achat.getName());
        }
        listeCourseActivity.setAchats(achats);

    }

    /**
     * @param in
     * @return
     * @throws IOException
     */
    public List<Achat> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {

            return readAchats(reader);
        } finally {
            reader.close();
        }
    }

    /**
     * This reads the json.
     *
     * @param reader
     * @return
     * @throws IOException
     */
    public List<Achat> readAchats(JsonReader reader) throws IOException {
        Log.d(AppConstants.ACTIVITY_TAG__TAG, "Decryptage des acahts en cours");
        achats = new ArrayList<Achat>();

        reader.beginArray();
        dbHelper.clearAchat();
        while (reader.hasNext()) {
            Achat achat = readAchat(reader);
            achats.add(achat);
            dbHelper.insertAchat(achat, true);

        }
        return achats;
    }

    private Achat readAchat(JsonReader reader) throws IOException {
        Achat achat = new Achat();
        reader.beginObject();
        String name = null;
        String id = null;
        String date = null;
        String owner = null;
        Boolean done = null;


        while (reader.hasNext()) {
            String nameJson = reader.nextName();
            JsonToken look = reader.peek();

            if (look == JsonToken.NULL) {
                reader.skipValue();
            } else if (nameJson.equals("name")) {
                name = reader.nextString();
            } else if (nameJson.equals("done")) {
                done = reader.nextBoolean();
            } else if (nameJson.equals("idr")) {
                id = reader.nextString();
            } else if (nameJson.equals("date")) {
                date = reader.nextString();
            } else {
                reader.skipValue();
            }

        }
        reader.endObject();
        achat.setDone(done);
        achat.setName(name);
        achat.setId(id);

        return achat;
    }

}