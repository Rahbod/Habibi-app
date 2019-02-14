package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class UserName {
    @SerializedName("name")
    public String name;
    @SerializedName("message")
    public String message;
    @SerializedName("token")
    public String regToken;
    @SerializedName("reagent")
    public String reagent;
    @SerializedName("reagentCode")
    public String reagentCode;
}
