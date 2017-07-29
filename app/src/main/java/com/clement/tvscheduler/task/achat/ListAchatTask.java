package com.clement.tvscheduler.task.achat;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.clement.tvscheduler.TVSchedulerConstants;
import com.clement.tvscheduler.activity.ListeCourseActivity;
import com.clement.tvscheduler.object.Achat;
import com.clement.tvscheduler.task.BaseTask;

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

    ListeCourseActivity listeCourseActivity;

    //  private String messageRetour;

    public ListAchatTask(ListeCourseActivity listeCourseActivity) {
        super(listeCourseActivity);
        this.listeCourseActivity = listeCourseActivity;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            Log.i(TVSchedulerConstants.ACTIVITY_TAG__TAG, "Execution " + this.getClass());
            InputStream is = getHttpUrlConnection("/tvscheduler/ws-active-achat").getInputStream();
            readJsonStream(is);
            //     messageRetour = "Succès";

            return 0L;
        } catch (Exception e) {
            Log.e(TVSchedulerConstants.ACTIVITY_TAG__TAG, e.getMessage(), e);
        }
        //       messageRetour = "Service non disponible";
        return 0L;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        if (achats == null) {
            listeCourseActivity.showMessage("Erreur du service");
            return;
        }
        Log.i(TVSchedulerConstants.ACTIVITY_TAG__TAG, "Achat retournés avec succès");
        for (Achat achat : achats) {
            Log.i(TVSchedulerConstants.ACTIVITY_TAG__TAG, "Achat: " + achat.getName());
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
        Log.d(TVSchedulerConstants.ACTIVITY_TAG__TAG, "Decryptage des acahts en cours");
        achats = new ArrayList<Achat>();

        reader.beginArray();
        while (reader.hasNext()) {
            Achat achat = readAchat(reader);
            achats.add(achat);
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