package edu.vassar.cmpu203.OBRE.model;

import java.util.ArrayList;
import java.util.List;

public class DetectedObject {
    String name;
    List<Double> dim;

    public DetectedObject(String name, double x, double y, double width, double height) {
        this.name = name;
        dim = List.of(x, y, width, height);
    }

    public String getName() {
        return name;
    }

    public List<Double> getDim() {
        return dim;
    }
}
