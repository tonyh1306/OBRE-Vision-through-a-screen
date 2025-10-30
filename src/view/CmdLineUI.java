package view;

import controller.Controller;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class CmdLineUI implements UI {

    PrintStream ps = System.out;
    final Scanner in = new Scanner(System.in);
    private Controller controller;


    public void startDetecting(){
        boolean running = true;
        while(running){

            if (controller == null) {
                ps.println("Error: Controller listener not set");
                return;
            }
            ps.println("Phone turned on, video stream started");
            ps.println("options: upload_image, keep_video_streaming, scan_for_text, exit");
            String option = in.nextLine().toLowerCase();

            switch(option) {
                case "upload_image":
                    ps.print("Enter a filename (e.g., resources/cat-dog.jpg): ");
                    String filename = in.nextLine().trim();
                        if (filename.isEmpty()) {
                            ps.println("Error: Filename cannot be empty. Returning to main menu.");
                            break;}
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
                    ps.println("Exiting the Prototype");
                    running = false;
                    break;
            }
        }
    }

    @Override
    public void startVideoStreaming() {

    }

    private boolean isValidImageFile(String filename) {
        return filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".png");
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

    public void setListener(Controller ctrl) {
        this.controller = ctrl;

    }
    @Override
    public void displayMessage(String message) {
        ps.println(message);
    }
}
