package edu.vassar.cmpu203.obre.persistence;

import androidx.annotation.NonNull;

import edu.vassar.cmpu203.obre.model.Ledger;

/**
 * Interface that specifies a contract that all persistence solutions must fulfill.
 */
public interface PersistenceFacade {
    /**
     * Interface that classes interested in being notified of data-generating events
     * from the persistence layer should implement.
     */
    interface Listener {

        /**
         * Called when the receiver has been fully loaded/received.
         *
         * @param ledger the ledger that was just received
         */
        void onLedgerReceived(@NonNull Ledger ledger);
    }

    /**
     * Saves new description to underlying persistence subsystem.
     *
     * @param description the description string to be saved.
     */
    void saveDescription(@NonNull final String description);

    /**
     * Issues a ledger retrieval operation.
     *
     * @param listener the observer to be notified of query result.
     */
    void loadLedger(@NonNull final Listener listener);

}
