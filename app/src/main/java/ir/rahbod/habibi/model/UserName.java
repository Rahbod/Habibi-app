package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class UserName {
    @SerializedName("name")
    public String name;
    @SerializedName("message")
    public String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
