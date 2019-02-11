package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Invoice {
    @SerializedName("cost")
    public String cost;
    @SerializedName("additionalCost")
    public String additionalCost;
    @SerializedName("description")
    public String description;
    @SerializedName("paymentMethod")
    public String paymentMethod;
    @SerializedName("status")
    public String status;
    @SerializedName("totalDiscount")
    public String totalDiscont;
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
