package com.clement.task.task.achat;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.activity.database.DbTaskHelper;
import com.clement.task.activity.fragment.CourseFragment;
import com.clement.task.object.Achat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clément on 09/07/2016.
 */
public class ListAchatTask extends CrudAchatTask {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    List<Achat> achats;


    //  private String messageRetour;

    public ListAchatTask(CourseFragment listeCourseActivity) {
        super(listeCourseActivity);
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            Log.i(AppConstants.ACTIVITY_TAG__TAG, "Execution " + this.getClass());

            /**
             * First we check if there are some elements to sync before doing the actual synchronisation.
             */
            List<Achat> achatsToSync = dbHelper.listAchats(true);
            if (achatsToSync.size() > 0) {
                Log.d(AppConstants.ACTIVITY_TAG__TAG, "There are some task to Sync before calling the server.");
                for (Achat achatToSync : achatsToSync
                        ) {
                    syncAchat(achatToSync);
                    Log.d(AppConstants.ACTIVITY_TAG__TAG, "Need to sync " + achatToSync.getName());
                }
            }
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
            courseFragment.showMessage("Erreur du service");
            return;
        }
        Log.i(AppConstants.ACTIVITY_TAG__TAG, "Achats retournés avec succès");
        for (Achat achat : achats) {
            Log.i(AppConstants.ACTIVITY_TAG__TAG, "Achat: " + achat.getName());
        }
        courseFragment.setAchats(achats);

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

    /**
     * Synchronize a task with server side.
     *
     * @param achatToSync
     */
    private void syncAchat(Achat achatToSync) throws Exception {
        if (achatToSync.getToCreateToDelete() == DbTaskHelper.TO_CREATE) {
            Log.i(AppConstants.ACTIVITY_TAG__TAG, "An achat " + achatToSync.getName() + " must be created on server side");
            callCreateAchatWebService(achatToSync);
        } else if (achatToSync.getToCreateToDelete() == DbTaskHelper.TO_DELETE) {
            Log.i(AppConstants.ACTIVITY_TAG__TAG, "An achat " + achatToSync.getName() + " must be deleted on server side");
                   callDeleteAchatWebService(achatToSync.getId());
        } else if (achatToSync.getToCreateToDelete() == DbTaskHelper.TO_UPDATE) {
            Log.i(AppConstants.ACTIVITY_TAG__TAG, "An achat " + achatToSync.getName() + " must be updated on server side");
                  callUpdateAchatWebService(achatToSync);
        }
    }


}