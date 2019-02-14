package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("id")
    public int id;
    @SerializedName("method")
    public String method;
    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
}
