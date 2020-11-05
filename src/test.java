import java.io.FileNotFoundException;

public class test {
    public static void main(String[] args) throws FileNotFoundException {
        parser p = new parser("abbcbcbcbcde$",9, 6, 3, 4);
        System.out.println(p.parse());
    }
}
