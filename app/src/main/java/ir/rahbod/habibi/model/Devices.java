package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class Devices {
    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("icon")
    public String icon;
    @SerializedName("hasChild")
    public boolean hasChild;
}
