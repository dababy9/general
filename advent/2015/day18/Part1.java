import java.io.File;
import java.util.Scanner;
import java.util.BitSet;
public class Part1 {

    public static int n;

    public static int countNeighbors(BitSet b, int index){
        int count = 0;
        int[] offsets = new int[]{1, n, n-1, n+1};
        for(int i = 0; i < offsets.length; i++){
            if(b.get(index - offsets[i])) count++;
            if(b.get(index + offsets[i])) count++;
        }
        return count;
    }

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            StringBuilder s = new StringBuilder();
            for(n = 2; scan.hasNextLine(); n++)
                s.append(scan.nextLine());
            BitSet b = new BitSet(n*n);
            int index = n + 1;
            for(char c : s.toString().toCharArray()){
                if(c == '#') b.set(index);
                index += (index % n == n - 2) ? 3 : 1;
            }
            for(int iterations = 0; iterations < 100; iterations++){
                BitSet child = (BitSet)b.clone();
                for(int i = 1; i < n-1; i++)
                    for(int j = 1; j < n-1; j++){
                        index = i * n + j;
                        int neighbors = countNeighbors(b, index);
                        if(b.get(index) && (neighbors < 2 || neighbors > 3)) child.flip(index);
                        if(!b.get(index) && (neighbors == 3)) child.flip(index);
                    }
                b = child;
            }
            System.out.println(b.cardinality());
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}