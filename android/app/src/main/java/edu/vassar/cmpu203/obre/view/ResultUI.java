package edu.vassar.cmpu203.obre.view;

public interface ResultUI {
    public interface Listener {
        void onSwitchToUpload(String detection);
    }

    void setListener(Listener listener);


}
