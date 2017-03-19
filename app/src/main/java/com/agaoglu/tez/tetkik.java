package com.agaoglu.tez;

/**
 * Created by Volkan on 19.03.2017.
 */

public class tetkik {
    public String hastaID;
    public String tetkikpath;

    public tetkik(){

    }

    public tetkik(String hastaID, String tetkikpath) {
        this.hastaID = hastaID;
        this.tetkikpath = tetkikpath;
    }

    public String getHastaID() {
        return hastaID;
    }

    public void setHastaID(String hastaID) {
        this.hastaID = hastaID;
    }

    public String getTetkikpath() {
        return tetkikpath;
    }

    public void setTetkikpath(String tetkikpath) {
        this.tetkikpath = tetkikpath;
    }
}
