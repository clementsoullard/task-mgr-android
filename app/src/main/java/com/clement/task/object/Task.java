package com.clement.task.object;

/**
 * Created by cleme on 29/10/2016.
 */
public class Task {

    private String name;

    private String id;

    private Boolean done;
    /**
     * Whether a task is temporary or not
     */
    private Boolean temporary;
    /**
     * The synchronisation to apply
     */
    private int toCreateToDelete;

    private String owner;

    public void setDone(Boolean done) {
        this.done = done;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDone() {

        if (done == null) {
            return false;
        }
        return done;
    }

    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }

    public Boolean getTemporary() {
        if (temporary == null) {
            return false;
        }
        return temporary;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getToCreateToDelete() {
        return toCreateToDelete;
    }

    public void setToCreateToDelete(int toCreateToDelete) {
        this.toCreateToDelete = toCreateToDelete;
    }
}
