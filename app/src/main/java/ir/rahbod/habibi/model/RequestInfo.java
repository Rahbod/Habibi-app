package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class RequestInfo {
    @SerializedName("id")
    public int ID;
    @SerializedName("description")
    public String description;
    @SerializedName("requestedDate")
    public String requestedDate;
    @SerializedName("requestedTime")
    public String requestedTime;
    @SerializedName("status")
    public String status;
    @SerializedName("device")
    public String deviceTitle;
    @SerializedName("address")
    public String address;
    @SerializedName("repairMan")
    public RepairMan repairMan;
    @SerializedName("invoice")
    public Invoice invoice;
    @SerializedName("message")
    public String message;
    @SerializedName("serviceDate")
    public String serviceDate;
    @SerializedName("serviceTime")
    public String serviceTime;
    @SerializedName("cancelable")
    public boolean cancelable;
}
