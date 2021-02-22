package com.example.note.model;

public class Note {
    private Integer id;                 //Id
    private String title;               //Tiêu đề
    private String content;             //Nội dung
    private String timeCreate;          //Thời gian khởi tạo
    private int color;                  //Màu nền

    public Note(Integer id, String title, String content, String timeCreate, int color) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timeCreate = timeCreate;
        this.color = color;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(String timeCreate) {
        this.timeCreate = timeCreate;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
