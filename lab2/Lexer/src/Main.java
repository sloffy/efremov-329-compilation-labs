import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        try {
            String code = Files.readString(Paths.get("../test/Test.cs"));

            Lexer lexer = new Lexer();

            lexer.analyze(code);
            lexer.printResults();
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}