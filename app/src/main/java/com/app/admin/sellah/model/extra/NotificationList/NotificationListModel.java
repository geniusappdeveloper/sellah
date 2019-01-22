
package com.app.admin.sellah.model.extra.NotificationList;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationListModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getListReadStatus() {
        return listReadStatus;
    }

    public void setListReadStatus(String listReadStatus) {
        this.listReadStatus = listReadStatus;
    }

    @SerializedName("list_read_status")
    @Expose
    private String listReadStatus;
    @SerializedName("arr_follow")
    @Expose
    private List<ArrFollow> arrFollow = null;
    @SerializedName("arr_announcements")
    @Expose
    private List<ArrAnnouncement> arrAnnouncements = null;
    @SerializedName("arr_post")
    @Expose
    private List<ArrPost> arrPost = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ArrFollow> getArrFollow() {
        return arrFollow;
    }

    public void setArrFollow(List<ArrFollow> arrFollow) {
        this.arrFollow = arrFollow;
    }

    public List<ArrAnnouncement> getArrAnnouncements() {
        return arrAnnouncements;
    }

    public void setArrAnnouncements(List<ArrAnnouncement> arrAnnouncements) {
        this.arrAnnouncements = arrAnnouncements;
    }

    public List<ArrPost> getArrPost() {
        return arrPost;
    }

    public void setArrPost(List<ArrPost> arrPost) {
        this.arrPost = arrPost;
    }

}
