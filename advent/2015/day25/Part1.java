import java.io.File;
import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.TreeMap;
public class Part1 {

    public void run(){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            String[] line = scan.nextLine().split(" ");
            int row = Integer.parseInt(line[16].substring(0, 4));
            int col = Integer.parseInt(line[18].substring(0, 4));
            int r2 = row*row, c2 = col*col;
            long prev = 20151125;
            for(int r = 2; r < r2; r++){
                for(int c = 1, workingRow = r; workingRow > 0; c++, workingRow--){
                    long newVal = (prev*252533) % 33554393;
                    if(workingRow == row && c == col){
                        System.out.println(newVal);
                        return;
                    }
                    prev = newVal;
                }
            }
        } catch(Exception e){
            System.out.println("File does not exist." + e);
        }
    }

    public static void main(String[] args){
        Part1 run = new Part1();
        run.run();
    }
} 