package com.bintangjuara.bk.models;

import java.io.Serializable;

public class Subject implements Serializable {
    String subjectName, message;

    public Subject(String subjectName, String message) {
        this.message = message;
        this.subjectName = subjectName;
    }

    public String getMessage() {
        return message;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
