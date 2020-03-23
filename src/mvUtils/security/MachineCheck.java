package mvUtils.security;

import mvUtils.display.StatusWithMessage;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * User: M Viswanathan
 * Date: 03-May-17
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class MachineCheck {
    public enum MachineStat {
        CANRUN("Yes"),
        CANNOTRUN("No"),
        NOMACHINERECORD("NM"),
        DIFFERENTMACHINE("DM");
        private final String stat;

        MachineStat(String machineStat) {
            this.stat = machineStat;
        }

        public String getValue() {
            return name();
        }

        @Override
        public String toString() {
            return stat;
        }

        public static MachineStat getEnum(String text) {
            MachineStat retVal = null;
            if (text != null) {
                for (MachineStat b : MachineStat.values()) {
                    if (text.equalsIgnoreCase(b.stat)) {
                        retVal = b;
                        break;
                    }
                }
            }
            return retVal;
        }
    }

    public String getMachineID() {
        return getMachineID(false);
    }

    public String getMachineID(boolean withUserName) {
        String userName = "";
        if (withUserName) {
            userName = MiscUtil.getUser();
//            debug("getMachineID.46: userName = " + userName);
        }
        return produceIDString((MiscUtil.getMotherboardSN() + userName + MiscUtil.getSerialNumber("C")).getBytes());
    }

    String produceIDString(byte[] mac) {
        byte[] usedBytes;
        int len = mac.length;
//        debug("produceIDString.53: len = " + len);
        if (len > 12)  {
            int skip = len / 8;
            len /= skip;
            usedBytes = new byte[len];
            for (int i = 0; i < len; i++)
                usedBytes[i] = mac[i * skip];
        }
        else
            usedBytes = mac;
        StringBuilder idStr = new StringBuilder();
        for (int i = 0; i < usedBytes.length; i++) {
            idStr.append(String.format("%02X", (255-usedBytes[i])));
        }
        return idStr.toString();
    }

    byte[] getMacFromMachineIdDStr(String idStr) {
        int len = idStr.length();
        byte[] mac = new byte[len / 2];
        for (int p = 0; p < len; p += 2) {
            mac[p / 2] = (byte)(255 - Byte.valueOf(idStr.substring(p, p + 2)));
        }
        return mac;
    }

    public String getKey(String idStr) {
        return getKey(idStr, 0);
    }

    public String getKey(String idStr, int modifier) {
//        if (modifier > 0)
//            idStr += ("" + modifier).trim();
        int len = idStr.length();
        if (len % 2 != 0) {
            idStr += "9";
            len = idStr.length();
        }
        StringBuilder keyBase = new StringBuilder();
        StringBuilder key = new StringBuilder();
        try {
            for (int p = 0; p < (len - 1); p+= 2) {
                keyBase.append(String.format("%04d", (modifier + 1000 - Integer.parseInt(idStr.substring(p, p + 2), 16))));
            }
            // reverse it
            for (int p = keyBase.length() - 1; p >= 0; p -= 2)
                key.append(keyBase.substring(p, p + 1));
        } catch (NumberFormatException e) {
            key.append("ERROR in processing input code");
        }
        return key.toString();
    }

    public StatusWithMessage checkKey(String key) {
        return checkKey(key, false);
    }

    public StatusWithMessage checkKey(String key, boolean withUsername, int modifier) {
        StatusWithMessage retVal = new StatusWithMessage();
        String machineID = getMachineID(withUsername);
        if (machineID.length() > 5) {
//            if (modifier > 0)
//                machineID += ("" + modifier).trim();
            if (!key.equals(getKey(machineID, modifier)))
                retVal.setInfoMessage("Key Mismatch");
        }
        else
            retVal.setErrorMessage("It appears that the Network is not connected");
        return retVal;
    }

    public StatusWithMessage checkKey(String key, boolean withUsername) {
        return checkKey(key, withUsername, 0);
    }

    public boolean checkKey(String machineID, String key, int modifier) {
        return getKey(machineID, modifier).equals(key);
    }

    public boolean checkKey(String machineID, String key) {
        return checkKey(machineID, key, 0);
    }

    void debug(String msg)  {
        System.out.println("MachineCheck: " + msg);
    }
}
