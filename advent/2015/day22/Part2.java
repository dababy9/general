import java.io.File;
import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.TreeMap;
public class Part2 {

    public int best = Integer.MAX_VALUE;
    public int dmg = 0;

    public void findBest(int pHP, int bHP, int pMana, int mSpent, int s, int p, int r, boolean turn){
        if(turn && pHP-- <= 0) return;
        if(bHP <= 0){
            best = Math.min(best, mSpent);
            return;
        }
        if(mSpent >= best) return;
        int shield = s-- > 0 ? 7 : 0;
        if(p-- > 0) bHP -= 3;
        if(r-- > 0) pMana += 101;
        if(!turn){
            findBest(pHP - Math.max(1, dmg - shield), bHP, pMana, mSpent, s, p, r, true);
            return;
        }
        if(pMana >= 229 && r <= 0) findBest(pHP, bHP, pMana-229, mSpent+229, s, p, 5, false);
        if(pMana >= 173 && p <= 0) findBest(pHP, bHP, pMana-173, mSpent+173, s, 6, r, false);
        if(pMana >= 113 && s <= 0) findBest(pHP, bHP, pMana-113, mSpent+113, 6, p, r, false);
        if(pMana >= 73) findBest(pHP+2, bHP-2, pMana-73, mSpent+73, s, p, r, false);
        if(pMana >= 53) findBest(pHP, bHP-4, pMana-53, mSpent+53, s, p, r, false);
    }

    public void run(){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            int hp = Integer.parseInt(scan.nextLine().split(" ")[2]);
            dmg = Integer.parseInt(scan.nextLine().split(" ")[1]);
            findBest(50, hp, 500, 0, 0, 0, 0, true);
            System.out.println(best);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }

    public static void main(String[] args){
        Part2 run = new Part2();
        run.run();
    }
} 