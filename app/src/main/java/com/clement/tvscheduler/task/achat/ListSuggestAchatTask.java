package com.clement.tvscheduler.task.achat;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.clement.tvscheduler.activity.ListeCourseActivity;
import com.clement.tvscheduler.activity.TvPcActivity;
import com.clement.tvscheduler.task.BaseTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clément on 09/07/2016.
 */
public class ListSuggestAchatTask extends BaseTask {


    List<String> suggestions;

    ListeCourseActivity listeCourseActivity;

    //  private String messageRetour;

    public ListSuggestAchatTask(ListeCourseActivity listeCourseActivity) {
        super(listeCourseActivity);
        this.listeCourseActivity = listeCourseActivity;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            Log.i(TvPcActivity.TAG, "Execution " + this.getClass());
            InputStream is = getHttpUrlConnection("tvscheduler/ws-suggest-achat").getInputStream();
            readJsonStream(is);
            return 0L;
        } catch (Exception e) {
            Log.e(TvPcActivity.TAG, e.getMessage(), e);
        }
        //       messageRetour = "Service non disponible";
        return 0L;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        if (suggestions == null) {
            listeCourseActivity.showMessage("Erreur du service");
            return;
        }
        Log.i(TvPcActivity.TAG, "Achat retournés avec succès");
        for (String suggestion : suggestions) {
            Log.i(TvPcActivity.TAG, "Achat: " + suggestion);
        }
        listeCourseActivity.setSuggestAchats(suggestions);

    }

    /**
     * @param in
     * @return
     * @throws IOException
     */
    public List<String> readJsonStream(InputStream in) throws IOException {
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
    public List<String> readAchats(JsonReader reader) throws IOException {
        Log.d(TvPcActivity.TAG, "Lecture des achats suggérés");
        suggestions = new ArrayList<String>();

        reader.beginArray();
        while (reader.hasNext()) {
            String suggestion = readAchat(reader);
            suggestions.add(suggestion);
        }
        return suggestions;
    }

    private String readAchat(JsonReader reader) throws IOException {
        String name = null;
        reader.beginObject();

        while (reader.hasNext()) {
            String nameJson = reader.nextName();
            JsonToken look = reader.peek();

            if (look == JsonToken.NULL) {
                reader.skipValue();
            } else if (nameJson.equals("name")) {
                name = reader.nextString();
                Log.d(TvPcActivity.TAG, "Lecture de " + name);

            } else {
                reader.skipValue();
            }

        }
        reader.endObject();

        return name;
    }

}