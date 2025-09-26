import java.io.File;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
public class Part2 {

    public static int sum(Object o){
        int total = 0;
        if(o instanceof JSONObject obj){
            for(String k : obj.keySet())
                try { if(obj.getString(k).equals("red")) return 0; }
                catch(Exception e){ continue; }
            for(String k : obj.keySet())
                try { total += obj.getInt(k); }
                catch(Exception e){ total += sum(obj.get(k)); }
            return total;
        } else if(o instanceof JSONArray arr){
            for(Object x : arr)
                total += sum(x);
        } else if(o instanceof Integer i) return i;
        return total;
    }

    public static void main(String[] args){
        try {
            File f = new File("input.txt");
            Scanner scan = new Scanner(f);
            JSONArray root = new JSONArray(scan.nextLine());
            System.out.println(sum(root));
        } catch(Exception e){
            System.out.println("File does not exist." + e);
        }
    }
}