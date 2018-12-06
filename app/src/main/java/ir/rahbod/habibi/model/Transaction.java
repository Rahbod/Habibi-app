package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class Transaction {
    @SerializedName("amount")
    public String amount;
    @SerializedName("date")
    public String date;
    @SerializedName("code")
    public String code;
    @SerializedName("status")
    public String status;
}
