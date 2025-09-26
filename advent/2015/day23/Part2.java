import java.io.File;
import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.TreeMap;
public class Part2 {

    public class Instruction {
        public int type;
        public boolean r;
        public int offset;

        public Instruction(String s){
            String[] arr = s.split(" ");
            if(arr[0].equals("hlf")){
                type = 0;
                r = arr[1].equals("a");
            } else if(arr[0].equals("tpl")){
                type = 1;
                r = arr[1].equals("a");
            } else if(arr[0].equals("inc")){
                type = 2;
                r = arr[1].equals("a");
            } else if(arr[0].equals("jmp")){
                type = 3;
                offset = Integer.parseInt(arr[1]);
            } else if(arr[0].equals("jie")){
                type = 4;
                r = arr[1].equals("a,");
                offset = Integer.parseInt(arr[2]);
            } else {
                type = 5;
                r = arr[1].equals("a,");
                offset = Integer.parseInt(arr[2]);
            }
        }
    }

    public void run(){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            ArrayList<Instruction> list = new ArrayList<>();
            while(scan.hasNextLine())
                list.add(new Instruction(scan.nextLine()));
            int a = 1, b = 0, i = 0, n = list.size();
            while(i >= 0 && i < n){
                Instruction x = list.get(i);
                switch(x.type){
                    case 0: if(x.r) a /= 2; else b /= 2; i++; break;
                    case 1: if(x.r) a *= 3; else b *= 3; i++; break;
                    case 2: if(x.r) a++; else b++; i++; break;
                    case 3: i += x.offset; break;
                    case 4: if((x.r ? a : b) % 2 == 0) i += x.offset; else i++; break;
                    default: if((x.r ? a : b) == 1) i += x.offset; else i++; break;
                }
            }
            System.out.println(b);
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }

    public static void main(String[] args){
        Part2 run = new Part2();
        run.run();
    }
} 