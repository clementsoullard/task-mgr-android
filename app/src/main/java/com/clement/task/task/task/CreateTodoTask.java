package com.clement.task.task.task;

import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.database.TaskDao;
import com.clement.task.activity.fragment.TaskFragment;
import com.clement.task.object.Task;

/**
 * This task is to add an task to the liste de courses
 * Created by Clément on 09/07/2016.
 */
public class CreateTodoTask extends CrudTodoTask {


    private Task task;

    /**
     *
     *
     */

    public CreateTodoTask(TaskFragment createTaskActivity, Task task) {
        super(createTaskActivity);
        this.task = task;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            callCreateWebService(task);
            dbHelper.insertTask(task, true, TaskDao.IN_SYNC);
            return 0L;
        } catch (Exception e) {
            dbHelper.insertTask(task, false, TaskDao.TO_CREATE);
            Log.e(AppConstants.ACTIVITY_TAG__TAG, "Erreur " + e.getMessage() + " ajout manuel a la db.");
        }
        messageRetour = "Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        connectedActivity.showMessage(messageRetour);
        taskFragmentI.taskEnregistre();
    }


}