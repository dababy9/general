import java.io.File;
import java.util.Scanner;
public class Part1 {

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            int target = Integer.parseInt(scan.nextLine());
            int maxHouse = 1000000;
            int[] houses = new int[maxHouse];
            for(int elf = 1; elf < maxHouse; elf++)
                for(int currHouse = elf; currHouse < maxHouse; currHouse += elf)
                    houses[currHouse] += elf;
            for(int i = 0; i < maxHouse; i++)
                if(houses[i]*10 >= target){
                    System.out.println(i);
                    break;
                }
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
} 