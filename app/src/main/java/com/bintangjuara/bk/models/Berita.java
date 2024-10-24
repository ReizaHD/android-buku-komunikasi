package com.bintangjuara.bk.models;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class Berita implements Serializable {
    private String id;
    private String namaSiswa = "Abqory Fusena Anarghya Setiadi";
    private String kelas = "Kelas 1A";
    private String periode = ": 7 - 11 Oktober 2024";
    private String tugasWeekend = "Assalamu'alaikum Ayah Bunda..."+
            "Terimakasih atas feedback pekan lalu. Alhamdulillah Kak Sena sudah mulai belajar naik sepeda roda dua ketika di rumah."+
            "Semoga semakin lancar. Pekan ini Kakak mengikuti kegiatan P5 dalam tahap pengenalan. Kakak dikenalkan dengan jenis-jenis sampah (organik."+
            "anorganik dan B3). Kakak mendapatkan oleh-oleh untuk menuntaskan tantangan pada lembar kerja yang ditempel pada"+
            "buku P5. Mohon pendampingan Ayah Bunda agar Kakak menuntaskan tantangan yang telah diberikan.";
    private String catatan = "Assalamu'alaikum Ayah Bunda...\n" +
            "Terimakasih atas feedback pekan lalu. Alhamdulillah Kak Sena sudah mulai belajar naik sepeda roda dua ketika di rumah.\n" +
            "Semoga semakin lancar.\n" +
            "Pekan ini Kakak mengikuti kegiatan P5 dalam tahap pengenalan. Kakak dikenalkan dengan jenis-jenis sampah (organik.\n" +
            "anorganik dan B3). Kakak mendapatkan oleh-oleh untuk menuntaskan tantangan pada lembar kerja yang ditempel pada\n" +
            "buku P5. Mohon pendampingan Ayah Bunda agar Kakak menuntaskan tantangan yang telah diberikan.\n";
    private String ekstrakurikuler = "Bahasa Inggris menuntaskan tantangan pada workbook halaman 15 Bahasa Indonesia menuntaskan tantangan"+
            " untuk menceritakan kembali isi cerita \"Naik-Naik Ke Puncak Bukit\"\nP5 Menuntaskan tantangan pada lembar kerja di buku P5";
    private String catatanOrtu = "Ayah Bunda dapat memberikan umpan balik berupa :\n" +
            "1. Kegiatan keagamaan yang dilakukan di rumah. (Shalat, wudhu, mengaji, muraja'ah, puasa dan lainnya)\n" +
            "2. Kegiatan stimulasi fisik, motorik kasar atau motorik halus yang dilakukan Ayah Bunda untuk Kakak di rumah\n" +
            "3. Kegiatan stimulasi literasi dan numerasi yang dilakukan di rumah\n" +
            "4. Kegiatan rumah tangga yang dilakukan Kakak di rumah\n" +
            "5. Pengalaman bermakna tentang Kakak selama sepekan yang terjadi di luar kegiatan sekolah.\n" +
            "6. Perkembangan sosial emosi yang terjadi di rumah.";
    private ArrayList<Pelajaran> pembelajaran;
    private String balasan;
    private boolean sudahDibaca;

    public boolean isSudahDibaca() {
        return sudahDibaca;
    }

    public void setSudahDibaca(boolean sudahDibaca) {
        this.sudahDibaca = sudahDibaca;
    }

    public Berita() {
        pembelajaran = new ArrayList<>();
        pembelajaran.add(new Pelajaran("Pendidikan Pancasila", "Pekan ini Kak Sena belajar tentang aktivitas yang dapat dilakukan secara mandiri di rumah. Alhamdulillah Kak Sena dapat"+
                "menyebutkan kegiatan yang dapat dilakukan di rumah. Kakak membutuhkan penguatan untuk mengidentifikasi aktivitas yang"+
                "dapat dilakukan secara mandiri maupun masih membutuhkan bantuan orang tua."));
        pembelajaran.add(new Pelajaran("Bahasa Inggris",
                "Pekan ini Kakak recalling tentang penggunaan kata that is dan that are pada suatu kalimat. Kakak kemudian dikenalkan"+
                        "dengan kosakata number dan shape.\n"+"Alhamdulillah Kak Sena dapat menuliskan kalimat number dan shape dari yang disajikan dengan bimbingan guru."));
    }

    public Berita(String catatan) {
        this.catatan = catatan;
        pembelajaran = new ArrayList<>();
        pembelajaran.add(new Pelajaran("Pendidikan Pancasila", "Pekan ini Kak Sena belajar tentang aktivitas yang dapat dilakukan secara mandiri di rumah. Alhamdulillah Kak Sena dapat"+
                "menyebutkan kegiatan yang dapat dilakukan di rumah. Kakak membutuhkan penguatan untuk mengidentifikasi aktivitas yang"+
                "dapat dilakukan secara mandiri maupun masih membutuhkan bantuan orang tua."));
        pembelajaran.add(new Pelajaran("Bahasa Inggris",
                "Pekan ini Kakak recalling tentang penggunaan kata that is dan that are pada suatu kalimat. Kakak kemudian dikenalkan"+
                        "dengan kosakata number dan shape.\n"+"Alhamdulillah Kak Sena dapat menuliskan kalimat number dan shape dari yang disajikan dengan bimbingan guru."));
    }

    public ArrayList<Pelajaran> getPembelajaran() {
        return pembelajaran;
    }

    public void setPembelajaran(ArrayList<Pelajaran> pembelajaran) {
        this.pembelajaran = pembelajaran;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getCatatanOrtu() {
        return catatanOrtu;
    }

    public void setCatatanOrtu(String catatanOrtu) {
        this.catatanOrtu = catatanOrtu;
    }

    public String getEkstrakurikuler() {
        return ekstrakurikuler;
    }

    public void setEkstrakurikuler(String ekstrakurikuler) {
        this.ekstrakurikuler = ekstrakurikuler;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getNamaSiswa() {
        return namaSiswa;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.namaSiswa = namaSiswa;
    }

    public String getTugasWeekend() {
        return tugasWeekend;
    }

    public void setTugasWeekend(String tugasWeekend) {
        this.tugasWeekend = tugasWeekend;
    }

    public String getBalasan() {
        return balasan;
    }

    public void setBalasan(String balasan) {
        this.balasan = balasan;
    }

    public Berita(String id, String tugasWeekend, String catatan, String ekstrakurikuler, String catatanOrtu, ArrayList<Pelajaran> pembelajaran) {
        this.id = id;
        this.tugasWeekend = tugasWeekend;
        this.catatan = catatan;
        this.ekstrakurikuler = ekstrakurikuler;
        this.catatanOrtu = catatanOrtu;
        this.pembelajaran = pembelajaran;
    }

    public String getId() {
        return id;
    }
}

//    public String getTanggalText() {
//        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
//        return sdf.format(tanggal);
//    }
//
//    public void setTanggal(String tanggal) {
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//            this.tanggal = dateFormat.parse(tanggal);
//        }catch (ParseException e){
//            e.printStackTrace();
//        }
//    }
