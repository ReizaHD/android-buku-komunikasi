package com.bintangjuara.bk.models;

import java.io.Serializable;

public class Pelajaran implements Serializable {
    String mataPelajaran, isi;

    public Pelajaran(String mataPelajaran, String isi) {
        this.isi = isi;
        this.mataPelajaran = mataPelajaran;
    }

    public String getIsi() {
        return isi;
    }

    public String getMataPelajaran() {
        return mataPelajaran;
    }
}
