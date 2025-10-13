package src.view;

import java.io.PrintStream;
import java.util.Scanner;

public class CmdLineUI implements UI {


    PrintStream ps = System.out;
    final Scanner in = new Scanner(System.in);


    public void startVideoStreaming(){
        ps.println("Phone turned on, video stream started");
    }




}
