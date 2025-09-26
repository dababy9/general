import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
public class Part2 {

    public static String lookAndSay(String s){
        StringBuilder r = new StringBuilder();
        char[] c = s.toCharArray();
        int run = 0;
        for(int i = 0; i < c.length; i++, run++){
            if(i != 0 && c[i] != c[i-1]){
                r.append(run).append(c[i-1]);
                run = 0;
            }
        }
        return r.append(run).append(c[c.length-1]).toString();
    }

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            String s = scan.nextLine();
            for(int i = 0; i < 50; i++)
                s = lookAndSay(s);
            System.out.println(s.length());
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}