import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
public class Part2 {

    public static class Wire {
        public int value;
        public String name, w1 = "0", w2 = "0";
        public char op;
        public static HashMap<String, Wire> map = new HashMap<>();
        public static HashMap<String, Integer> memo = new HashMap<>();

        public Wire(String[] rule, String n){
            name = n;
            switch(rule.length){
                case 3: {
                    try { value = Integer.parseInt(rule[0]); op = 'v'; }
                    catch(Exception e){ w1 = rule[0]; op = 'V'; }
                }; break;
                case 4: w1 = rule[1]; op = 'n'; break;
                case 5: {
                    w1 = rule[0];
                    if(rule[1].equals("AND") || rule[1].equals("OR")){
                        w2 = rule[2];
                        op = (rule[1].equals("AND") ? 'a' : 'o');
                    } else {
                        value = Integer.parseInt(rule[2]);
                        op = (rule[1].equals("LSHIFT") ? 'l' : 'r');
                    }
                }; break;
            }
        }

        public int getValue(){
            int rValue = 0;
            try { rValue = memo.get(name); }
            catch(Exception e){
                int w1v, w2v;
                try { w1v = map.get(w1).getValue(); }
                catch(Exception x){ w1v = Integer.parseInt(w1); }
                try { w2v = map.get(w2).getValue(); }
                catch(Exception x){ w2v = Integer.parseInt(w2); }
                switch(op){
                    case 'v': rValue = value; break;
                    case 'V': rValue = w1v; break;
                    case 'n': rValue = ~w1v; break;
                    case 'a': rValue = (w1v & w2v); break;
                    case 'o': rValue = (w1v | w2v); break;
                    case 'l': rValue = (w1v << value); break;
                    case 'r': rValue = (w1v >> value); break;
                }
                memo.put(name, rValue);
            }
            return rValue;
        }
    }

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            while(scan.hasNextLine()){
                String[] line = scan.nextLine().split(" ");
                String name = line[line.length-1];
                Wire.map.put(name, new Wire(line, name));
            }
            Wire.map.put("b", new Wire(new String[]{Integer.toString(Wire.map.get("a").getValue() & 0x0000FFFF), "->", "b"}, "b"));
            Wire.memo = new HashMap<>();
            System.out.println(Wire.map.get("a").getValue() & 0x0000FFFF);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}