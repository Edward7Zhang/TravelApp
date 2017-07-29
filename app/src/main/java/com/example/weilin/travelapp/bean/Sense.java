package com.example.weilin.travelapp.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hulinfa on 2017/1/6.
 */

public class Sense implements Serializable {

    private String senseCode;
    private String senseName;
    private String senseAddress;
    private String senseLatitude;
    private String senseLongitude;
    private String sensePhnumber;
    private String senseOpenTime;
    private String senseCloseTime;
    private ArrayList<String> sensePictures;
    private ArrayList<Comment> senseComments;

    public static class Comment implements Serializable {
        private String commentCode;
        private String commentName;
        private ArrayList<String> commentList;

        @Override
        public String toString() {
            return "Comment{" +
                    "commentCode='" + commentCode + '\'' +
                    ", commentName='" + commentName + '\'' +
                    ", commentList=" + commentList +
                    '}';
        }

        public Comment(String commentCode, String commentName, ArrayList<String> commentList) {
            this.commentCode = commentCode;
            this.commentName = commentName;
            this.commentList = commentList;
        }

        public void setCommentCode(String commentCode) {
            this.commentCode = commentCode;
        }

        public String getCommentName() {
            return commentName;
        }

        public void setCommentName(String commentName) {
            this.commentName = commentName;
        }

        public ArrayList<String> getCommentList() {
            return commentList;
        }

        public Comment() {
        }

        public void setCommentList(ArrayList<String> commentList) {
            this.commentList = commentList;
        }
    }

    public String getSenseCode() {
        return senseCode;
    }

    public void setSenseCode(String senseCode) {
        this.senseCode = senseCode;
    }

    public String getSenseName() {
        return senseName;
    }

    public void setSenseName(String senseName) {
        this.senseName = senseName;
    }

    public String getSenseAddress() {
        return senseAddress;
    }

    public void setSenseAddress(String senseAddress) {
        this.senseAddress = senseAddress;
    }

    public String getSenseLatitude() {
        return senseLatitude;
    }

    public void setSenseLatitude(String senseLatitude) {
        this.senseLatitude = senseLatitude;
    }

    public String getSenseLongitude() {
        return senseLongitude;
    }

    public void setSenseLongitude(String senseLongitude) {
        this.senseLongitude = senseLongitude;
    }

    public String getSensePhnumber() {
        return sensePhnumber;
    }

    public void setSensePhnumber(String sensePhnumber) {
        this.sensePhnumber = sensePhnumber;
    }

    public String getSenseOpenTime() {
        return senseOpenTime;
    }

    public void setSenseOpenTime(String senseOpenTime) {
        this.senseOpenTime = senseOpenTime;
    }

    public String getSenseCloseTime() {
        return senseCloseTime;
    }

    public void setSenseCloseTime(String senseCloseTime) {
        this.senseCloseTime = senseCloseTime;
    }

    public ArrayList<String> getSensePictures() {
        return sensePictures;
    }

    public void setSensePictures(ArrayList<String> sensePictures) {
        this.sensePictures = sensePictures;
    }

    public ArrayList<Comment> getSenseComments() {
        return senseComments;
    }

    public void setSenseComments(ArrayList<Comment> senseComments) {
        this.senseComments = senseComments;
    }

    @Override
    public String toString() {
        return "Sense{" +
                "senseCode='" + senseCode + '\'' +
                ", senseName='" + senseName + '\'' +
                ", senseAddress='" + senseAddress + '\'' +
                ", senseLatitude='" + senseLatitude + '\'' +
                ", senseLongitude='" + senseLongitude + '\'' +
                ", sensePhnumber='" + sensePhnumber + '\'' +
                ", senseOpenTime='" + senseOpenTime + '\'' +
                ", senseCloseTime='" + senseCloseTime + '\'' +
                ", sensePictures=" + sensePictures +
                ", senseComments=" + senseComments +
                '}';
    }

    public Sense(String senseCode, String senseName, String senseAddress, String senseLatitude, String senseLongitude, String sensePhnumber, String senseOpenTime, String senseCloseTime, ArrayList<String> sensePictures, ArrayList<Comment> senseComments) {
        this.senseCode = senseCode;
        this.senseName = senseName;
        this.senseAddress = senseAddress;
        this.senseLatitude = senseLatitude;
        this.senseLongitude = senseLongitude;
        this.sensePhnumber = sensePhnumber;
        this.senseOpenTime = senseOpenTime;
        this.senseCloseTime = senseCloseTime;
        this.sensePictures = sensePictures;
        this.senseComments = senseComments;
    }
}
