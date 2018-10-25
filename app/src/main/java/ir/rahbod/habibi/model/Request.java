package ir.rahbod.habibi.model;

public class Request {
    String name;
    String model;
    String condition;

    public Request() {
   }

    public Request(String name, String model, String condition) {
        this.name = name;
        this.model = model;
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
