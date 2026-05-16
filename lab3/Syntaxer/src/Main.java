import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String inputFile = "../test/Test.cs";
        String cleanedFile = "../test/TestCleaned.cs";

        try {
            String cleanedCode = Preprocessor.process(
                    inputFile,
                    cleanedFile
            );

            Lexer lexer = new Lexer();

            lexer.analyze(cleanedCode);
            lexer.printResults();
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}