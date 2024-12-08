import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Deque;
import java.util.LinkedList;
public class Part2 {

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
        ArrayList<Module> outputs = new ArrayList<>();
        int numInputs = 0;
        String name;

        public Module(String n){
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
        HashMap<Module, Boolean> prevs = new HashMap<>();
        
        public Conjunction(String n){
            super(n);
        }
        public ArrayList<Tuple> processInput(Module from, boolean pulse){
            prevs.put(from, pulse);
            ArrayList<Tuple> ret = new ArrayList<>();
            int numHigh = 0;
            for(Module key : prevs.keySet()){
                Boolean prev = prevs.get(key);
                if(prev != null && prev.booleanValue())
                    numHigh++;
            }
            for(Module m : outputs) ret.add(new Tuple(this, numHigh != numInputs, m));
            return ret;
        }
    }

    public class Broadcaster extends Module {
        public Broadcaster(String n){
            super(n);
        }
        public ArrayList<Tuple> processInput(Module from, boolean pulse){
            ArrayList<Tuple> ret = new ArrayList<>();
            for(Module m : outputs) ret.add(new Tuple(this, pulse, m));
            return ret;
        }
    }

    public class Dummy extends Module {
        public Dummy(){
            super("dummy");
        }
        public ArrayList<Tuple> processInput(Module from, boolean pulse){
            return new ArrayList<>();
        }
    }

    public void run(){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            HashMap<Module, String[]> outs = new HashMap<>();
            HashMap<String, Module> modules = new HashMap<>();
            while(scan.hasNextLine()){
                String[] line = scan.nextLine().split(" -> ");
                String[] outputs = line[1].split(", ");
                Module curr;
                String name;
                if(line[0].equals("broadcaster")){
                    name = line[0];
                    curr = new Broadcaster(name);
                } else if(line[0].charAt(0) == '%'){
                    name = line[0].substring(1);
                    curr = new FlipFlop(name);
                } else {
                    name = line[0].substring(1);
                    curr = new Conjunction(name);
                }
                outs.put(curr, outputs);
                modules.put(name, curr);
            }
            Module dummy = new Dummy();
            for(Module m : outs.keySet()){
                for(String s : outs.get(m)){
                    Module out = modules.getOrDefault(s, dummy);
                    out.numInputs++;
                    m.outputs.add(out);
                }
            }
            Deque<Tuple> queue = new LinkedList<>();
            boolean found = false;
            for(int i = 0; !found; i++){
                queue.add(new Tuple(null, false, modules.get("broadcaster")));
                while(!queue.isEmpty()){
                    Tuple t = queue.poll();
                    if(t.to == dummy && !t.pulse){
                        System.out.println(i);
                        found = true;
                        break;
                    }
                    for(Tuple out : t.to.processInput(t.from, t.pulse))
                        queue.add(out);
                }
            }
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }

    public static void main(String[] args){
        Part2 run = new Part2();
        run.run();
    }
} 