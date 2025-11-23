package edu.vassar.cmpu203.obre.model;

import java.util.List;

/**
 * Data model representing a single object detected by the computer vision algorithm.
 */
public class DetectedObject {
    String name;
    List<Double> dim;

    /**
     * Creates a new DetectedObject.
     *
     * @param name The class name (label) of the object (e.g., "person", "car").
     * @param x The X coordinate of the bounding box.
     * @param y The Y coordinate of the bounding box.
     * @param width The width of the bounding box.
     * @param height The height of the bounding box.
     */
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
