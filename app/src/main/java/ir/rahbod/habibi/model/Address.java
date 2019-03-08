package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class Address {
    private boolean checked = false;

    @SerializedName("id")
    private int id;
    @SerializedName("telephone")
    private String telephone = "";
    @SerializedName("address")
    private String address;
    @SerializedName("map_lat")
    private String lat;
    @SerializedName("map_lng")
    private String lng;
    @SerializedName("map_zoom")
    private String zoom;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private boolean status;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
