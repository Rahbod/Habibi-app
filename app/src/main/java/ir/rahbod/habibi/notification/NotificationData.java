package ir.rahbod.habibi.notification;

/**
 * Created by ebtekar on 11/5/2017.
 */

public class NotificationData {

    private String title = null;
    private String description = null;
    private String icon = null;
    private String image = null;
    private int isOngoing = -1;
    private String style = null;
    private int id = -1;
    private String link = null;
    private String type = null;
    private String timestamp = null;

    public NotificationData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIsOngoing() {
        return isOngoing;
    }

    public void setIsOngoing(int isOngoing) {
        this.isOngoing = isOngoing;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
