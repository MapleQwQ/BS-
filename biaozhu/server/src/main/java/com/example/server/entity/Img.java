package com.example.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Img {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgId;
    private Long taskId;
    private String pictureAddress;
    private int imgState;      //=0表示未标注，=1表示已标注

    public Img() {
    }

    public Img(Long imgId, Long taskId, String pictureAddress, int imgState) {
        this.imgId = imgId;
        this.taskId = taskId;
        this.pictureAddress = pictureAddress;
        this.imgState = imgState;
    }

    public Img(Long taskId, String pictureAddress, int imgState) {
        this.taskId = taskId;
        this.pictureAddress = pictureAddress;
        this.imgState = imgState;
    }

    public int getImgState() {
        return imgState;
    }

    public void setImgState(int imgState) {
        this.imgState = imgState;
    }

    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getPictureAddress() {
        return pictureAddress;
    }

    public void setPictureAddress(String pictureAddress) {
        this.pictureAddress = pictureAddress;
    }

    @Override
    public String toString() {
        return "Img{" +
                "imgId=" + imgId +
                ", taskId=" + taskId +
                ", pictureAddress='" + pictureAddress + '\'' +
                ", imgState=" + imgState +
                '}';
    }
}
