package edu.vassar.cmpu203.obre.model;

public class TextRecognizer {

    /**
     * This function takes a filename and outputs a text from the OCR reading of that file.
     *
     * @param filename a string to the file
     */
    public String recognizeText(String filename) {
        //will later implement Gemini API and OCR logic
        System.out.println("recognizing text from: " + filename);

        // change this to include real logic in next iteration
        return "Text output is: '" + filename + "' is a tree (example)";
    }
}
