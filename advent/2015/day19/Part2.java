import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
public class Part2 {

    public static HashMap<String, String> reductions = new HashMap<>();

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            for(String[] line = scan.nextLine().split(" "); line.length != 1; line = scan.nextLine().split(" "))
                reductions.put(line[2], line[0]);
            StringBuilder input = new StringBuilder(scan.nextLine());
            int total = 0;
            while(input.length() > 1){
                for(String key : reductions.keySet()){
                    int i = input.indexOf(key);
                    if(i >= 0){
                        input.replace(i, i + key.length(), reductions.get(key));
                        total++;
                    }
                }
            }
            System.out.println(total);
        } catch(Exception e){
            System.out.println("File does not exist."+e);
        }
    }
} 