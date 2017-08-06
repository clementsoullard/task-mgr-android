package com.clement.task.database;

import com.clement.task.object.Achat;
import com.clement.task.object.Task;

import java.util.List;

/**
 * Created by cleme on 04/08/2017.
 */

public interface DbHelperI {

    void clearAchat();

    void clearTask();


    void insertAchat(Achat achat, boolean inSync);

    List<Task> listTasks(boolean onlyTaskToSync);

    void insertTask(Task task, boolean inSync, int typeofMod);

    void updateTask(String mongoId, Boolean done, Integer toCreateToDelete);

    void markTaskForDeletion(String taskId);

    /**
     *
     * The achat Section
     * @param onlyAchatToSync
     * @return
     */
    List<Achat> listAchats(boolean onlyAchatToSync);

    void markAchatForDeletion(String achatId);

    void updateAchat(Achat achat, boolean inSync);
}
