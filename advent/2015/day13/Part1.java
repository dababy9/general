import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
public class Part1 {

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            ArrayList<String[]> lines = new ArrayList<>();
            while(scan.hasNextLine())
                lines.add(scan.nextLine().split(" "));
            int n = (int) Math.sqrt(lines.size()) + 1;
            int[][] g = new int[n][n];
            int index = 0;
            for(int i = 0; i < n; i++)
                for(int j = 0; j < n; j++){
                    if(i == j) continue;
                    String[] line = lines.get(index++);
                    g[i][j] = g[j][i] += Integer.parseInt(line[3]) * (line[2].equals("gain") ? 1 : -1);
                }
            int[][] dp = new int[n][1<<n];
            for(int i = 0; i < n; i++) Arrays.fill(dp[i], Integer.MIN_VALUE);
            dp[0][1] = 0;
            for(int mask = 1; mask < (1<<n); mask++)
                for(int i = 0; i < n; i++){
                    if(((1<<i) & mask) == 0) continue;
                    for(int j = 0; j < n; j++){
                        if(j == i || ((1<<j) & mask) == 0) continue;
                        int prevMask = mask ^ (1<<i);
                        if(dp[j][prevMask] > Integer.MIN_VALUE)
                            dp[i][mask] = Math.max(dp[i][mask], dp[j][prevMask] + g[j][i]);
                    }
                }
            int max = Integer.MIN_VALUE;
            for(int i = 1; i < n; i++) max = Math.max(max, dp[i][(1<<n)-1] + g[0][i]);
            System.out.println(max);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}