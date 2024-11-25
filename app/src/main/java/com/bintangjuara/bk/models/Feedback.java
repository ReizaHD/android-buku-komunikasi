package com.bintangjuara.bk.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Feedback extends Announcement implements Serializable {
    private final int feedbackId;
    private final int studentId;
    private final String studentName, studentClass;
    private final String weekendAssignment, additionalFeedback, extracurricular;
    private String parentFeedback;
    private final ArrayList<Subject> subjects;

    public Feedback(int announcementId, int feedbackId, int studentId, String studentName, String studentClass, ArrayList<Subject> subjects, String weekendAssignment, String additionalFeedback, String extracurricular, String parentFeedback, boolean isRead, Date date) {
        super();
        super.setAnnouncementId(announcementId);
        super.setRead(isRead);
        super.setDate(date);
        this.feedbackId = feedbackId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentClass = studentClass;
        this.subjects = subjects;
        this.weekendAssignment = weekendAssignment;
        this.additionalFeedback = additionalFeedback;
        this.parentFeedback = parentFeedback;
        this.extracurricular = extracurricular;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getAdditionalFeedback() {
        return additionalFeedback;
    }

    public String getExtracurricular() {
        return extracurricular;
    }

    public int getFeedbackId() {
        return feedbackId;
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

    public void setParentFeedback(String msg){
        parentFeedback = msg;
    }
}
