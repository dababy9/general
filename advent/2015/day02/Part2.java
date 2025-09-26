import java.io.File;
import java.util.Scanner;
import java.util.Arrays;
public class Part2 {

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            int sum = 0;
            while(scan.hasNextLine()){
                String[] line = scan.nextLine().split("x");
                int[] d = new int[line.length];
                for(int i = 0; i < 3; i++) d[i] = Integer.parseInt(line[i]);
                Arrays.sort(d);
                sum += 2 * (d[0] + d[1]);
                sum += d[0] * d[1] * d[2];
            }
            System.out.println(sum);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}