package com.example.manage.bean;

import jakarta.validation.constraints.NotBlank;

public class ClientBannerBean {
    private int id;
    @NotBlank(message = "标题不能为空")
    private String title;
    private String description;
    @NotBlank(message = "图片不能为空")
    private String img;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
