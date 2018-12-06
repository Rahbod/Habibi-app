package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class ItemRequest {
    @SerializedName("id")
    public int id;
    @SerializedName("deviceID")
    public int deviceID;
    @SerializedName("addressID")
    public int addressID;
    @SerializedName("createDate")
    public String createDate;
    @SerializedName("description")
    public String description;
    @SerializedName("requestedDate")
    public String requestedDate;
    @SerializedName("requestedTime")
    public String requestedTime;
    @SerializedName("status")
    public String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
