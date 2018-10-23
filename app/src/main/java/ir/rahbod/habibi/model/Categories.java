package ir.rahbod.habibi.model;

public class Categories {
    int id;
    String title;
    String uri;

    public Categories(int id, String title, String uri) {
        this.id = id;
        this.title = title;
        this.uri = uri;
    }
    public Categories() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
