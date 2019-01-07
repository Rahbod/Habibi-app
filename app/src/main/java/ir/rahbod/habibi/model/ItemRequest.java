package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class ItemRequest {
    @SerializedName("id")
    public int deviceID;
    @SerializedName("device")
    public String device;
    @SerializedName("date")
    public String date;
    @SerializedName("status")
    public String status;
}
