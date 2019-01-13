package ir.rahbod.habibi.model;

public class GetTime {
    public int startTime;
    public int endTime;
    private boolean checkTime;

    public boolean isCheckTime() {
        return checkTime;
    }

    public void setCheckTime(boolean checkTime) {
        this.checkTime = checkTime;
    }
}
