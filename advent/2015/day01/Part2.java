import java.io.File;
import java.util.Scanner;
public class Part2 {

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            char[] c = scan.nextLine().toCharArray();
            int sum = 0;
            for(int i = 0; i < c.length; i++){
                sum += (c[i] == '(' ? 1 : -1);
                if(sum < 0){
                    System.out.println(i+1);
                    break;
                }
            }
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
}