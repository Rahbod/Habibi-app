package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Invoice {
    @SerializedName("id")
    public int id;
    @SerializedName("description")
    public String description;
    @SerializedName("status")
    public String status;
    @SerializedName("tariffs")
    public List<Factor> factors;
    @SerializedName("sum")
    public String sum;
    @SerializedName("finalCost")
    public String finalCost;
    @SerializedName("discount")
    public String discount;
    @SerializedName("discountPercent")
    public String discountPercent;
}
