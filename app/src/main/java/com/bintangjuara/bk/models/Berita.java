package com.bintangjuara.bk.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Berita implements Serializable {
    private String isiPesan, guru, murid;
    private Date tanggal;
    private boolean sudahDibaca;

    public Berita(String guru, String isiPesan, String tanggal, String murid, boolean sudahDibaca){
        this.guru = guru;
        this.isiPesan = isiPesan;
        this.murid = murid;
        this.sudahDibaca = sudahDibaca;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            this.tanggal = dateFormat.parse(tanggal);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    public String getGuru() {
        return guru;
    }

    public void setGuru(String guru) {
        this.guru = guru;
    }

    public String getIsiPesan() {
        return isiPesan;
    }

    public void setIsiPesan(String isiPesan) {
        this.isiPesan = isiPesan;
    }

    public String getMurid() {
        return murid;
    }

    public void setMurid(String murid) {
        this.murid = murid;
    }

    public String getTanggalText() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        return sdf.format(tanggal);
    }

    public void setTanggal(String tanggal) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            this.tanggal = dateFormat.parse(tanggal);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    public boolean isSudahDibaca() {
        return sudahDibaca;
    }

    public void setSudahDibaca(boolean sudahDibaca) {
        this.sudahDibaca = sudahDibaca;
    }
}
