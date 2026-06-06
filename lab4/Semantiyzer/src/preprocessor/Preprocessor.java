package preprocessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Preprocessor {

    public static String process(String inputFile,
                                 String outputFile) throws IOException {
        String code = Files.readString(Path.of(inputFile));

        int openComments = countOccurrences(code, "/*");
        int closeComments = countOccurrences(code, "*/");

        if (openComments != closeComments) {
            throw new RuntimeException(
                    "Ошибка: незакрытый многострочный комментарий"
            );
        }

        // Удаление многострочных комментариев
        code = code.replaceAll("(?s)/\\*.*?\\*/", "");

        // Удаление однострочных комментариев
        code = code.replaceAll("//.*", "");

        // Переименование класса
        code = code.replaceAll(
                "\\bclass\\s+Test\\b",
                "class TestCleaned"
        );

        String[] lines = code.split("\\R");

        List<String> cleaned = new ArrayList<>();

        for (String line : lines) {

            String normalized =
                    line.replaceAll("\\s+", " ").trim();

            if (!normalized.isEmpty()) {
                cleaned.add(normalized);
            }
        }

        Files.write(Path.of(outputFile), cleaned);

        System.out.println("Препроцессор завершил работу.");
        System.out.println("Очищенный файл: " + outputFile);

        return Files.readString(Path.of(outputFile));
    }

    private static int countOccurrences(String text,
                                        String pattern) {
        int count = 0;
        int index = 0;

        while ((index = text.indexOf(pattern, index)) != -1) {
            count++;
            index += pattern.length();
        }

        return count;
    }
}
