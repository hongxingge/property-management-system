package com.example.manage.bean;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ParkBean {
    private int id;
    @NotBlank(message = "车位编号不能为空")
    private String number;
    private int type;
    private int carportId;
    private String carportName;
    @Positive(message = "面积必须大于0")
    private int area;
    private String remark;

    public int getCarportId() {
        return carportId;
    }

    public void setCarportId(int carportId) {
        this.carportId = carportId;
    }

    public String getCarportName() {
        return carportName;
    }

    public void setCarportName(String carportName) {
        this.carportName = carportName;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
