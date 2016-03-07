package mvUtils.file;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 28-Dec-15
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileChooserWithOptions extends JFileChooser {
    boolean duplicateExists = false;
    private String extension;
    private String startWith = "";
    String duplicationMessage = "Do you want to overwrite the existing file?";
    public FileChooserWithOptions(String description, String extension) {
        super();
        this.extension = extension;
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                description, extension);
        setFileFilter(filter);
    }

    public FileChooserWithOptions(String title, String description, String extension) {
        this(description, extension);
        setTitle(title);
    }

    public FileChooserWithOptions(String title, String description, String extension, String duplicationMSg) {
        this(title, description, extension);
        setDuplicationMessage(duplicationMSg);
    }

    public void setDuplicationMessage(String msg) {
        this.duplicationMessage = msg;
    }

    public void setStartWithString(String startWithString) {
        startWith = startWithString;
    }

    public void setTitle(String title) {
        setDialogTitle(title);
    }

    public boolean isItDuplicate() {
        return duplicateExists;
    }

    @Override
    public File getSelectedFile() {
        File selectedFile = super.getSelectedFile();

        if (selectedFile != null) {
            String name = selectedFile.getName();
            boolean extensionOK = name.endsWith("." + extension);
            if (!extensionOK)
                selectedFile = new File(selectedFile.getParentFile(),
                        name + '.' + extension);
        }
        return selectedFile;
    }

    @Override
    public void approveSelection() {
        duplicateExists = false;
        if (getDialogType() == SAVE_DIALOG) {
            File selectedFile = getSelectedFile();
            if (selectedFile != null) {
                String onlyName = selectedFile.getName();
                if ((startWith.length() <= 0) || onlyName.startsWith(startWith)) {
                    if (selectedFile.exists()) {
                        duplicateExists = true;
                        int response = JOptionPane.showConfirmDialog(this,
                                "The file " + selectedFile.getName() +
                                        " already exists\n    " + duplicationMessage, "File Exists",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);
                        if (response != JOptionPane.YES_OPTION)
                            return;
                    }
                }
                else {
                    JOptionPane.showConfirmDialog(this,"The file name has to start with " + startWith,
                                    "Inproper File Name",
                            JOptionPane.DEFAULT_OPTION);
                    return;
                }
            }
        }
        super.approveSelection();
    }
}
