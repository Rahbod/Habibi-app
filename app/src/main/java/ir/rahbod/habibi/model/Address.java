package ir.rahbod.habibi.model;

import com.google.gson.annotations.SerializedName;

public class Address {
    private boolean checked = false;

    @SerializedName("id")
    private int id;
    @SerializedName("telephone")
    private String telephone;
    @SerializedName("address")
    private String address;
    @SerializedName("map_lat")
    private double lat;
    @SerializedName("map_lng")
    private double lng;
    @SerializedName("map_zoom")
    private double zoom;

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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double map_zoom) {
        this.zoom = map_zoom;
    }
}
