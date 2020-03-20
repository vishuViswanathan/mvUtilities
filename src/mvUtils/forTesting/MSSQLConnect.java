package mvUtils.forTesting;

/**
 * Created by mviswanathan on 02-06-2018.
 */
public class MSSQLConnect {
    public static void main (String[] argv) {
        String url= "jdbc:jtds:sqlserver://localhost/TIPLIntranetUsers";
        String id= "enggDataUser";
        String pass = "eng0001";
        java.sql.Connection con;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            try {
                con = java.sql.DriverManager.getConnection(url, id, pass);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                con = null;
            }
        }catch(ClassNotFoundException cnfex){
            con = null;
        }
    }
}
