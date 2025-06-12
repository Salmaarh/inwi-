package ma.emsi.testautomation.entity;


import java.util.List;

public class scenario {
    private String name;
    private List<step> steps;

    public scenario() {}

    public scenario(String name, List<step> steps) {
        this.name = name;
        this.steps = steps;
    }

    public String getName() {
        return "";
    }

    public step[] getsteps() {
        return new step[0];
    }

    // Getters & Setters
}
