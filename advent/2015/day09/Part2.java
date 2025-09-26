import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
public class Part2 {

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            HashMap<String, Integer> cities = new HashMap<>();
            ArrayList<String[]> lines = new ArrayList<>();
            int n = 0;
            while(scan.hasNextLine()){
                String[] line = scan.nextLine().split(" ");
                if(!cities.containsKey(line[0])) cities.put(line[0], n++);
                if(!cities.containsKey(line[2])) cities.put(line[2], n++);
                lines.add(line);
            }
            int[][] g = new int[n][n];
            for(String[] line : lines){
                int p = cities.get(line[0]);
                int q = cities.get(line[2]);
                g[p][q] = g[q][p] = Integer.parseInt(line[4]);
            }
            int[][] dp = new int[n][1<<n];
            for(int i = 0; i < n; i++) dp[i][1<<i] = 0;
            for(int mask = 1; mask < (1<<n); mask++)
                for(int i = 0; i < n; i++){
                    if(((1<<i) & mask) == 0) continue;
                    for(int j = 0; j < n; j++){
                        if(j == i || ((1<<j) & mask) == 0) continue;
                        dp[i][mask] = Math.max(dp[i][mask], dp[j][mask ^ (1<<i)] + g[j][i]);
                    }
                }
            int max = 0;
            for(int i = 0; i < n; i++) max = Math.max(max, dp[i][(1<<n)-1]);
            System.out.println(max);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}