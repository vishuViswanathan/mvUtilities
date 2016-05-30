package mvUtils.file;

import mvUtils.display.StatusWithMessage;
import mvUtils.jnlp.JNLPFileHandler;

import javax.jnlp.FileContents;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

/**
 * User: M Viswanathan
 * Date: 16-May-16
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccessControl {
    String accessFileCode = "accessData1234567890";
    protected boolean asJNLP = false;
    Vector<String[]> passList;
    int maxFileLength = 100000;
    String newLine = "\r\n";
    String separator = " ";
    String regx = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])[^\\s]{" + 6 + ",}$";
    String passwordToolTip = "<html>Password must of min " + 6 + " characters length<p>with at least one each of <p>" +
            "a number <p>" +
            "one upper case Letter <p>one lower case letter <p>and one of @ # $ % & ";
    protected String filePath;
    String suggestedExtension;
    String suggestedFileName;

    public AccessControl() {
        passList = new Vector<>();
    }

    public AccessControl(String passwordRegx, String passwordToolTip) {
        this();
        if (passwordRegx != null) {
            this.regx = passwordRegx;
            this.passwordToolTip = passwordToolTip;
        }
    }

    public void setAsJNLP()  {
        this.asJNLP = true;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setsuggestedExtension(String suggestedExtension) {
        this.suggestedExtension = suggestedExtension;
    }

    public StatusWithMessage getAndSaveNewAccess(String accessString, String title) {
        StatusWithMessage retVal = new StatusWithMessage();
        PasswordDialog pDlg;
        pDlg = new PasswordDialog(title, true, regx, passwordToolTip);
        if (pDlg.allOK) {
            retVal = addNewAccess(accessString, pDlg.getName(), pDlg.getPassword());
        } else
            retVal.addErrorMessage("No password is set");
        return retVal;
    }

    public StatusWithMessage addNewAccess(String accessString, String name, String password) {
        StatusWithMessage retVal = new StatusWithMessage();
        String accessAndName = (accessString + name).toLowerCase();
        String nameHash = getHash(accessAndName);
        String passWordHash = getHash(accessAndName + password);
        if (nameHash != null && passWordHash != null) {
            retVal = saveToPassList(nameHash, passWordHash);
        }
        return retVal;
    }

    public StatusWithMessage deleteAccess(String accessString, String name) {
        StatusWithMessage retVal = new StatusWithMessage();
        String accessAndName = (accessString + name).toLowerCase();
        String nameHash = getHash(accessAndName);
        int loc = locateNameHash(nameHash);
        if (loc >= 0) {
            passList.remove(loc);
        } else
            retVal.addErrorMessage("Name/Access is not in Record - no need to delete");
        return retVal;
    }

    public StatusWithMessage getAndCheckPassword(String accessString, String title) {
        StatusWithMessage retVal = new StatusWithMessage();
        PasswordDialog pDlg;
        pDlg = new PasswordDialog(title, false, regx, passwordToolTip);
        if (pDlg.allOK) {
            if (!checkPassword(accessString, pDlg.getName(), pDlg.getPassword()))
                retVal.setErrorMessage("Invalid Name/ Password for this access");
        }
        else
            retVal.addErrorMessage("Password is not entered");
        return retVal;
    }


    public boolean checkPassword(String accessString, String name, String password) {
        boolean retVal = false;
        String accessAndName = (accessString + name).toLowerCase();
        String nameHash = getHash(accessAndName);
        String passWordHash = getHash(accessAndName + password);
        int loc = locateNameHash(nameHash);
        if (loc >= 0)
            retVal = passList.get(loc)[1].equals(passWordHash);
        return retVal;
    }

    protected int locateNameHash(String nameHash) {
        int retVal = -1;
        int loc = 0;
        for (String[] oneLine : passList) {
            if (oneLine[0].equals(nameHash)) {
                retVal = loc;
                break;
            }
            loc++;
        }
        return retVal;
    }

    protected static String getHash(String stringToHash) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(stringToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    protected StatusWithMessage saveToPassList(String nameHash, String passwordHash) {
        StatusWithMessage retVal = new StatusWithMessage();
        if (locateNameHash(nameHash) >= 0) {
            retVal.addErrorMessage("Name/Access Exists");
        }
        else {
            String[] newLine = new String[]{nameHash, passwordHash};
            passList.add(newLine);
        }
        return retVal;
    }

    public StatusWithMessage readPasswordFile() {
        StatusWithMessage retVal = new StatusWithMessage();
        if (asJNLP) {
            try {
                FileContents fc = JNLPFileHandler.getReadFile(null, new String[]{suggestedExtension}, 0, 0);
                if (fc != null) {
                    long len = fc.getLength();
                    if (len > 20 && len < maxFileLength) {
                        String data = JNLPFileHandler.readFile(fc);
                        retVal = takeDataFromString(data);
                    }
                    else
                        retVal.addErrorMessage("Not an Access Data file :Too short/long a file for access Data");
                }
            } catch (IOException e) {
                retVal.addErrorMessage("Problem in reading accessFile:" + e.getMessage());
            }
        }
        else {
            block:
            {
                try {
                    File f = new File(filePath);
                    long len = f.length();
                    if ((len >= accessFileCode.length()) && (len < maxFileLength)) {
                        FileReader fileReader = new FileReader(f);
                        char[] buff = new char[(int)len];
                        fileReader.read(buff);
                        retVal = takeDataFromString(new String(buff));
                        fileReader.close();
                    }
                } catch (IOException e) {
                    retVal.addErrorMessage("IO error in reading access file");
                    break block;
                }
            }
        }
        return retVal;
    }

    public StatusWithMessage saveToPasswordFile() {
        StatusWithMessage retVal = new StatusWithMessage();
        if (asJNLP) {
            if (!JNLPFileHandler.saveToFile(dataAsString(), suggestedExtension, suggestedFileName))
                retVal.addErrorMessage("Some problem in saving to file");
        }
        else {
            try {
                FileWriter fileWriter = new FileWriter(new File(filePath));
                BufferedWriter bW = new BufferedWriter(fileWriter);
                bW.write(dataAsString());
                bW.close();
                fileWriter.close();
            } catch (IOException e) {
                retVal.addErrorMessage("IO problem in saving access");
            }
        }
        return retVal;
    }

    String oneDataLine(String[] oneSet) {
        return oneSet[0] + separator + oneSet[1];
    }

    String dataAsString() {
        StringBuilder retVal = new StringBuilder(accessFileCode + newLine);
        for (String[] oneSet: passList)
            retVal.append((oneDataLine(oneSet) + newLine ));
        return retVal.toString();
    }

    StatusWithMessage takeDataFromString(String data) {
        StatusWithMessage retVal = new StatusWithMessage();
        passList = new Vector<>();
        String[] sets = data.split(newLine);
        if ((sets.length > 0) && sets[0].equals(accessFileCode)) {
            int nData = 0;
            for (int i = 1; i < sets.length; i++) {
                String[] oneSet = sets[i].split(separator);
                if (oneSet.length == 2) {
                    passList.add(oneSet);
                    nData++;
                }
            }
            if (nData == 0)
                retVal.addInfoMessage("Empty access data List");
        }
        else
            retVal.addErrorMessage("Not an accessData File");
        return retVal;
    }

}
