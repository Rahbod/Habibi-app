package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionList {
    @SerializedName("list")
    public List<Transaction> list;
}
