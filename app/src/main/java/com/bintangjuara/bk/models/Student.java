package com.bintangjuara.bk.models;

public class Student {
    private String name, className;
    private int id;

    public Student(int id, String name, String className) {
        this.className = className;
        this.id = id;
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
