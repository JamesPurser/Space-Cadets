import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

class EmailReader {
    public static void main(String[] args) throws IOException {

        String emailID = readEmail();
        System.out.print(fetchName("https://www.ecs.soton.ac.uk/people/" + emailID));

    }

    //Retrieves the email from the file and extracts the user's email ID.
    static String readEmail() throws IOException {

        BufferedReader input = new BufferedReader(new FileReader("email"));
        String email = input.readLine();
        input.close();

        email.trim();
        String[] emailID = email.split("@", 2);

        return emailID[0];
    }
    
    //Retrieves the name of user from the website's title.
    static String fetchName(String url) throws IOException {

        Document document = Jsoup.connect(url).get();
        String[] name = document.title().split(" \\| ", 3);

        return name[0];
    }
  }