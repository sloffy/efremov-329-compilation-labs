import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class Preprocessor {

    public static void main(String[] args) {

        String inputFile = "../test/TestProgram.cs";
        String outputFile = "../test/TestProgramCleaned.cs";

        try {
            String code = Files.readString(Path.of(inputFile));

            boolean error = false;

            int openComments = countOccurrences(code, "/*");
            int closeComments = countOccurrences(code, "*/");

            if (openComments != closeComments) {
                System.out.println("Ошибка: незакрытый многострочный комментарий");
                System.out.println("Файл не будет сохранён.");
                return;
            }

            code = code.replaceAll("(?s)/\\*.*?\\*/", "");

            code = code.replaceAll("//.*", "");

            code = code.replaceAll("\\bclass\\s+TestProgram\\b", "class TestProgramCleaned");

            String[] lines = code.split("\\R");

            List<String> cleaned = new ArrayList<>();

            for (String line : lines) {

                String normalized = line.replaceAll("\\s+", " ").trim();

                if (!normalized.isEmpty()) {
                    cleaned.add(normalized);
                }
            }

            Files.write(Path.of(outputFile), cleaned);

            System.out.println("Файл успешно обработан.");
            System.out.println("Результат сохранён в: " + outputFile);

            if (!error) {
                System.out.println("Ошибок не выявлено");
            }

        } catch (IOException e) {
            System.out.println("Ошибка чтения/записи файла: " + e.getMessage());
        }
    }

    private static int countOccurrences(String text, String pattern) {
        int count = 0;
        int index = 0;

        while ((index = text.indexOf(pattern, index)) != -1) {
            count++;
            index += pattern.length();
        }

        return count;
    }
}