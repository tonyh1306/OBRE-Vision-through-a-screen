package view;

import controller.Controller;

import java.io.PrintStream;
import java.util.Scanner;

public class CmdLineUI implements UI {


    PrintStream ps = System.out;
    final Scanner in = new Scanner(System.in);


    public void startVideoStreaming(){
        boolean running = true;
        while(running){

            ps.println("Phone turned on, video stream started");
            ps.println("options: upload_image, keep_video_streaming, exit");
            String option = in.nextLine().toLowerCase();

            switch(option) {
                case "upload_image":
                    ps.print("Enter filename (e.g., resources/cat-dog.jpg): ");
                    String filename = in.nextLine().trim();
                        if (filename.isEmpty()) {
                            System.out.println("Error: Filename cannot be empty. Returning to main menu.");
                            break;}
                    break;
                case "keep_video_streaming":
                    break;
                case "exit":
                    running = false;
                    break;
            }


        }

    }


    public void setListener(Controller ctrl) {

    }
}
