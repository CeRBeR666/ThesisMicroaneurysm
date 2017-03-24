package com.agaoglu.tez;

/**
 * Created by Volkan on 19.03.2017.
 */

public class tetkik {
    public String hastaID;
    public String tetkikPath;
    public String diabetoran;
    public String hastaad;

    public tetkik(){

    }



    public tetkik(String hastaID, String tetkikPath, String hastaad, String diabetoran) {
        this.hastaID = hastaID;
        this.tetkikPath = tetkikPath;
    }

    public String getHastaID() {
        return hastaID;
    }

    public void setHastaID(String hastaID) {
        this.hastaID = hastaID;
    }

    public String gettetkikPath() {
        return tetkikPath;
    }

    public void settetkikPath(String tetkikPath) {
        this.tetkikPath = tetkikPath;
    }

    public String getDiabetoran() {
        return diabetoran;
    }

    public void setDiabetoran(String diabetoran) {
        this.diabetoran = diabetoran;
    }

    public String getHastaad() {
        return hastaad;
    }

    public void setHastaad(String hastaad) {
        this.hastaad = hastaad;
    }
}
