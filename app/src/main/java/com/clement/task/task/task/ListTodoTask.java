package com.clement.task.task.task;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.activity.TaskListFragmentI;
import com.clement.task.activity.database.DbTaskHelper;
import com.clement.task.object.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clément on 09/07/2016.
 */
public class ListTodoTask extends CrudTodoTask {

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private List<Task> tasks;


    public ListTodoTask(TaskListFragmentI mainActivity) {
        super(mainActivity);
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            Log.i(AppConstants.ACTIVITY_TAG__TAG, "Execution " + this.getClass());
            List<Task> tasksToSync = dbHelper.listTasks(true);
            if (tasksToSync.size() > 0) {
                Log.d(AppConstants.ACTIVITY_TAG__TAG, "There are some task to Sync before calling the server.");
                for (Task taskToSync : tasksToSync
                        ) {
                    syncTask(taskToSync);
                    Log.d(AppConstants.ACTIVITY_TAG__TAG, "Need to sync " + taskToSync.getName());
                }
            }
            String uri = "/tvscheduler/today-tasks";
            InputStream is = getHttpUrlConnection(uri).getInputStream();
            readJsonStream(is);
            messageRetour = "Succès";
            return 0L;
        } catch (Exception e) {
            tasks = dbHelper.listTasks(false);
            Log.e(AppConstants.ACTIVITY_TAG__TAG, e.getMessage() + " récupération des tache par la base de données");
        }
        messageRetour = "Service non disponible";
        return 0L;
    }

    /**
     * Synchronize a task with server side.
     *
     * @param taskToSync
     */
    private void syncTask(Task taskToSync) throws Exception {
        if (taskToSync.getToCreateToDelete() == DbTaskHelper.TO_CREATE) {
            Log.i(AppConstants.ACTIVITY_TAG__TAG, "A task " + taskToSync.getName() + " must be created on server side");
            callCreateWebService(taskToSync);
        } else if (taskToSync.getToCreateToDelete() == DbTaskHelper.TO_DELETE) {
            Log.i(AppConstants.ACTIVITY_TAG__TAG, "A task " + taskToSync.getName() + " must be deleted on server side");
            callDelWebService(taskToSync.getId());
        } else if (taskToSync.getToCreateToDelete() == DbTaskHelper.TO_UPDATE) {
            Log.i(AppConstants.ACTIVITY_TAG__TAG, "A task " + taskToSync.getName() + " must be updated on server side");
            callUpdateWebService(taskToSync);
        }
    }


    @Override
    protected void onPostExecute(Long aLong) {
        Log.i(AppConstants.ACTIVITY_TAG__TAG, "Taches retournées avec succès");
        if (tasks == null) {
            taskFragmentI.showMessage("Erreur de service");
            return;
        }
        for (Task task : tasks) {
            Log.i(AppConstants.ACTIVITY_TAG__TAG, "Tache: " + task.getName());
        }
        taskFragmentI.setTodos(tasks);

    }

    /**
     * @param in
     * @return
     * @throws IOException
     */
    public List<Task> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {

            return readTasks(reader);
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
    public List<Task> readTasks(JsonReader reader) throws IOException {
        Log.d(AppConstants.ACTIVITY_TAG__TAG, "Decryptage des Task du jour");
        tasks = new ArrayList<Task>();
        /**
         * Saving into the database.
         */

        reader.beginArray();
        dbHelper.clearTask();


        while (reader.hasNext()) {
            Task task = readTask(reader);
            tasks.add(task);
            dbHelper.insertTask(task, true, DbTaskHelper.IN_SYNC);
        }
        return tasks;
    }

    /**
     * Read a task from the JSON flow.
     *
     * @param reader
     * @return
     * @throws IOException
     */
    private Task readTask(JsonReader reader) throws IOException {

        Task task = new Task();
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
            } else if (nameJson.equals("taskName")) {
                name = reader.nextString();
            } else if (nameJson.equals("done")) {
                done = reader.nextBoolean();
            } else if (nameJson.equals("idr")) {
                id = reader.nextString();
            } else if (nameJson.equals("date")) {
                date = reader.nextString();
            } else if (nameJson.equals("owner")) {
                owner = reader.nextString();
            } else {
                reader.skipValue();
            }

        }
        reader.endObject();
        task.setDone(done);
        task.setName(name);
        task.setOwner(owner);
        task.setId(id);

        return task;
    }

}