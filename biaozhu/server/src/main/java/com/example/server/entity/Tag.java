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
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;
    private String tag;
    private String tagName;
    private String x;
    private String x1;
    private String y;
    private String y1;
    private String uuid;
    private Long imgId;

    public Tag() {
    }

    public Tag(Long tagId, String tag, String tagName, String x, String x1, String y, String y1, String uuid, Long imgId) {
        this.tagId = tagId;
        this.tag = tag;
        this.tagName = tagName;
        this.x = x;
        this.x1 = x1;
        this.y = y;
        this.y1 = y1;
        this.uuid = uuid;
        this.imgId = imgId;
    }

    public Tag(String tag, String tagName, String x, String x1, String y, String y1, String uuid, Long imgId) {
        this.tag = tag;
        this.tagName = tagName;
        this.x = x;
        this.x1 = x1;
        this.y = y;
        this.y1 = y1;
        this.uuid = uuid;
        this.imgId = imgId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getX1() {
        return x1;
    }

    public void setX1(String x1) {
        this.x1 = x1;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getY1() {
        return y1;
    }

    public void setY1(String y1) {
        this.y1 = y1;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagId=" + tagId +
                ", tag='" + tag + '\'' +
                ", tagName='" + tagName + '\'' +
                ", x='" + x + '\'' +
                ", x1='" + x1 + '\'' +
                ", y='" + y + '\'' +
                ", y1='" + y1 + '\'' +
                ", uuid='" + uuid + '\'' +
                ", imgId=" + imgId +
                '}';
    }
}
