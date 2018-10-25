package ir.rahbod.habibi.model;

public class Address {
    String address;
    boolean checked = false;

    public Address(String address) {
        this.address = address;
    }

    public Address() {}

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
