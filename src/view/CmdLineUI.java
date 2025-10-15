package src.view;

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
        String option = in.nextLine();

        switch(option) {
            case "upload_image":
                break;
            case "keep_video_streaming":
                break;
            case "exit":
                running = false;
                break;
        }



        }

    }






}
