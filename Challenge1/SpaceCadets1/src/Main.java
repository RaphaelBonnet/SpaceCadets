import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {

        Main myCode = new Main();
        myCode.retrieveName(myCode.constructWebAddress(myCode.inputID()));
    }

    public String inputID() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter University Email ID: ");
        String id = scan.next();
        return id;
    }

    public URL constructWebAddress(String id) throws MalformedURLException {
        try {
            URL webPage = new URL("https", "www.southampton.ac.uk", "/people/" + id);
            return webPage;
        } catch (Exception e) {
            System.out.println("URL Malformed");
        }
        return null;
    }

    public void retrieveName(URL url) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String nameLine;
        do {
            nameLine = reader.readLine();
        } while (nameLine == null || !nameLine.contains("og:title"));
        String name = nameLine.split("content=\"")[1].split("\"")[0];
        System.out.println(name);

    }


}