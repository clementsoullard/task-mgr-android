package com.clement.task.database;

/**
 * Created by cleme on 06/08/2017.
 */

public interface TableDaoI {
    /**
     * This are the value to store the status of the entry in the column to create to delete.
     * TO_UPDATE means it is nothing
     */
    /**
     * Feed the column ToCreate_ToDeelete with this value, if the entry is in sync
     */
    public static Integer IN_SYNC = -1;
    /**
     * Feed the column ToCreate_ToDeelete with this value, if the entry is to update
     */
    public static Integer TO_UPDATE = 0;
    /**
     * Feed the column ToCreate_ToDeelete with this value, if the entry is to create
     */
    public static Integer TO_CREATE = 1;
    /**
     * Feed the column ToCreate_ToDeelete with this value, if the entry is to delete
     */
    public static Integer TO_DELETE = 2;


    String getCreateStatement();
    String getDropStatement();

}
