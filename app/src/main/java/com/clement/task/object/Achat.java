package com.clement.task.object;

/**
 * Created by cleme on 29/10/2016.
 */
public class Achat {
    String name;

    String id;

    Boolean done;


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

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
