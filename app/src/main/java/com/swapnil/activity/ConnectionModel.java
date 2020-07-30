package com.swapnil.activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 30/7/20 :July : 2020 on 10 : 14.
 */
public class ConnectionModel {
    @SerializedName("facebook_id")
    @Expose
    private String facebook_id;
    @SerializedName("device_contact_name")
    @Expose
    private String deviceContactName;
    @SerializedName("is_connected")
    @Expose
    private Integer isConnected;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public Integer getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(Integer isConnected) {
        this.isConnected = isConnected;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getDeviceContactName() {
        return deviceContactName;
    }

    public void setDeviceContactName(String deviceContactName) {
        this.deviceContactName = deviceContactName;
    }
}
