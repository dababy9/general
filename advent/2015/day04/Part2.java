import java.io.File;
import java.util.Scanner;
import java.security.MessageDigest;
public class Part2 {

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            String original = scan.nextLine();
            MessageDigest md = MessageDigest.getInstance("MD5");
            for(int i = 0; true; i++){
                String appended = original + String.valueOf(i);
                byte[] digest = md.digest(appended.getBytes("UTF-8"));
                if(digest[0] == 0 && digest[1] == 0 && digest[2] == 0){
                    System.out.println(i);
                    break;
                }
            }
        } catch(Exception e){
            System.out.println("File does not exist.");
        }
    }
} 