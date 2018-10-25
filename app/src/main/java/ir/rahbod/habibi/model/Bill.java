package ir.rahbod.habibi.model;

public class Bill {
    String title;
    String cost;

    public Bill(String title, String cost) {
        this.title = title;
        this.cost = cost;
    }

    public Bill() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
