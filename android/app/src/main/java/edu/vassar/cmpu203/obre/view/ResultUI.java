package edu.vassar.cmpu203.obre.view;

import android.view.View;

public interface ResultUI {
    public interface Listener {
        void onSwitchToUpload(String detection);
    }

    void setListener(Listener listener);


}
