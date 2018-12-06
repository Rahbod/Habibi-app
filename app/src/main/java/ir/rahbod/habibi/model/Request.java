package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class Request {
    String name;
    String model;
    String condition;
    @SerializedName("deviceID")
    public int deviceID;
    @SerializedName("addressID")
    public int addressID;
    @SerializedName("description")
    public String description;
    @SerializedName("date")
    public String date;
    @SerializedName("time")
    public String time;
    @SerializedName("message")
    public String message;

    public Request() {
   }

    public Request(String name, String model, String condition) {
        this.name = name;
        this.model = model;
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
