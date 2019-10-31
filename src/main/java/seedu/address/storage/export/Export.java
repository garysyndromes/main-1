package seedu.address.storage.export;

import java.io.IOException;

/**
 * Interface for exporting files generated from nJoyAssistant into documents.
 */
public interface Export {
    String EXPORT_DIRECTORY_PATH = "../export/";

    /**
     * Saves the files generated by nJoyAssistant into a Microsoft Office document.
     *
     * @throws IOException If there is an error writing into the document.
     */
    void saveExport() throws IOException;
}
