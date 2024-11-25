package com.bintangjuara.bk.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Announcement implements Serializable {
    private int announcementId;
    private boolean isRead;
    private String title;
    private String content;
    private String[] imageUrl;
    private Date date;

    public Announcement(int announcementId,String title, String content, String[] imageUrl, boolean isRead, Date date) {
        this.announcementId = announcementId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.isRead = isRead;
        this.date = date;
    }

    public Announcement() {
        this.announcementId = 0;
        this.content = "";
        this.imageUrl = new String[]{};
        this.isRead = false;
    }

    public int getAnnouncementId() {
        return announcementId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String[] getImageUrl() {
        return imageUrl;
    }

    public Date getDate() {
        return date;
    }

    public String getStrDate(){
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("id", "ID"));
        return outputFormat.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead){
        this.isRead = isRead;
    }

    public void setAnnouncementId(int announcementId) {
        this.announcementId = announcementId;
    }
}
