import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
public class Part2 {
    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            ArrayList<Integer> list = new ArrayList<>();
            while(scan.hasNextLine())
                list.add(Integer.parseInt(scan.nextLine()));
            int index = 0;
            int[] containers = new int[list.size()];
            for(int i : list)
                containers[index++] = i;
            int maxCombo = (1 << containers.length) - 1;
            int total = 0, best = Integer.MAX_VALUE;
            for(int i = 1; i < maxCombo; i++){
                int contained = 0, used = Integer.bitCount(i);
                for(int j = 0; j < containers.length; j++)
                    if(((1 << j) & i) != 0) contained += containers[j];
                if(contained == 150)
                    if(used < best){
                        best = used;
                        total = 1;
                    } else if(used == best) total++;
            }
            System.out.println(total);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}