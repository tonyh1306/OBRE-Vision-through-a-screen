package edu.vassar.cmpu203.obre.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Represents a collection of completed sales.
 *
 */
public class Ledger implements Serializable {

    private final Collection<String> detections; // the sales recorded on the ledger

    /**
     * Constructs an empty ledger.
     */
    public Ledger(){
        this.detections = new LinkedList<>();  }

    /**
     * Adds a new sale to the ledger.
     * @param detection the sale to be added to the ledger.
     */
    public void addSale(String detection){
        this.detections.add(detection);
    }

    /**
     * Creates and returns a textual representation of the ledger.
     *
     * @return a textual representation of the ledger
     */
    @NonNull
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (String detection : this.detections) {
            sb.append(detection);
            sb.append("-----------\n");
        }
        return sb.toString();
    }
}
