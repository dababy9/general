import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Part1 {

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            String line = scan.nextLine();
            Pattern p = Pattern.compile("-?\\d+");
            Matcher m = p.matcher(line);
            int sum = 0;
            while(m.find()) sum += Integer.parseInt(m.group());
            System.out.println(sum);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}