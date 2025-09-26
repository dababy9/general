import java.io.File;
import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Collections;
public class Part2 {

    ArrayList<Integer> list = new ArrayList<>();
    ArrayList<Long> bestResults = new ArrayList<>();
    int best = Integer.MAX_VALUE;
    int l = 0;
    int gSize = 0;

    public void dfs(int index, int total, int size, long quant){
        if(total > gSize || size > best) return;
        if(total == gSize){
            if(size < best){
                best = size;
                bestResults = new ArrayList<>();
            }
            bestResults.add(quant);
            return;
        }
        for(int i = index; i < l; i++)
            dfs(i+1, total+list.get(i), size+1, quant*list.get(i));
    }

    public void run(){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            while(scan.hasNextLine())
                list.add(Integer.parseInt(scan.nextLine()));
            Collections.reverse(list);
            int sum = 0;
            for(int x : list) sum += x;
            gSize = sum/4;
            l = list.size();
            dfs(0, 0, 0, 1);
            System.out.println(Collections.min(bestResults));
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }

    public static void main(String[] args){
        Part2 run = new Part2();
        run.run();
    }
} 