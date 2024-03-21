import java.io.File;
import java.util.Scanner;
public class Part1 {

    public static int count(String s){
        char[] c = s.substring(1, s.length()-1).toCharArray();
        int r = 0;
        for(int i = 0; i < c.length; i++, r++){
            if(c[i] == '\\'){
                if(c[i+1] == 'x') i += 3;
                else i++;
            }
        }
        return r;
    }

    public void run(){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            int sum = 0;
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                sum += line.length();
                sum -= count(line);
            }
            System.out.println(sum);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }

    public static void main(String[] args){
        Part1 run = new Part1();
        run.run();
    }
} 