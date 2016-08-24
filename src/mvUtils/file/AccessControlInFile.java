package mvUtils.file;

import mvUtils.display.StatusWithMessage;

import java.io.*;
import java.util.Vector;

/**
 * User: M Viswanathan
 * Date: 16-May-16
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccessControlInFile extends AccessControl {
    public AccessControlInFile(PasswordIntensity intensity, String filePath) throws Exception {
        this(intensity, filePath, false);
    }

    public AccessControlInFile(String filePath) throws Exception {
        this(PasswordIntensity.HIGH, filePath);
    }

    public AccessControlInFile(PasswordIntensity intensity, String filePath, boolean onlyIfExists) throws Exception {
        this(intensity, filePath, null, null, onlyIfExists);
    }

    public AccessControlInFile(String filePath, boolean onlyIfExists) throws Exception {
        this(PasswordIntensity.HIGH, filePath, onlyIfExists);
    }

    public AccessControlInFile(PasswordIntensity intensity, String filePath, String passwordRegx, String passwordToolTip, boolean onlyIfExists) throws Exception {
        super(intensity);
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
            } else {
                if (!file.canWrite())
                    throw new Exception("Unable to access Access Control file ");
            }
        } catch (Exception e) {
            throw new Exception("Unable to access Access Control file ");
        }
    }

    public AccessControlInFile(String filePath, String passwordRegx, String passwordToolTip, boolean onlyIfExists) throws Exception {
        this(PasswordIntensity.HIGH, filePath, passwordRegx, passwordToolTip, onlyIfExists);
    }

    public StatusWithMessage deleteAccess(String accessString, String name) {
        StatusWithMessage retVal = readPasswordFile();
        if (retVal.getDataStatus() == StatusWithMessage.DataStat.OK) {
            retVal = super.deleteAccess(accessString, name);
            if (retVal.getDataStatus() == StatusWithMessage.DataStat.OK)
                retVal = saveToPasswordFile();
        }
        return retVal;
    }

    public StatusWithMessage addNewAccess(String accessString, String name, String password) {
        StatusWithMessage retVal = readPasswordFile();
        if (retVal.getDataStatus() != StatusWithMessage.DataStat.WithErrorMsg) {
            StatusWithMessage addNewResponse = super.addNewAccess(accessString, name, password);
            if (addNewResponse.getDataStatus() == StatusWithMessage.DataStat.WithErrorMsg )
                retVal.addErrorMessage(addNewResponse.getErrorMessage());
            else {
                StatusWithMessage saveResponse = saveToPasswordFile();
                if (saveResponse.getDataStatus() == StatusWithMessage.DataStat.WithErrorMsg)
                    retVal.addErrorMessage(saveResponse.getErrorMessage());
            }
        }
        return retVal;
    }

    public boolean checkPassword(String accessString, String name, String password) {
        boolean retVal = false;
        if (readPasswordFile().getDataStatus() == StatusWithMessage.DataStat.OK);
            retVal = super.checkPassword(accessString, name, password);
        return retVal;
    }

    protected StatusWithMessage saveToPassList(String nameHash, String passwordHash) {
        StatusWithMessage retVal = super.saveToPassList(nameHash, passwordHash);
        if (retVal.getDataStatus() == StatusWithMessage.DataStat.OK)
            retVal = saveToPasswordFile();
        return retVal;
    }

//    public static void main(String[] args) {
//        String path = "j:/mvUtilities/testing/passList.txt";
//        try {
//            AccessControlInFile access = new AccessControlInFile(path);
//            StatusWithMessage st1 = access.addNewAccess("EXPERT1", "vishuExpert", "Expert0001$");
//            System.out.println("" + st1.getDataStatus() + ":" + st1.getErrorMessage());
//            boolean isOK = access.checkPassword("EXPERT1", "vishuExpert", "Vis0001$") ;
//            System.out.println("EXPERT1" + ":vishuExpert" +  ":Vis0001$ " + st1.getDataStatus() + ":" + isOK);
//            isOK = access.checkPassword("EXPERT1", "vishuExpert", "Expert0001$") ;
//            System.out.println("EXPERT1" + ":vishuExpert" +  ":Expert0001$: " + st1.getDataStatus() + ":" + isOK);
//            isOK = access.checkPassword("EXPERT", "vishuNew", "pass001") ;
//            System.out.println("EXPERT" + ":vishuNew" +  ":pass001: " + st1.getDataStatus() + ":" + isOK);
//            StatusWithMessage st4 = access.deleteAccess("RUNTIME", "RuntimeVishu");
//            System.out.println((st4.getDataStatus() == StatusWithMessage.DataStat.OK) ? "mv1 Acess Deleted" : st4.getErrorMessage());
//            StatusWithMessage st7 = access.deleteAccess("RUTIME", "RUNtimeVishu");
//            System.out.println((st7.getDataStatus() == StatusWithMessage.DataStat.OK) ? "RUNtimeVishu Acess Deleted" : st7.getErrorMessage());
////            access.deleteAccess("RUNTIME", "RuntimeVishu");
//            StatusWithMessage st3 = access.getAndSaveNewAccess("RUNTIME", "For Access as Runtime");
//            System.out.println((st3.getDataStatus() == StatusWithMessage.DataStat.OK) ? "new password is ok" : st3.getErrorMessage());
//            StatusWithMessage st5 = access.getAndCheckPassword("RUNTIME", "Access as RUNTIME");
//            System.out.println((st5.getDataStatus() == StatusWithMessage.DataStat.OK) ? "password is ok" : st5.getErrorMessage());
//            StatusWithMessage st6 = access.getAndCheckPassword("RUNTIME", "Access as RUNTIME");
//            System.out.println((st6.getDataStatus() == StatusWithMessage.DataStat.OK) ? "password is ok" : st6.getErrorMessage());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

}
