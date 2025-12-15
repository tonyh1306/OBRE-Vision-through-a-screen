package edu.vassar.cmpu203.obre.view;

import androidx.annotation.NonNull;

public interface UI {
    interface Listener {
    }

    <L extends Listener> void setListener(@NonNull final L listener);
}
