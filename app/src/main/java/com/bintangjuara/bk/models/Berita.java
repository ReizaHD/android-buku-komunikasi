package com.bintangjuara.bk.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Berita implements Serializable {
    private int feedbackId;
    private int studentId;
    private String studentName, studentClass;
    private String weekendAssignment, additionalFeedback, extracurricular, parentFeedback;
    private boolean isRead;
    private Date date;
    private ArrayList<Subject> subjects;

    public Berita(int feedbackId, int studentId, String studentName, String studentClass, ArrayList<Subject> subjects, String weekendAssignment, String additionalFeedback, String extracurricular, String parentFeedback, boolean isRead, Date date) {
        this.feedbackId = feedbackId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentClass = studentClass;
        this.subjects = subjects;
        this.weekendAssignment = weekendAssignment;
        this.additionalFeedback = additionalFeedback;
        this.parentFeedback = parentFeedback;
        this.extracurricular = extracurricular;
        this.isRead = isRead;
        this.date = date;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getAdditionalFeedback() {
        return additionalFeedback;
    }

    public Date getDate() {
        return date;
    }

    public String getStrDate(){
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("id", "ID"));
        return outputFormat.format(date);
    }

    public String getExtracurricular() {
        return extracurricular;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getParentFeedback() {
        return parentFeedback;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getStudentName() {
        return studentName;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public String getWeekendAssignment() {
        return weekendAssignment;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setParentFeedback(String msg){
        parentFeedback = msg;
    }
}
