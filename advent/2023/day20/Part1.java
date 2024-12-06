import java.io.File;
import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.TreeMap;
public class Part1 {

    public class Tuple {
        Module from, to;
        boolean pulse;

        Tuple(Module f, boolean p, Module t){
            from = f;
            pulse = p;
            to = t;
        }
    }

    public abstract class Module {
        ArrayList<Module> outputs;
        String name;
        int numInputs = 0;
        public Module(String n){
            outputs = new ArrayList<>();
            name = n;
        }
        public abstract ArrayList<Tuple> processInput(Module from, boolean pulse);
    }

    public class FlipFlop extends Module {
        boolean status = false;
        public FlipFlop(String n){
            super(n);
        }
        public ArrayList<Tuple> processInput(Module from, boolean pulse){
            ArrayList<Tuple> ret = new ArrayList<>();
            if(!pulse){
                status = !status;
                for(Module m : outputs) ret.add(new Tuple(this, status, m));
            }
            return ret;
        }
    }

    public class Conjunction extends Module {
        TreeMap<Module, Boolean> prevs;
        public Conjunction(String n){
            super(n);
            prevs = new TreeMap<>();
        }
        public ArrayList<Tuple> processInput(Module from, boolean pulse){
            ArrayList<Tuple> ret = new ArrayList<>();
            int numHigh = 0;
            for(Module key : prevs.keySet()){
                Boolean prev = prevs.get(key);
                if(prev != null && prev.booleanValue())
                    numHigh++;
            }
            boolean status = numHigh != numInputs;
            for(Module m : outputs) ret.add(new Tuple(this, status, m));
            return ret;
        }
    }

    public void run(){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            while(scan.hasNextLine()){
                String line = scan.nextLine();
            }
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }

    public static void main(String[] args){
        Part1 run = new Part1();
        TreeMap<Integer, Boolean> map = new TreeMap<>();
        boolean result = map.get(0);
        System.out.println(result);
        //run.run();
    }
} 