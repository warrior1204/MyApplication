package com.example.sendMessage.bean;


public class Msg {
    private int id;
    private int fesId;
    private String content;

    public Msg(int id, int fesId, String content) {
        this.id = id;
        this.fesId = fesId;
        this.content = content;
    }

    public int getfesId() {
        return fesId;
    }

    public void setfesId(int fesId) {
        this.fesId = fesId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
