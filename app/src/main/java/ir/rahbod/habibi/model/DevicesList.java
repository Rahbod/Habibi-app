package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DevicesList {
    @SerializedName("list")
    public List<Devices> list;
    @SerializedName("credit")
    public int credit;
    @SerializedName("showCredit")
    public String showCredit;
}
