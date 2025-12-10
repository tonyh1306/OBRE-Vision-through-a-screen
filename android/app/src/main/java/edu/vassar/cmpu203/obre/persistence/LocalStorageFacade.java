package edu.vassar.cmpu203.obre.persistence;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import edu.vassar.cmpu203.obre.model.Ledger;

public class LocalStorageFacade implements PersistenceFacade {
    private static final String LEDGER_NAME = "ledger.ngp";
    private final File file;

    /**
     * Constructor method.
     *
     * @param context the context in which the app is running.
     */
    public LocalStorageFacade(final Context context) {
        this.file = new File(context.getFilesDir(), LEDGER_NAME);
    }

    @Override
    public void saveDescription(@NonNull String description) {
        boolean append = this.file.isFile();
        try (
                FileOutputStream fos = new FileOutputStream(this.file, true);
                ObjectOutputStream oos = append ?
                        new AppendableObjectOutputStream(fos) :
                        new ObjectOutputStream(fos)) {
            oos.writeObject(description);

        } catch (IOException e) {
            final String emsg = String.format("I/O exception while writing ledger: %s", e.getMessage());
            Log.e("LocalStorageFacade", emsg, e);

        }

    }

    /**
     * An ObjectOutputStream variation that does away with headers so we can write append to an
     * existing file.
     */
    private static class AppendableObjectOutputStream extends ObjectOutputStream {
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        /**
         * Called to write a header, but writes nothing at all.
         *
         * @throws IOException if invoked while serializing an object.
         */
        @Override
        protected void writeStreamHeader() throws IOException {
            reset(); // do not write a header when appending
        }
    }


    @Override
    public void loadLedger(@NonNull Listener listener) {
        final Ledger ledger = new Ledger();

        if (this.file.isFile()) {
            try (ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(this.file))) {

                while (true) { // keep reading sales until end of file is reached
                    try {
                        final String des = (String) oiStream.readObject();
                        ledger.addDescription(des);
                    } catch (EOFException eof) { // end of file reached
                        break;
                    } catch (ClassNotFoundException e) { // file
                        final String emsg = String.format("Exception while reading sale: %s", e.getMessage());
                        Log.e("NextGenPOS", emsg, e);
                    }
                }
            } catch (IOException e) { // general IO exception
                final String emsg = String.format("Exception while reading ledger: %s", e.getMessage());
                Log.e("NextGenPOS", emsg, e);
            }
        }
        listener.onLedgerReceived(ledger); // notify listener

    }
}

