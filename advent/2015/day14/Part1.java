import java.io.File;
import java.util.Scanner;
public class Part1 {

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            int maxDistance = 0;
            int raceTime = 2503;
            while(scan.hasNextLine()){
                String[] line = scan.nextLine().split(" ");
                int r = Integer.parseInt(line[3]), t1 = Integer.parseInt(line[6]), t2 = Integer.parseInt(line[13]);
                int distance = raceTime / (t1 + t2) * r * t1 + Math.min(t1, raceTime % (t1 + t2)) * r;
                if(distance > maxDistance) maxDistance = distance;
            }
            System.out.println(maxDistance);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}