package edu.vassar.cmpu203.obre.view;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import edu.vassar.cmpu203.obre.databinding.ActivityMainBinding;

/**
 * Class to manage components shared among all screens and the fragments being displayed.
 */
public class MainUI {
    private final ActivityMainBinding mainBinding;
    private final FragmentManager fragmentManager;

    public MainUI(@NonNull FragmentActivity factivity) {
        this.mainBinding = ActivityMainBinding.inflate(factivity.getLayoutInflater());
        this.fragmentManager = factivity.getSupportFragmentManager();

        EdgeToEdge.enable(factivity);
        ViewCompat.setOnApplyWindowInsetsListener(this.mainBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    /**
     * Replaces the content of the screen container with the fragment passed in
     * as an argument.
     *
     * @param fragment The fragment to be displayed
     */

    public void displayFragment(@NonNull Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        fragmentTransaction.replace(this.mainBinding.fragmentContainer.getId(), fragment);
        fragmentTransaction.commit();
    }

    /**
     * Replaces the content of the screen container with the fragment passed in
     * as an argument. Pop back the stack if addToBackStack is true.
     *
     * @param fragment The fragment to be displayed
     * @param addToBackStack whether to add the fragment to the back stack
     */

    public void displayFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        fragmentTransaction.replace(this.mainBinding.fragmentContainerView.getId(), fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }


    /**
     * Returns the root view of the activity.
     *
     * @return the screen's root view
     */
    @NonNull
    public View getRootView() {
        return this.mainBinding.getRoot();
    }

    /**
     * Returns the fragment currently displayed on the screen.
     *
     * @return the fragment currently being displayed
     * @param <F> the fragment currently being displayed
     */
    public <F extends Fragment> F getFragment() {
        return this.mainBinding.fragmentContainer.getFragment();
    }
}
