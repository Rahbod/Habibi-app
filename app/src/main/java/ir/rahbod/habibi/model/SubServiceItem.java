package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class SubServiceItem {
    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("parent")
    public int parentID;
    @SerializedName("icon")
    public String icon;
}
