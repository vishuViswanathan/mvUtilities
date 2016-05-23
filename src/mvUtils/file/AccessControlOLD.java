package mvUtils.file;

import mvUtils.display.StatusWithMessage;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

/**
 * User: M Viswanathan
 * Date: 22-Apr-16
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class AccessControlOLD {
    String filePath;
    int maxFileLength = 100000;
    String regx = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])[^\\s]{" + 6 + ",}$";
    String passwordToolTip = "<html>Password must of min " + 6 + " characters length<p>with at least one each of <p>" +
            "a number <p>" +
            "one upper case Letter <p>one lower case letter <p>and one of @ # $ % & ";
    public AccessControlOLD() {

    }

    public AccessControlOLD(String filePath) throws Exception {
        this(filePath, false);
    }

    public AccessControlOLD(String filePath, boolean onlyIfExists) throws Exception {
        this(filePath, null, null, onlyIfExists);
    }

    public AccessControlOLD(String filePath, String passwordRegx, String passwordToolTip, boolean onlyIfExists) throws Exception {
        this.filePath = filePath;
        if (passwordRegx != null) {
            this.regx = passwordRegx;
            this.passwordToolTip = passwordToolTip;
        }
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                if (onlyIfExists)
                    throw new Exception("Unable to locate Access Control file");
                file.createNewFile();
            }
            else {
                if (!file.canWrite())
                    throw new Exception("Unable to access Access Control file ");
            }
        } catch (Exception e) {
            throw new Exception("Unable to access Access Control file ");
        }
    }

    public AccessControlOLD(String passwordRegx, String passwordToolTip) {
        if (passwordRegx != null) {
            this.regx = passwordRegx;
            this.passwordToolTip = passwordToolTip;
        }
    }

    public StatusWithMessage getAndSaveNewAccess(String accessString, String title) {
        StatusWithMessage retVal = new StatusWithMessage();
        PasswordDialog pDlg;
        pDlg = new PasswordDialog(title, true, regx, passwordToolTip);
        if (pDlg.allOK) {
            retVal = addNewAccess(accessString, pDlg.getName(), pDlg.getPassword());
        }
        else
            retVal.addErrorMessage("No password is set");
        return retVal;
    }

    public StatusWithMessage addNewAccess(String accessString, String name, String password) {
        StatusWithMessage retVal = new StatusWithMessage();
        String accessAndName = (accessString + name).toLowerCase();
        String nameHash =  getHash(accessAndName);
        String passWordHash = getHash(accessAndName + password);
        Vector<String[]> passList = readPasswordFile();
        if (nameHash != null && passWordHash != null) {
            if (locateNameHash(passList, nameHash) >= 0){
                retVal.addErrorMessage("Name/Access Exists");
            }
            else {
                String[] newLine = new String[2];
                newLine[0] = nameHash;
                newLine[1] = passWordHash;
                passList.add(newLine);
                if (!savePassList(passList))
                    retVal.addErrorMessage("Facing problem in Saving access File");
            }
        }
        return retVal;
    }

    public StatusWithMessage deleteAccess(String accessString, String name) {
        StatusWithMessage retVal = new StatusWithMessage();
        Vector<String[]> passList = readPasswordFile();
        String accessAndName = (accessString + name).toLowerCase();
        String nameHash =  getHash(accessAndName);
        int loc = locateNameHash(passList, nameHash);
        if (loc >= 0) {
            passList.remove(loc);
            savePassList(passList);
        }
        else
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
        return retVal;
    }


    public boolean checkPassword(String accessString, String name, String password) {
        boolean retVal = false;
        Vector<String[]> passList = readPasswordFile();
        String accessAndName = (accessString + name).toLowerCase();
        String nameHash =  getHash(accessAndName);
        String passWordHash = getHash(accessAndName + password);
        int loc = locateNameHash(passList, nameHash);
        if (loc >= 0)
            retVal = passList.get(loc)[1].equals(passWordHash);
        return retVal;
    }

    int locateNameHash(Vector<String[]> passList, String nameHash)  {
        int retVal = -1;
        int loc = 0;
        for (String[] oneLine:passList) {
            if (oneLine[0].equals(nameHash)) {
                retVal = loc;
                break;
            }
            loc++;
        }
        return retVal;
    }

    boolean savePassList(Vector<String[]> passList) {
        boolean retVal = false;
        block: {
            try {
                FileWriter fileWriter = new FileWriter(new File(filePath));
                BufferedWriter bW = new BufferedWriter(fileWriter);
                for (String[] oneLine:passList) {
                    bW.write(oneLine[0] + " " + oneLine[1]);
                    bW.newLine();
                }
                bW.close();
                fileWriter.close();
                retVal = true;
            } catch (IOException e) {
                break block;
            }
        }
        return retVal;
    }

    Vector<String[]> readPasswordFile() {
        Vector<String[]> passList = null;
        block: {
            try {
                FileReader fileReader = new FileReader(new File(filePath));
                BufferedReader br = new BufferedReader(fileReader);
                passList = new Vector<String[]>();

                String line = null;
                // if no more lines the readLine() returns null
                while ((line = br.readLine()) != null) {
                    passList.add(line.split(" "));
                }
                br.close();
                fileReader.close();
                return passList;
            } catch (IOException e) {
                break block;
            }
        }
        return passList;
    }

    private static String getHash(String stringToHash)
    {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(stringToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hash = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return hash;
    }

    public static void main(String[] args) {
        String path = "j:/mvUtilities/testing/passList.txt";
        try {
            AccessControlOLD access = new AccessControlOLD(path);
            StatusWithMessage st1 = access.addNewAccess("EXPERT1", "vishuExpert", "Expert0001$");
            System.out.println("" + st1.getDataStatus() + ":" + st1.getErrorMessage());
            boolean isOK = access.checkPassword("EXPERT1", "vishuExpert", "Vis0001$") ;
            System.out.println("EXPERT1" + ":vishuExpert" +  ":Vis0001$ " + st1.getDataStatus() + ":" + isOK);
            isOK = access.checkPassword("EXPERT1", "vishuExpert", "Expert0001$") ;
            System.out.println("EXPERT1" + ":vishuExpert" +  ":Expert0001$: " + st1.getDataStatus() + ":" + isOK);
            isOK = access.checkPassword("EXPERT", "vishuNew", "pass001") ;
            System.out.println("EXPERT" + ":vishuNew" +  ":pass001: " + st1.getDataStatus() + ":" + isOK);
            StatusWithMessage st4 = access.deleteAccess("RUTIME", "mv1");
            System.out.println((st4.getDataStatus() == StatusWithMessage.DataStat.OK) ? "mv1 Acess Deleted" : st4.getErrorMessage());
            StatusWithMessage st7 = access.deleteAccess("RUTIME", "RUNtimeVishu");
            System.out.println((st7.getDataStatus() == StatusWithMessage.DataStat.OK) ? "RUNtimeVishu Acess Deleted" : st7.getErrorMessage());
//            access.deleteAccess("RUNTIME", "RuntimeVishu");
            StatusWithMessage st3 = access.getAndSaveNewAccess("RUNTIME", "For Access as Runtime");
            System.out.println((st3.getDataStatus() == StatusWithMessage.DataStat.OK) ? "new password is ok" : st3.getErrorMessage());
            StatusWithMessage st5 = access.getAndCheckPassword("RUNTIME", "Access as RUNTIME");
            System.out.println((st5.getDataStatus() == StatusWithMessage.DataStat.OK) ? "password is ok" : st5.getErrorMessage());
            StatusWithMessage st6 = access.getAndCheckPassword("RUNTIME", "Access as RUNTIME");
            System.out.println((st6.getDataStatus() == StatusWithMessage.DataStat.OK) ? "password is ok" : st6.getErrorMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
