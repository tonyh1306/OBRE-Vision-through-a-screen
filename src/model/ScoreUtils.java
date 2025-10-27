package model;

import java.util.List;

record MaxScore (double maxValue, int indexOfMax) {}

public class ScoreUtils {

    public static MaxScore findMaxScore(List<Double> array) {
        double max = array.get(0);
        int indexOfMax = 0;
        for (int i = 1; i < array.size(); i++) {
            if (array.get(i) > max) {
                max = array.get(i);
                indexOfMax = i;
            }
        }
        return new MaxScore( max, indexOfMax );
    }
}