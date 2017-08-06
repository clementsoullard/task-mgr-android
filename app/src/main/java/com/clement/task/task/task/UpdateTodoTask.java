package com.clement.task.task.task;

import android.util.Log;

import com.clement.task.AppConstants;
import com.clement.task.activity.TaskListFragmentI;
import com.clement.task.database.TableDaoI;
import com.clement.task.object.Task;

/**
 * Created by Clément on 09/07/2016.
 */
public class UpdateTodoTask extends CrudTodoTask {


    private Task task;


    public UpdateTodoTask(TaskListFragmentI mainActivity, Task task) {
        super(mainActivity);
        this.task = task;

    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            callUpdateWebService(task);
            return 0L;
        } catch (Exception e) {
            Log.e(AppConstants.ACTIVITY_TAG__TAG, e.getMessage() + "  - False - HTTP_OK updating db marking to sync");
            dbHelper.updateTask(task.getId(), task.getDone(), TableDaoI.TO_UPDATE);
            messageRetour = "Service non disponible";

            Log.e(AppConstants.ACTIVITY_TAG__TAG, e.getMessage(), e);
        }
        messageRetour = "Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        connectedActivity.showMessage(messageRetour);
    }
}