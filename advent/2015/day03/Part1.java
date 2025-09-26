import java.io.File;
import java.util.Scanner;
import java.util.BitSet;
public class Part1 {

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            char[] c = scan.nextLine().toCharArray();
            int size = c.length * 2 + 10, total = 1;
            int x = size / 2, y = size / 2;
            BitSet b = new BitSet(size*size);
            b.set(x*size + y);
            for(int i = 0; i < c.length; i++){
                switch(c[i]){
                    case '>': x++; break;
                    case '<': x--; break;
                    case 'v': y++; break;
                    case '^': y--; break;
                }
                if(!b.get(x*size + y)) total++;
                b.set(x*size + y);
            }
            System.out.println(total);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}