import java.io.File;
import java.util.Scanner;
public class Part1 {

    public static int bDmg, bArm;

    public static boolean fight(int bHP, int pDmg, int pArm){
        int pTurnDmg = Math.max(1, bDmg - pArm);
        int bTurnDmg = Math.max(1, pDmg - bArm);
        return Math.ceil(100.0/pTurnDmg) >= Math.ceil((double)bHP/bTurnDmg);
    }

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            int bHP = Integer.parseInt(scan.nextLine().split(" ")[2]);
            bDmg = Integer.parseInt(scan.nextLine().split(" ")[1]);
            bArm = Integer.parseInt(scan.nextLine().split(" ")[1]);
            int[][] w = new int[][]{{8, 4}, {10, 5}, {25, 6}, {40, 7}, {74, 8}};
            int[][] a = new int[][]{{0, 0}, {13, 1}, {31, 2}, {53, 3}, {75, 4}, {102, 5}};
            int[][] r = new int[][]{{0, 0, 0}, {25, 1, 0}, {50, 2, 0}, {100, 3, 0}, {20, 0, 1}, {40, 0, 2}, {80, 0, 3}};
            int best = Integer.MAX_VALUE;
            for(int i = 0; i < w.length; i++)
                for(int j = 0; j < a.length; j++)
                    for(int k = 0; k < r.length; k++)
                        for(int l = k + (k == 0 ? 0 : 1); l < r.length; l++){
                            int pDmg = w[i][1] + r[k][1] + r[l][1];
                            int pArm = a[j][1] + r[k][2] + r[l][2];
                            if(fight(bHP, pDmg, pArm)) best = Math.min(best, w[i][0] + a[j][0] + r[k][0] + r[l][0]);
                        }
        System.out.println(best);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}