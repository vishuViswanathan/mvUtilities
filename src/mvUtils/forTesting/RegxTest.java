package mvUtils.forTesting;

/**
 * User: M Viswanathan
 * Date: 22-Aug-16
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.Console;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegxTest{
    public static void main(String[] args){
        Console console = System.console();
        if (console == null) {
            System.err.println("No console.");
            System.exit(1);
        }
        Pattern pattern = Pattern.compile(console.readLine("Enter your regex: "));
        Matcher matcher = pattern.matcher(console.readLine("Enter input string to search: "));
        boolean found = false;
        while (matcher.find()) {
            System.out.println("I found the text "+matcher.group()+" starting at index "+
                    matcher.start()+" and ending at index "+matcher.end());
            found = true;
        }
        if(!found){
            System.out.println("No match found.");
        }
    }
}