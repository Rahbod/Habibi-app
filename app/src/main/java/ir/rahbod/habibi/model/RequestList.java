package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestList {
    @SerializedName("list")
    public List<ItemRequest> list;
}
