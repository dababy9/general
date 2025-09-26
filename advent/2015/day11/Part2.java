import java.io.File;
import java.util.Scanner;
public class Part2 {

    public static boolean valid(char[] s){
        for(char c : s)
            if(c == 'i' || c == 'o' || c == 'l') return false;
        boolean flag = false;
        for(int i = 0; i < s.length-2; i++)
            if(s[i]+1 == s[i+1] && s[i]+2 == s[i+2]){ flag = true; break; }
        if(!flag) return false;
        int pairs = 0;
        for(int i = 0; i < s.length-1; i++)
            if(s[i] == s[i+1]){ pairs++; i++;}
        return pairs >= 2;
    }

    public static void increment(char[] s){
        do {
            for(int i = s.length-1; i >= 0; i--){
                if(s[i] == 'z') s[i] = 'a';
                else { s[i]++; break; }
            }
        } while(!valid(s));
    }

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            char[] s = scan.nextLine().toCharArray();
            increment(s);
            increment(s);
            System.out.println(new String(s));
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}