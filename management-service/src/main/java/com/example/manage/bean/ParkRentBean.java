package com.example.manage.bean;

public class ParkRentBean {
    private int id;
    private int carId;
    private String ownerId;
    private int parkId;
    private int costId;
    private String startTime;
    private String endTime;
    private ParkBean parkBean;
    private OwnerCarBean ownerCarBean;
    private RoomUserBean roomUserBean;
    private PayCostBean payCostBean;


    public PayCostBean getPayCostBean() {
        return payCostBean;
    }

    public void setPayCostBean(PayCostBean payCostBean) {
        this.payCostBean = payCostBean;
    }

    public int getCostId() {
        return costId;
    }

    public void setCostId(int costId) {
        this.costId = costId;
    }

    public ParkBean getParkBean() {
        return parkBean;
    }

    public void setParkBean(ParkBean parkBean) {
        this.parkBean = parkBean;
    }

    public OwnerCarBean getOwnerCarBean() {
        return ownerCarBean;
    }

    public void setOwnerCarBean(OwnerCarBean ownerCarBean) {
        this.ownerCarBean = ownerCarBean;
    }

    public RoomUserBean getRoomUserBean() {
        return roomUserBean;
    }

    public void setRoomUserBean(RoomUserBean roomUserBean) {
        this.roomUserBean = roomUserBean;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
