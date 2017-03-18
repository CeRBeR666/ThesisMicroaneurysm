package com.agaoglu.tez;

/**
 * Created by Volkan on 12.03.2017.
 */

public class hasta {
    public String isim;
    public String dogtar;
    public String adres;
    public String telefon;
    public String cinsiyet;

    public hasta(){

    }

    public hasta(String isim, String dogtar, String adres, String telefon, String cinsiyet){
        this.isim = isim;
        this.dogtar = dogtar;
        this.adres = adres;
        this.telefon = telefon;
        this.cinsiyet = cinsiyet;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public void setDogtar(String dogtar) {
        this.dogtar = dogtar;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getIsim() {
        return isim;
    }

    public String getDogtar() {
        return dogtar;
    }

    public String getAdres() {
        return adres;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        this.cinsiyet = cinsiyet;
    }
}
