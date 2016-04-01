package com.liangxun.university.entity;

/**
 * Created by Administrator on 2015/4/6.
 */
public class PKComment {
    private String id;
    private String zpId;
    private String empId;
    private String commentCont;
    private String fPlid;
    private String dateline;

    private String nickName;//评论人昵称
    private String cover;//评论人头像
    private String fNickName;//父评论者昵称

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getfNickName() {
        return fNickName;
    }

    public void setfNickName(String fNickName) {
        this.fNickName = fNickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZpId() {
        return zpId;
    }

    public void setZpId(String zpId) {
        this.zpId = zpId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getCommentCont() {
        return commentCont;
    }

    public void setCommentCont(String commentCont) {
        this.commentCont = commentCont;
    }

    public String getfPlid() {
        return fPlid;
    }

    public void setfPlid(String fPlid) {
        this.fPlid = fPlid;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }
}
