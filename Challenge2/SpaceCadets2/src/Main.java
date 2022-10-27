import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Interpreter interpreter = new Interpreter(new File("sourcecode2.txt")); //Setup where you declare which file you want it to read from.
    }
}