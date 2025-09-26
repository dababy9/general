import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
public class Part2 {

    public static class Reindeer {
        int rate, flyTime, restTime;
        int t, distance = 0, points = 0;

        public Reindeer(String r, String ft, String rt){
            rate = Integer.parseInt(r);
            flyTime = t = Integer.parseInt(ft);
            restTime = Integer.parseInt(rt);
        }

        public Reindeer move(){
            if(--t >= 0) distance += rate;
            if(t + restTime == 0) t = flyTime;
            return this;
        }
    }

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            ArrayList<Reindeer> racers = new ArrayList<>();
            while(scan.hasNextLine()){
                String[] line = scan.nextLine().split(" ");
                racers.add(new Reindeer(line[3], line[6], line[13]));
            }
            for(int i = 0; i < 2503; i++){
                int max = 0;
                for(Reindeer r : racers)
                    max = Math.max(r.move().distance, max);
                for(Reindeer r : racers)
                    if(r.distance == max) r.points++;
            }
            int maxPoints = 0;
            for(Reindeer r : racers)
                maxPoints = Math.max(maxPoints, r.points);
            System.out.println(maxPoints);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}