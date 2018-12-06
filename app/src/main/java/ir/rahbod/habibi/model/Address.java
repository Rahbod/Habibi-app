package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class Address {
    private boolean checked = false;

    @SerializedName("id")
    public int id;
    @SerializedName("telephone")
    public String telephone;
    @SerializedName("address")
    public String address;


    public Address(String address) {
        this.address = address;
    }

    public Address() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
