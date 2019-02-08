
package com.app.admin.sellah.model.extra.ChatHeadermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("friendId")
    @Expose
    private String friendId;
    @SerializedName("friendName")
    @Expose
    private String friendName;
    @SerializedName("friendImage")
    @Expose
    private String friendImage;

    public String getIsRead() {
        return IsRead;
    }

    public void setIsRead(String isRead) {
        IsRead = isRead;
    }

    @SerializedName("IsRead")
    @Expose
    private String IsRead;
    @SerializedName("isBlocked")
    @Expose
    private String isBlocked;

    public String getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(String isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(String blockedBy) {
        this.blockedBy = blockedBy;
    }

    @SerializedName("blockedBy")
    @Expose
    private String blockedBy;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    @SerializedName("message")
    @Expose
    private String message ;
    @SerializedName("last_msg_time")
    @Expose
    private String lastMsgTime  ;
    @SerializedName("online_status")
    @Expose
    private String onlineStatus  ;

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getLastSeenTime() {
        return lastSeenTime;
    }

    public void setLastSeenTime(String lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
    }

    @SerializedName("last_seen_time")
    @Expose
    private String lastSeenTime  ;

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendImage() {
        return friendImage;
    }

    public void setFriendImage(String friendImage) {
        this.friendImage = friendImage;
    }

}
