import java.io.File;
import java.util.Scanner;
public class Part2 {

    public static int p(String s, int i){
        return Integer.parseInt(s.split(",")[i]);
    }

    public static int[] getRanges(String[] line, int start, int stop){
        return new int[]{p(line[start], 0), p(line[stop], 0), p(line[start], 1), p(line[stop], 1)};
    }

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            char[][] map = new char[1000][1000];
            while(scan.hasNextLine()){
                String line[] = scan.nextLine().split(" ");
                char op = 'X';
                int[] ranges;
                if(line[0].equals("turn")){
                    ranges = getRanges(line, 2, 4);
                    op = line[1].equals("on") ? '1' : '0';
                } else ranges = getRanges(line, 1, 3);
                for(int i = ranges[0]; i <= ranges[1]; i++)
                    for(int j = ranges[2]; j <= ranges[3]; j++){
                        switch(op){
                            case '1': map[i][j]++; break;
                            case '0': map[i][j] -= (map[i][j] == 0 ? 0 : 1); break;
                            case 'X': map[i][j] += 2; break;
                        }
                    }
            }
            int sum = 0;
            for(int i = 0; i < 1000; i++)
                for(int j = 0; j < 1000; j++)
                    sum += map[i][j];
            System.out.println(sum);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
} 