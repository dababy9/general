import java.io.File;
import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;
public class Part1 {

    public static class Pair {
        public String from, to;
        public int length;

        public Pair(String f, String t){
            from = f;
            to = t;
            length = f.length();
        }
    }
    
    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            ArrayList<Pair> rules = new ArrayList<>();
            for(String[] lines = scan.nextLine().split(" "); lines.length > 1; lines = scan.nextLine().split(" "))
                rules.add(new Pair(lines[0], lines[2]));
            String input = scan.nextLine();
            HashSet<String> set = new HashSet<>();
            for(Pair rule : rules){
                for(int index = input.indexOf(rule.from); index > 0; index = input.indexOf(rule.from, index + 1))
                    set.add(input.substring(0, index) + rule.to + input.substring(index + rule.length));
            }
            System.out.println(set.size());
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
} 