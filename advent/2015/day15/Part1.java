import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
public class Part1 {

    public static int getNum(String line){
        return Integer.parseInt(line.replaceAll(",", ""));
    }

    public static void add(int[] base, int[] additive, int amount){
        for(int i = 0; i < base.length; i++)
            base[i] += additive[i] * amount;
    }

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            ArrayList<String[]> lines = new ArrayList<>();
            while(scan.hasNextLine())
                lines.add(scan.nextLine().split(" "));
            int[][] p = new int[lines.size()][4];
            int index = 0;
            for(String[] line : lines)
                p[index++] = new int[]{getNum(line[2]), getNum(line[4]), getNum(line[6]), getNum(line[8])};
            int best = Integer.MIN_VALUE;
            for(int a = 0; a <= 100; a++)
                for(int b = 0; b <= 100-a; b++)
                    for(int c = 0; c <= 100-a-b; c++){
                        int[] t = new int[4], amounts = new int[]{a, b, c, 100-a-b-c};
                        for(int i = 0; i < amounts.length; i++)
                            add(t, p[i], amounts[i]);
                        if(t[0] < 0 || t[1] < 0 || t[2] < 0 || t[3] < 0) continue;
                        best = Math.max(t[0]*t[1]*t[2]*t[3], best);
                    }
            System.out.println(best);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}