package src.controller;
import src.view.CmdLineUI;
import src.view.UI;

public class Controller implements UI.Listener{
    private final UI ui;

    @Override
    public void analyze_video() {

    }

    public void onProcessImage(){

    }

    public Controller(UI ui){
        this.ui = ui;
    }

    public static void main(String[] args){
        final UI ui = new CmdLineUI();
        final Controller ctrl = new Controller(ui);
        ctrl.ui.setListener(ctrl);
        ctrl.ui.startVideoStreaming();

    }
}
