package com.example.bk.models;

public class Pemberitahuan {
    private String isiPesan, guru, murid, tanggal;
    private boolean sudahDibaca;

    public Pemberitahuan(String guru, String isiPesan, String tanggal, String murid, boolean sudahDibaca) {
        this.guru = guru;
        this.isiPesan = isiPesan;
        this.murid = murid;
        this.tanggal = tanggal;
        this.sudahDibaca = sudahDibaca;
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

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public boolean isSudahDibaca() {
        return sudahDibaca;
    }

    public void setSudahDibaca(boolean sudahDibaca) {
        this.sudahDibaca = sudahDibaca;
    }
}
