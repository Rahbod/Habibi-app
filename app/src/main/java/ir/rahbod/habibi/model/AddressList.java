package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressList {
    @SerializedName("list")
    public List<Address> list;
    @SerializedName("address")
    public Address address;
}
