package view;

import controller.Controller;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * Command-line interface for the Object Recognition App.It handles
 * user input and displays output for both object and text recognition in the command line.
 * It implements the UI Interface
 */
public class CmdLineUI implements UI {

    PrintStream ps = System.out;
    final Scanner in = new Scanner(System.in);
    private Controller controller;

    /**
     * this function starts the main detection loop.
     * It prompts the user to input an image filename for text recognition.
     * It also validates the input and forwards requests to the controller.
     */
    public void startDetecting(){
        boolean running = true;
        while(running){

            if (controller == null) {
                ps.println("Error: Controller listener not set");
                return;
            }
            ps.println("Phone turned on, video stream started");
            ps.println("options: upload_image, keep_video_streaming, scan_for_text, exit");
            String option = in.nextLine().toLowerCase().trim();

            switch(option) {
                case "upload_image":
                    ps.print("Enter a filename (e.g., cat-dog.jpg): ");
                    String filename = in.nextLine().trim();
                        if (filename.isEmpty()) {
                            ps.println("Error: Filename cannot be empty. Returning to main menu.");
                            break;
                        }
                    while (!isValidImageFile(filename)) {
                        System.out.println("Please provide a .jpg or .png image.");
                        filename = in.nextLine();
                    }
                    controller.processImageFile(filename);

                    break;
                case "keep_video_streaming":
                    break;
                case "scan_for_text":
                    System.out.println("Enter image file to scan for text:");
                    String file = in.nextLine();
                    while (!isValidImageFile(file)) {
                        System.out.println("Please provide a .jpg or .png image.");
                        file = in.nextLine();
                    }
                    controller.processTextRecognition(file);
                    break;

                case "exit":
                    ps.println("Exiting the Prototype.");
                    running = false;
                    break;

                default:
                    ps.println("Invalid option. Please try again. \n");;
                    break;
            }
        }
    }

    @Override
    public void startVideoStreaming() {

    }
    /**
     * This function checks whether a filename ends with a supported image extension
     * (for right now the options are
     * .jpg or .png).
     *
     * @param filename The filename.
     * @return true if the file is a valid image, false otherwise.
     */
    private boolean isValidImageFile(String filename) {
        return filename.toLowerCase().trim().endsWith(".jpg")
                || filename.toLowerCase().trim().endsWith(".png")
                || filename.toLowerCase().trim().endsWith(".jpeg");
    }
    @Override
    public void setListener() {
    }

    @Override
    public void displayDetections(List<String> detections) {
        if (detections.isEmpty()) {
            ps.println("\nObject Recognition Complete: No objects detected.");
            return;
        }
        ps.println("\nObject Recognition Results (" + detections.size() + " Detections):");
        for (int i = 0; i < detections.size(); i++) {
            ps.printf("%d. %s%n", (i + 1), detections.get(i));
        }
    }

    /**
     * Sets the controller listener.
     * @paramcontroller
     */
    public void setListener(Controller ctrl) {
        this.controller = ctrl;

    }

    /**
     * Displays recognized text output in the console.
     * @param, The text to display.
     */
    @Override
    public void displayMessage(String message) {
        ps.println(message);
    }
}
