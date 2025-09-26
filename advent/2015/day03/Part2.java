import java.io.File;
import java.util.Scanner;
import java.util.BitSet;
public class Part2 {

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            char[] c = scan.nextLine().toCharArray();
            int size = c.length * 2 + 10, total = 1;
            int x1 = size / 2, y1 = size / 2, x2 = size / 2, y2 = size / 2;
            BitSet b = new BitSet(size*size);
            b.set(x1*size + y1);
            for(int i = 0; i < c.length; i++){
                switch(c[i]){
                    case '>': if(i % 2 == 0) x1++; else x2++; break;
                    case '<': if(i % 2 == 0) x1--; else x2--; break;
                    case 'v': if(i % 2 == 0) y1++; else y2++; break;
                    case '^': if(i % 2 == 0) y1--; else y2--; break;
                }
                int x = (i % 2 == 0) ? x1 : x2, y = (i % 2 == 0) ? y1 : y2;
                if(!b.get(x*size + y)) total++;
                b.set(x*size + y);
            }
            System.out.println(total);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}