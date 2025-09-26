import java.io.File;
import java.util.Scanner;
import java.util.Map;
public class Part2 {

    public static int getNum(String s){
        return Integer.parseInt(s.replaceAll(",", ""));
    }

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            Map<String, Integer> map = Map.of("children:", 0, "cats:", 1, "samoyeds:", 2, "pomeranians:", 3, "akitas:", 4, "vizslas:", 5, "goldfish:", 6, "trees:", 7, "cars:", 8, "perfumes:", 9);
            int[] match = new int[]{3, 7, 2, 3, 0, 0, 5, 3, 2, 1};
            while(scan.hasNextLine()){
                String[] line = scan.nextLine().split(" ");
                boolean flag = true;
                for(int i = 2; i <= 6; i += 2){
                    int compound = map.get(line[i]);
                    int a = getNum(line[i+1]), b = match[compound];
                    if((compound == 1 || compound == 7) && flag) flag = a > b;
                    else if((compound == 3 || compound == 6) && flag) flag = a < b;
                    else if(flag) flag = a == b;
                }
                if(flag){
                    System.out.println(line[1].replaceAll("\\D", ""));
                    break;
                }
            }
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}