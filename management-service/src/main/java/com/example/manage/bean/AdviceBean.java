package com.example.manage.bean;

public class AdviceBean {
    private int id;
    private String uid;
    private String fileNames;
    private String advice;
    private String name;
    private String phone;
    private long time;


    public String getFileNames() {
        return fileNames;
    }

    public void setFileNames(String fileNames) {
        this.fileNames = fileNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
