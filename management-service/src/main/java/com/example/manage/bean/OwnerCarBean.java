package com.example.manage.bean;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OwnerCarBean {
    private int id;
    @NotBlank(message = "车牌号不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5][A-Z][A-Z0-9]{5,6}$", message = "车牌号格式不正确")
    private String carNumber;
    @NotBlank(message = "品牌不能为空")
    private String carBrand;
    private String carColor;
    private String ownerId;
    private String ownerName;
    private String ownerIdCard;
    private int carType;


    public String getOwnerIdCard() {
        return ownerIdCard;
    }

    public void setOwnerIdCard(String ownerIdCard) {
        this.ownerIdCard = ownerIdCard;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getCarType() {
        return carType;
    }

    public void setCarType(int carType) {
        this.carType = carType;
    }
}
